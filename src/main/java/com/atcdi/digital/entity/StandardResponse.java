package com.atcdi.digital.entity;


import lombok.Data;

@Data
public class StandardResponse {

    private Integer code;

    private Boolean success;

    private String msg;

    private Object data;


    public static StandardResponse success(String msg){
        StandardResponse res = new StandardResponse();
        res.success = true;
        res.msg = msg;
        res.code = 200;
        return res;
    }


    public static StandardResponse error(int code, String msg){
        StandardResponse res = new StandardResponse();
        res.success = false;
        res.msg = msg;
        res.code = code;
        return res;
    }
}
