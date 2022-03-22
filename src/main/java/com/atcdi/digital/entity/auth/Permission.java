package com.atcdi.digital.entity.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;



@Data
public class Permission {
    @JsonIgnore
    private int permissionId;
    @ApiModelProperty("权限名称，具体到接口级")
    private String name;
    @ApiModelProperty("权限对应的url")
    private String url;

}
