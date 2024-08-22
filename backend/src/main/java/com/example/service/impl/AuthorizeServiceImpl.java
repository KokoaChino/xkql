package com.example.service.impl;

import com.example.entity.auth.Account;
import com.example.mapper.UserMapper;
import com.example.service.AuthorizeService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;


@Service // Spring 服务类
public class AuthorizeServiceImpl implements AuthorizeService {

    @Value("${spring.mail.username}")
    String from; // 存储邮件发送者的邮箱地址

    @Resource
    UserMapper mapper; // 用户数据交互接口，负责数据库操作

    @Resource
    MailSender mailSender; // 邮件发送组件

    @Resource // 用于与 Redis 进行字符串操作
    StringRedisTemplate template; // Redis 字符串模板，用于存取数据

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(); // 用于密码加密

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { // 根据用户名加载用户信息
        if(username == null) // 检查用户名是否为空
            throw new UsernameNotFoundException("用户名不能为空"); // 抛出异常提示用户名不能为空
        Account account = mapper.findAccountByNameOrEmail(username); // 通过用户名或邮箱查找账户信息
        if(account == null) // 如果账户不存在
            throw new UsernameNotFoundException("用户名或密码错误"); // 抛出异常提示用户名或密码错误
        return User // 创建 UserDetails 对象，返回用户信息
                .withUsername(account.getUsername()) // 设置用户名
                .password(account.getPassword()) // 设置密码
                .roles("user") // 设置用户角色为 "user"
                .build();
    }

    @Override
    public String sendValidateEmail(String email, String sessionId, boolean hasAccount) { // 发送验证邮件
        String key = "email:" + sessionId + ":" + email + ":" +hasAccount; // 操作 Redis 的 key，用来唯一标识邮件请求
        if(Boolean.TRUE.equals(template.hasKey(key))) { // 检查重发请求的频率
            Long expire = Optional.ofNullable(template.getExpire(key, TimeUnit.SECONDS)).orElse(0L); // 获取 key 的过期时间
            if(expire > 120) return "请求频繁，请稍后再试"; // 如果过期时间大于 120 秒，返回提示
        }
        Account account = mapper.findAccountByNameOrEmail(email); // 查找邮箱对应的账户
        if(hasAccount && account == null) return "没有此邮件地址的账户"; // 如果请求存在账户，但没有找到
        if(!hasAccount && account != null) return "此邮箱已被其他用户注册"; // 如果请求不带账户，检测邮箱已被使用
        Random random = new Random();
        String code = String.format("%06d", random.nextInt(1000000)); // 创建验证码
        SimpleMailMessage message = new SimpleMailMessage(); // 创建邮件消息对象
        message.setFrom(from); // 设置邮件发送者
        message.setTo(email); // 设置邮件接收者
        message.setSubject("您的验证邮件"); // 设置邮件主题
        message.setText("验证码是："+code); // 设置邮件内容，包含验证码
        try {
            mailSender.send(message); // 发送邮件
            template.opsForValue().set(key, String.valueOf(code), 3, TimeUnit.MINUTES); // 在 Redis 中保存验证码，有效期为 3 分钟
            return null; // 返回 null 代表发送成功，无错误
        } catch (MailException e) { // 捕获发送邮件时的异常
            e.printStackTrace(); // 打印异常信息
            return "邮件发送失败，请检查邮件地址是否有效"; // 返回错误信息
        }
    }

    @Override
    public String validateAndRegister(String username, String password, String email, String code, String sessionId) {
        String key = "email:" + sessionId + ":" + email + ":false"; // 创建一个唯一的键，用于存储和验证该会话中的电子邮件验证码
        if(Boolean.TRUE.equals(template.hasKey(key))) { // 检查 Redis 中是否存在与该键对应的记录
            String s = template.opsForValue().get(key); // 从 Redis 获取与该键相关联的验证码
            if(s == null) return "验证码失效，请重新请求"; // 如果获取的验证码为空，则提示用户验证码已失效
            if(s.equals(code)) { // 验证用户输入的验证码是否与存储的验证码相等
                Account account = mapper.findAccountByNameOrEmail(username); // 查找数据库中是否已经存在相同用户名或电子邮件的账户
                if(account != null) return "此用户名已被注册，请更换用户名"; // 如果账户已经存在，返回提示信息以更换用户名
                template.delete(key); // 验证成功后，从 Redis 中删除验证码，确保验证码只能使用一次
                password = encoder.encode(password); // 对提供的密码进行加密处理
                if (mapper.createAccount(username, password, email) > 0) { // 在数据库中创建新账户，检查操作是否成功
                    mapper.createDatas(username);
                    return null; // 如果创建账户成功，返回 null 表示没有错误
                } else {
                    return "内部错误，请联系管理员"; // 如果创建账户失败，返回内部错误提示
                }
            } else {
                return "验证码错误，请检查后再提交"; // 如果验证码不匹配，返回验证码错误提示
            }
        } else {
            return "请先请求一封验证码邮件"; // 如果 Redis 中没有此键，提示用户先请求验证码邮件
        }
    }

    @Override
    public String validateOnly(String email, String code, String sessionId) {
        String key = "email:" + sessionId + ":" + email + ":true"; // 创建一个唯一的键，用于验证仅验证码，不涉及账户注册
        if(Boolean.TRUE.equals(template.hasKey(key))) { // 检查 Redis 中是否存在该键对应的验证码记录
            String s = template.opsForValue().get(key); // 从 Redis 中获取与该键关联的验证码
            if(s == null) return "验证码失效，请重新请求"; // 如果验证码为空，提示用户验证码已失效
            if(s.equals(code)) { // 比较用户输入的验证码与存储的验证码
                template.delete(key); // 验证成功后，从 Redis 中删除验证码，避免重复使用
                return null; // 返回 null 表示验证成功，没有错误
            } else {
                return "验证码错误，请检查后再提交"; // 如果验证码不匹配，返回验证码错误提示
            }
        } else {
            return "请先请求一封验证码邮件"; // 如果 Redis 中没有此键，提示用户先请求验证码邮件
        }
    }

    @Override
    public boolean resetPassword(String password, String email) {
        password = encoder.encode(password); // 对输入的新密码进行加密处理
        return mapper.resetPasswordByEmail(password, email) > 0; // 尝试通过电子邮件更新密码，并返回操作是否成功的布尔值
    }
}