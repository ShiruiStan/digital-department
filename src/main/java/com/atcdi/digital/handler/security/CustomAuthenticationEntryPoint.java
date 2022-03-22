package com.atcdi.digital.handler.security;

import com.atcdi.digital.entity.StandardException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e){
        System.out.println("---------------------------------");
        System.out.println(httpServletRequest.getMethod() + " " + httpServletRequest.getRequestURI());
        if (httpServletRequest.getCookies() != null) {
            for (Cookie c : httpServletRequest.getCookies()) {
                System.out.println("      " + c.getName() + " " + c.getValue() + " " + c.getDomain());
            }
        }
        throw new StandardException(401, "请先登录!");
    }
}
