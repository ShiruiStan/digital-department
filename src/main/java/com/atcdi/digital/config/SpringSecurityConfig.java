package com.atcdi.digital.config;

import com.atcdi.digital.handler.PermissionHandler;
import com.atcdi.digital.handler.security.*;
import com.atcdi.digital.service.UserService;
import com.atcdi.digital.handler.security.AuthenticationHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.DefaultLoginPageConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.annotation.Resource;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    @Resource
    UserService userService;
    @Resource
    AuthenticationHandler authTool;
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
                .httpBasic()

                .and()
                .csrf().disable()
                .authorizeRequests()
                .anyRequest()
                .authenticated()

                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .addFilterBefore(new PermissionHandler(), FilterSecurityInterceptor.class)
                .formLogin()
//                .loginPage("/login")
                .loginProcessingUrl("/login")
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

