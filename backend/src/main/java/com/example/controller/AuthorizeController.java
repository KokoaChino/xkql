package com.example.controller;

import com.example.entity.RestBean;
import com.example.mapper.UserMapper;
import com.example.service.AuthorizeService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Validated // 该注解启用方法参数的验证，对请求参数进行校验
@RestController // 表明这是一个 RESTful 控制器，自动转换返回值为 JSON 格式
@RequestMapping("/api/auth") // 将所有处理以 /api/auth 开头的请求映射到此控制器
public class AuthorizeController {

    private final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,}$"; // 定义有效邮箱地址的正则表达式
    private final String USERNAME_REGEX = "^[a-zA-Z0-9]+$"; // 定义有效用户名的正则表达式

    @Resource
    AuthorizeService service; // 授权服务，用于处理注册、验证等业务逻辑

    @Resource
    UserMapper mapper;

    @PostMapping("/valid-register-email")
    public RestBean<String> validateRegisterEmail(@Pattern(regexp = EMAIL_REGEX) @RequestParam("email") String email, // 校验输入的邮箱格式并从请求中获取
                                                  HttpSession session) { // 获取 HTTP 会话
        String s = service.sendValidateEmail(email, session.getId(), false); // 调用服务发送验证邮件，并传递会话 ID
        if (s == null) return RestBean.success("邮件已发送，请注意查收"); // 如果没有错误，返回成功信息
        else return RestBean.failure(400, s); // 返回带有错误代码和信息的失败响应
    }

    @PostMapping("/valid-reset-email")
    public RestBean<String> validateResetEmail(@Pattern(regexp = EMAIL_REGEX) @RequestParam("email") String email, // 校验输入的邮箱格式并从请求中获取
                                               HttpSession session) { // 获取 HTTP 会话
        String s = service.sendValidateEmail(email, session.getId(), true); // 调用服务发送重置邮件，并传递会话 ID
        if (s == null) return RestBean.success("邮件已发送，请注意查收"); // 如果没有错误，返回成功信息
        else return RestBean.failure(400, s); // 返回带有错误代码和信息的失败响应
    }

    @PostMapping("/register")
    public RestBean<String> registerUser(@Pattern(regexp = USERNAME_REGEX) @Length(min = 2, max = 8) @RequestParam("username") String username, // 校验用户名格式并范围
                                         @Length(min = 6, max = 16) @RequestParam("password") String password, // 校验密码长度
                                         @Pattern(regexp = EMAIL_REGEX) @RequestParam("email") String email, // 校验邮箱格式
                                         @Length(min = 6, max = 6) @RequestParam("code") String code, // 校验验证码长度
                                         HttpSession session) { // 获取 HTTP 会话
        String s = service.validateAndRegister(username, password, email, code, session.getId()); // 调用服务进行用户注册
        if(s == null) return RestBean.success("注册成功"); // 如果没有错误，返回注册成功的信息
        else return RestBean.failure(400, s); // 返回带有错误代码和信息的失败响应
    }

    @PostMapping("/start-reset")
    public RestBean<String> startReset(@Pattern(regexp = EMAIL_REGEX) @RequestParam("email") String email, // 校验输入的邮箱格式并从请求中获取
                                       @Length(min = 6, max = 6) @RequestParam("code") String code, // 校验验证码长度
                                       HttpSession session) { // 获取 HTTP 会话
        String s = service.validateOnly(email, code, session.getId()); // 调用服务进行邮箱及验证码的校验
        if(s == null) { // 如果没有错误
            session.setAttribute("reset-password", email); // 在会话中存储邮箱用于后续操作
            return RestBean.success(); // 返回成功的响应
        } else {
            return RestBean.failure(400, s); // 返回带有错误代码和信息的失败响应
        }
    }

    @PostMapping("/do-reset")
    public RestBean<String> resetPassword(@Length(min = 6, max = 16) @RequestParam("password") String password, // 校验密码长度
                                          HttpSession session) { // 获取 HTTP 会话
        String email = (String) session.getAttribute("reset-password"); // 从会话中获取 "reset-password" 属性
        if(email == null) { // 用户未完成邮箱验证
            return RestBean.failure(401, "请先完成邮箱验证"); // 提示用户完成邮箱验证
        } else if(service.resetPassword(password, email)) { // 检查密码重置是否成功
            session.removeAttribute("reset-password"); // 成功后从会话中移除 "reset-password" 属性
            return RestBean.success("密码重置成功"); // 提示用户密码重置成功
        } else { // 如果密码重置失败
            return RestBean.failure(500, "内部错误，请联系管理员"); // 返回失败响应，提示内部错误
        }
    }

    @PostMapping("/unsubscribe")
    public void unsubscribe(@RequestBody String username) {
        username = username.substring(1, username.length() - 1);
        mapper.deleteAccount(username);
        mapper.deleteAccountDatas(username);
    }
}