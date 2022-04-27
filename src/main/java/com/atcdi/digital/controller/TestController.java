package com.atcdi.digital.controller;


import com.atcdi.digital.entity.StandardResponse;
import com.atcdi.digital.entity.User;
import com.atcdi.digital.entity.project.Project;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.session.MapSession;
import org.springframework.session.SessionRepository;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;


@RestController
@RequestMapping("/test")
@Api(tags  = "测试接口")
public class TestController {

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
