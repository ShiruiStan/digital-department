package com.atcdi.digital.hanlder;


import com.atcdi.digital.entity.StandardException;
import com.atcdi.digital.entity.StandardResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;


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

    @ExceptionHandler(NoHandlerFoundException.class)
    public StandardResponse pageNotFound(NoHandlerFoundException e) {
        return StandardResponse.error(404, "请求的页面/方法不存在");
    }

//    @ExceptionHandler(HttpClientErrorException.NotFound.class)
//    public StandardResponse pageNotFound(StandardException e) {
//        System.out.println(11);
//        return StandardResponse.error(404, e.getMessage());
//    }


    @ExceptionHandler(StandardException.class)
    public StandardResponse customRuntimeException(StandardException e) {
        return StandardResponse.error(e.code, e.getMessage());
    }

}
