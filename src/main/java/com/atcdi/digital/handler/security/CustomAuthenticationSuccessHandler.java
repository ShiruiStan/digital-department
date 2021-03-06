package com.atcdi.digital.handler.security;

import com.atcdi.digital.entity.StandardResponse;
import com.atcdi.digital.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Resource
    ObjectMapper mapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        ObjectNode res = mapper.createObjectNode();
        res.put("token", httpServletRequest.getSession().getId());
        User user = (User) httpServletRequest.getSession().getAttribute("user");
        res.put("userId", user.getUserId());
        httpServletResponse.getWriter().write(mapper.writeValueAsString(StandardResponse.success("登录成功", res)));
    }
}