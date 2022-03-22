package com.atcdi.digital.config;


import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.util.Arrays;


@Component
@Order(-999)
public class PreFilterConfig implements Filter {
    @Resource
    HandlerExceptionResolver handlerExceptionResolver;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws RuntimeException {
        HttpServletRequest req = (HttpServletRequest) request;
        System.out.println(req.getMethod() + " " + req.getRequestURI());
        if (req.getCookies() != null) {
            for (Cookie c : req.getCookies()) {
                System.out.println("      " + c.getName() + " " + c.getValue() + " " + c.getDomain());
            }
        }

        HttpServletResponse res = (HttpServletResponse) response;
        res.setHeader("x-frame-options", "SAMEORIGIN");
        String origin = req.getHeader("Origin");
        if (origin != null) res.setHeader("Access-Control-Allow-Origin", origin);
//        String header = req.getHeader("Access-Control-Request-Headers");
        String header = "Accept, Origin, Content-Type, Authorization, Content-Length";
        res.setHeader("Access-Control-Allow-Headers", header);
        res.setHeader("Access-Control-Allow-Methods", "*");
        res.setHeader("Access-Control-Max-Age", "3600");
        res.setHeader("Access-Control-Allow-Credentials", "true");
        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            handlerExceptionResolver.resolveException(req, res, null, e);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }
}