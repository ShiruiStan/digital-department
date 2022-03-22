package com.atcdi.digital.entity;


import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StandardResponse {

    @ApiModelProperty("参照HTTP状态码，解决所有请求返回均为200")
    private Integer code;
    @ApiModelProperty("是否成功")
    private Boolean success;
    @ApiModelProperty("可用于直接提示的消息")
    private String message;
    @ApiModelProperty("具体数据")
    private Object result;


    public static  StandardResponse success(String msg){
        StandardResponse res = new StandardResponse();
        res.success = true;
        res.message = msg;
        res.code = 200;
        return res;
    }

    public static  StandardResponse success(JsonNode json){
        StandardResponse res = new StandardResponse();
        res.success = true;
        res.message = "请求成功";
        res.result = json;
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



    public static StandardResponse error(int code, String msg){
        StandardResponse res = new StandardResponse();
        res.success = false;
        res.message = msg;
        res.code = code;
        return res;
    }

}
