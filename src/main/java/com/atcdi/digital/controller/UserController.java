package com.atcdi.digital.controller;

import com.atcdi.digital.dao.PermissionDao;
import com.atcdi.digital.entity.StandardResponse;
import com.atcdi.digital.entity.User;
import com.atcdi.digital.service.PermissionService;
import com.atcdi.digital.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@RequestMapping("/user")
@Api(tags  = "用户相关接口")
public class UserController {
    @Resource
    UserService userService;
    @Resource
    PermissionService permissionService;


    @GetMapping("/info")
    public StandardResponse getUserInfo(){
        return StandardResponse.success(userService.getCurrentUser());
    }

    @PostMapping("/update")
    public StandardResponse updateUserInfo(@NonNull int id, @Nullable String phone){
        return StandardResponse.error(500, "接口暂未实现");
    }

    @GetMapping("/resetpassword")
    public StandardResponse resetPassword(){
        return userService.resetUserPassword() ? StandardResponse.success("重置成功，密码为123456") : StandardResponse.error(500, "密码重置失败");

    }

    @GetMapping("/roles")
    public StandardResponse getUserRoles(){
        return StandardResponse.success(userService.getUserRoles());
    }


    @GetMapping("/menus")
    public StandardResponse getUserMenus(){
        return StandardResponse.success(userService.getUserMenus());
    }






}
