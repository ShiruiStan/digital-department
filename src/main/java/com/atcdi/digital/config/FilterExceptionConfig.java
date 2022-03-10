package com.atcdi.digital.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
@Order(-999)
public class FilterExceptionConfig implements Filter {
    @Autowired
    HandlerExceptionResolver handlerExceptionResolver;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws RuntimeException {
        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            handlerExceptionResolver.resolveException((HttpServletRequest)request, (HttpServletResponse)response, null, e);
        }
    }

    @Override
    public void init(javax.servlet.FilterConfig filterConfig){ }

    @Override
    public void destroy() {}
}