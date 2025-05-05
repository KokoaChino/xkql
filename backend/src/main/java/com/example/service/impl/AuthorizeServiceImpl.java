package com.example.service.impl;

import com.example.entity.common.Account;
import com.example.mapper.UserMapper;
import com.example.service.api.AuthorizeService;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;


@Service
public class AuthorizeServiceImpl implements AuthorizeService {

    @Value("${spring.mail.username}")
    String from;

    @Resource
    UserMapper mapper;

    @Resource
    JavaMailSender mailSender;

    @Resource
    StringRedisTemplate template;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null)
            throw new UsernameNotFoundException("用户名不能为空");
        Account account = mapper.findAccountByNameOrEmail(username);
        if (account == null)
            throw new UsernameNotFoundException("用户名或密码错误");
        return User
                .withUsername(account.getUsername())
                .password(account.getPassword())
                .roles("user")
                .build();
    }

    @Override
    public String sendValidateEmail(String email, String sessionId, boolean hasAccount) {
        String key = "email:" + sessionId + ":" + email + ":" + hasAccount;
        if (Boolean.TRUE.equals(template.hasKey(key))) {
            Long expire = Optional.ofNullable(template.getExpire(key, TimeUnit.SECONDS)).orElse(0L);
            if (expire > 120) return "请求频繁，请稍后再试";
        }
        Account account = mapper.findAccountByNameOrEmail(email);
        if (hasAccount && account == null) return "没有此邮件地址的账户";
        if (!hasAccount && account != null) return "此邮箱已被其他用户注册";
        String code = String.format("%06d", new Random().nextInt(1000000));
        Boolean res = sendCodeEmail(email, code);
        if (res) {
            template.opsForValue().set(key, code, 3, TimeUnit.MINUTES);
            return null;
        } else {
            return "邮件发送失败，请检查邮件地址是否有效";
        }
    }

    public Boolean sendCodeEmail(String email, String code) { // 发送验证码邮件
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
            helper.setFrom(from, "星开祈灵百宝箱");
            helper.setTo(email);
            helper.setSubject("【星开祈灵百宝箱】账户安全验证码（请及时查收）");
            String htmlContent = """
    <html>
    <body style='font-family: Microsoft YaHei, sans-serif;'>
        <p>尊敬的用户：</p>
        <p>您好！</p>
        <p>您正在申请【星开祈灵百宝箱】的账户安全验证服务，本次操作验证码为：<br>
        <strong style='color: #1890ff; font-size: 18px;'>%s</strong></p>
        
        <div style='color: #666; margin-top: 20px;'>
            <h4>【温馨提示】</h4>
            <ol>
                <li>本验证码3分钟内有效，请及时完成验证</li>
                <li>切勿向他人泄露验证码</li>
                <li>如非本人操作，请忽略本邮件</li>
            </ol>
        </div>
        
        <p>感谢您对星开祈灵的信任与支持！(≧∀≦)ゞ<br>
        <hr style='border: 0; border-top: 1px solid #eee; margin: 20px 0;'>
        <p style='color: #999; font-size: 12px;'>此邮件为系统自动发送，请勿直接回复</p>
    </body>
    </html>
    """.formatted(code);
            helper.setText(htmlContent, true);
            new Thread(() -> mailSender.send(message)).start();
            return true;
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String validateAndRegister(String username, String password, String email, String code, String sessionId) {
        String key = "email:" + sessionId + ":" + email + ":false";
        if (Boolean.TRUE.equals(template.hasKey(key))) {
            String s = template.opsForValue().get(key);
            if (s == null) return "验证码失效，请重新请求";
            if (s.equals(code)) {
                Account account = mapper.findAccountByNameOrEmail(username);
                if (account != null) return "此用户名已被注册，请更换用户名";
                template.delete(key);
                password = encoder.encode(password);
                if (mapper.createAccount(username, password, email) > 0) {
                    mapper.createData(username);
                    return null;
                } else {
                    return "内部错误，请联系作者：星开祈灵";
                }
            } else {
                return "验证码错误，请检查后再提交";
            }
        } else {
            return "请先请求一封验证码邮件";
        }
    }

    @Override
    public String validateOnly(String email, String code, String sessionId) {
        String key = "email:" + sessionId + ":" + email + ":true";
        if (Boolean.TRUE.equals(template.hasKey(key))) {
            String s = template.opsForValue().get(key);
            if (s == null) return "验证码失效，请重新请求";
            if (s.equals(code)) {
                template.delete(key);
                return null;
            } else {
                return "验证码错误，请检查后再提交";
            }
        } else {
            return "请先请求一封验证码邮件";
        }
    }

    @Override
    public boolean resetPassword(String password, String email) {
        password = encoder.encode(password);
        return mapper.resetPasswordByEmail(password, email) > 0;
    }

    @Override
    public Account getAccount(String username) { // 获取用户实体
        return mapper.findAccountByNameOrEmail(username);
    }
}