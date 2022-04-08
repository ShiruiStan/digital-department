package com.atcdi.digital.entity.auth;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.http.HttpMethod;


@Data
public class Permission {
    int permissionId;
    @ApiModelProperty("权限code")
    String code;
    @ApiModelProperty("权限名称")
    String name;
    @ApiModelProperty("请求方法")
    HttpMethod method = HttpMethod.GET;
    @ApiModelProperty("请求url")
    String url;

}
