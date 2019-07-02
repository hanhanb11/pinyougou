package com.pinyougou.manager.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/*
显示登陆用户名
 */
@RestController
@RequestMapping("/login")
public class LoginController {
    //将用户名封装到一个map集合中，返回客户端

    @RequestMapping("/name")
    public Map name() {
        //1.获取上下文 2.获取认证 3.获取认证用户
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Map map = new HashMap();
        map.put("loginName", name);
        return map;
    }
}
