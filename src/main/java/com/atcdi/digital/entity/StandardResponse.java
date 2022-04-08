package com.atcdi.digital.entity;


import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StandardResponse {

    @ApiModelProperty("参照HTTP状态码，解决所有请求返回均为200")
    Integer code;
    @ApiModelProperty("是否成功")
    Boolean success;
    @ApiModelProperty("可用于直接提示的消息")
    String message;
    @ApiModelProperty("具体数据")
    Object result;

    public static  StandardResponse success(){
        StandardResponse res = new StandardResponse();
        res.success = true;
        res.code = 200;
        return res;
    }

    public static  StandardResponse success(String msg){
        StandardResponse res = new StandardResponse();
        res.success = true;
        res.message = msg;
        res.code = 200;
        return res;
    }

    public static  StandardResponse success(String msg, Object data){
        StandardResponse res = new StandardResponse();
        res.success = true;
        res.message = msg;
        res.result = data;
        res.code = 200;
        return res;
    }


    public static  StandardResponse success(Object data){
        StandardResponse res = new StandardResponse();
        res.success = true;
        res.message = "请求成功";
        res.result = data;
        res.code = 200;
        return res;
    }


    public static StandardResponse error(){
        StandardResponse res = new StandardResponse();
        res.success = false;
        res.code = 500;
        return res;
    }

    public static StandardResponse error(int code, String msg){
        StandardResponse res = new StandardResponse();
        res.success = false;
        res.message = msg;
        res.code = code;
        return res;
    }

}
