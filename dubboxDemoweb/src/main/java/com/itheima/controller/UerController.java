package com.itheima.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UerController {

    @Reference
    private UserService userService;

    @RequestMapping("/getName")
    @ResponseBody
    public String getName(){
     return userService.getName();
    }
}
