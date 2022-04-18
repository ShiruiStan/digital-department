package com.atcdi.digital.config;

import com.atcdi.digital.handler.PermissionHandler;
import com.atcdi.digital.handler.security.*;
import com.atcdi.digital.service.UserService;
import com.atcdi.digital.handler.security.LoginHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import org.springframework.session.MapSession;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.SessionRepository;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;

import javax.annotation.Resource;
import java.util.concurrent.ConcurrentHashMap;

@EnableWebSecurity
@Configuration
@EnableSpringHttpSession
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    @Resource
    UserService userService;
    @Resource
    LoginHandler loginHandler;
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

    @Bean
    public HttpSessionIdResolver sessionIdResolver(){
        return new HeaderHttpSessionIdResolver("Authorization");
    }

    @Bean
    public SessionRepository<MapSession> sessionRepository() {
        return new MapSessionRepository(new ConcurrentHashMap<>());
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(loginHandler);
        auth.userDetailsService(userService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers(HttpMethod.GET,
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/static/**",
                        "/favicon.ico",
                        "/v3/**",
                        "/swagger-resources/**",
                        "/swagger-ui/**",
                        "/test/**")
                .antMatchers(HttpMethod.OPTIONS, "/**");
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
                .addFilterAt(new PermissionHandler(), FilterSecurityInterceptor.class)
                .formLogin()
//                .loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .permitAll()


                .and()
                .exceptionHandling()
                .accessDeniedHandler(deniedHandler)

                .and()
                .logout()
                .logoutSuccessHandler(logoutHandler)
                .permitAll();

    }

}

