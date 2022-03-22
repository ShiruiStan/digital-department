package com.atcdi.digital.handler.security;

import com.atcdi.digital.entity.StandardResponse;
import com.atcdi.digital.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    @Resource
    ObjectMapper mapper;
    @Resource
    UserService userService;

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        userService.userLogout();
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter().write(mapper.writeValueAsString(StandardResponse.success("退出成功")));
    }
}