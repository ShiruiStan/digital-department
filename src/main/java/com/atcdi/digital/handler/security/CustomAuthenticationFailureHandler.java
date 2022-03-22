package com.atcdi.digital.handler.security;

import com.atcdi.digital.entity.StandardException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e){
        if (e instanceof LockedException) {
            throw new StandardException(401, "账户被锁定，请联系管理员!");
        } else if (e instanceof CredentialsExpiredException) {
            throw new StandardException(401, "密码过期，请联系管理员!");
        } else if (e instanceof AccountExpiredException) {
            throw new StandardException(401, "账户过期，请联系管理员!");
        } else if (e instanceof DisabledException) {
            throw new StandardException(401, "账户被禁用，请联系管理员!");
        } else if (e instanceof BadCredentialsException) {
            throw new StandardException(401, "用户名或者密码错误，请重新输入!");
        }else {
            throw new StandardException(401, e.getMessage());
        }

    }
}
