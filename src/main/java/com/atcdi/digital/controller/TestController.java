package com.atcdi.digital.controller;


import com.atcdi.digital.entity.StandardResponse;
import com.atcdi.digital.entity.User;
import com.atcdi.digital.entity.project.Project;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDate;


@RestController
@RequestMapping("/test")
@Api(tags  = "测试接口")
public class TestController {

    @Resource
    ObjectMapper mapper;

    @GetMapping("/hello")
    @ApiOperation("测试接口")
    public StandardResponse hello() {
        return StandardResponse.success("测试成功");
    }

    @GetMapping("/date")
    public StandardResponse date(LocalDate d) {
        System.out.println(d);
        return StandardResponse.success("测试成功");
    }

    @GetMapping("/user")
    @ApiOperation("用户属性展示")
    public User user() {
        return new User();
    }

    @GetMapping("/project")
    @ApiOperation("项目属性展示")
    public Project project() {
        return new Project();
    }

    @GetMapping("/password")
    @ApiOperation("生成默认密码")
    public String password() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode("123456");
    }


}
