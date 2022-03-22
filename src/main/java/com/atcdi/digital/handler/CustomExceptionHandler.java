package com.atcdi.digital.handler;


import com.atcdi.digital.entity.StandardException;
import com.atcdi.digital.entity.StandardResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.ServletException;


@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(Exception.class)
    public StandardResponse globalRuntimeException(Exception e) {
        String error = e.getClass().getSimpleName() + " : ";
        error += e.getCause() == null ? e.getMessage() : e.getMessage() + " & " + e.getCause().getMessage();
        log.error(error);
        e.printStackTrace();
        return StandardResponse.error(500, e.getMessage());
    }

    @ExceptionHandler({NoHandlerFoundException.class, HttpRequestMethodNotSupportedException.class})
    public StandardResponse pageNotFound(ServletException e) {
        return StandardResponse.error(404, "请求的页面/方法不存在");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public StandardResponse accessDenied(AccessDeniedException e) {
        return StandardResponse.error(403, "无权限访问");
    }


    @ExceptionHandler(StandardException.class)
    public StandardResponse customRuntimeException(StandardException e) {
        return StandardResponse.error(e.code, e.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public StandardResponse missParams(MissingServletRequestParameterException e) {
        return StandardResponse.error(404, "参数不正确");
    }


}
