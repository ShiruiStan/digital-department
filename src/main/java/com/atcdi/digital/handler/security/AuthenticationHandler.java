package com.atcdi.digital.handler.security;

import com.atcdi.digital.entity.User;
import com.atcdi.digital.service.UserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
public class AuthenticationHandler implements AuthenticationProvider {
    @Resource
    UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userName = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        UserDetails user = userService.loadUserByUsername(userName);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("");
        }
        userService.userLogin((User)user);
        return new UsernamePasswordAuthenticationToken(userName, password, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
