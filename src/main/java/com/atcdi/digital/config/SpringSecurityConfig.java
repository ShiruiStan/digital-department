package com.atcdi.digital.config;

import com.atcdi.digital.hanlder.*;
import com.atcdi.digital.service.UserService;
import com.atcdi.digital.utils.AuthenticationUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import javax.annotation.Resource;

@EnableWebSecurity
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    @Resource
    UserService userService;
    @Resource
    AuthenticationUtil authTool;
    @Resource
    CustomAuthenticationEntryPoint authenticationEntryPoint;
    @Resource
    CustomAuthenticationSuccessHandler successHandler;
    @Resource
    CustomAuthenticationFailureHandler failureHandler;
    @Resource
    CustomLogoutSuccessHandler logoutHandler;
    @Resource
    CustomAccessDeniedHandler deniedHandler;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authTool);
        auth.userDetailsService(userService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers(HttpMethod.GET,
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/favicon.ico",
                        "/v3/**",
                        "/swagger-resources/**",
                        "/swagger-ui/**",
                        "/test/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .anyRequest()
                .authenticated()


                .and()
                .httpBasic().authenticationEntryPoint(authenticationEntryPoint)


                .and()
                .formLogin()
                .loginPage("/login")
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .permitAll()

//                .and()
//                .rememberMe()

                .and()
                .exceptionHandling()
                .accessDeniedHandler(deniedHandler)

                .and()
                .logout()
                .logoutSuccessHandler(logoutHandler)
                .permitAll();


    }

}

