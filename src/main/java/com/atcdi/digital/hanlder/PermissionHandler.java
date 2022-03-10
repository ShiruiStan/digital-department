package com.atcdi.digital.hanlder;

import com.atcdi.digital.utils.SessionUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class PermissionHandler implements Filter {
    @Resource
    SessionUtil sessionUtil;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (sessionUtil.hasPermission(((HttpServletRequest)request).getRequestURI())){
            chain.doFilter(request, response);
        }else{
            throw new AccessDeniedException("");
        }

    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
