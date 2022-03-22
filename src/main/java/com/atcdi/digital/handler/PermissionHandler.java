package com.atcdi.digital.handler;

import org.springframework.context.ApplicationContext;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;



public class PermissionHandler extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
        SessionHandler sessionHandler = ctx.getBean(SessionHandler.class);
        if (sessionHandler.hasPermission(request.getRequestURI())) {
            chain.doFilter(request, response);
        } else {
            throw new AccessDeniedException("");
        }

    }

}
