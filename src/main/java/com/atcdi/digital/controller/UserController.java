package com.atcdi.digital.controller;

import com.atcdi.digital.entity.StandardResponse;
import com.atcdi.digital.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;


@RestController
@RequestMapping("/user")
@Api(tags = "用户相关接口")
public class UserController {
    @Resource
    UserService userService;


    @GetMapping("/info")
    public StandardResponse getUserInfo() {
        return StandardResponse.success(userService.getCurrentUser());
    }

    @PostMapping("/update")
    public StandardResponse updateUserInfo(@NonNull int id, @Nullable String phone) {
        return StandardResponse.error(500, "接口暂未实现");
    }

    @GetMapping("/reset_password")
    public StandardResponse resetPassword() {
        userService.modifyUserPassword("123456");
        return StandardResponse.success();
    }

    @PostMapping("/modify_password")
    public StandardResponse modifyPassword(@NonNull String password) {
        userService.modifyUserPassword(password);
        return StandardResponse.success();
    }

    @GetMapping("/roles")
    public StandardResponse getUserRoles() {
        return StandardResponse.success(userService.getUserRoleNames());
    }

    @GetMapping("/menus")
    public StandardResponse getUserMenus() {
        return StandardResponse.success(userService.getUserMenus());
    }

    @GetMapping("/permissions")
    public StandardResponse getUserPermissions() {
        return StandardResponse.success(userService.getPermissions());
    }

    @GetMapping("/list")
    public StandardResponse getAllUsers() {
        return StandardResponse.success(userService.getUserList());
    }

    @PostMapping("/avatar")
    public StandardResponse updateUserAvatar(@RequestBody MultipartFile avatar) throws IOException {
        userService.setUserAvatar(avatar);
        return StandardResponse.success();
    }
}
