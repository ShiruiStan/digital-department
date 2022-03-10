package com.atcdi.digital.controller;

import com.atcdi.digital.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/auth")
@Api(tags  = "权限相关接口")
public class AuthController {
    @Resource
    UserService userService;

    @GetMapping("/info")
    public String info(){
        return userService.getCurrentUserRoles().toString();
    }
}
