package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.user.AccountUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;


@RestController // 标记该类为 RESTful 控制器，自动将返回对象序列化为 JSON 格式
@RequestMapping("/api/user")
public class UserController {

    @GetMapping("/me")
    public RestBean<AccountUser> me(@SessionAttribute("account") AccountUser user) { // 从会话中获取名为 "account" 的属性
        return RestBean.success(user); // 返回成功响应
    }
}