package com.atcdi.digital.entity.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Set;

@Data
public class Menu {
    @JsonIgnore
    int menuId;
    @ApiModelProperty("一级菜单名称")
    String menuGroup;
    @ApiModelProperty("二级菜单名称")
    String menuName;
    @ApiModelProperty("二级菜单对应的vue模板名称")
    String vueName;
    @JsonIgnore
    Set<Permission> permissions;
}
