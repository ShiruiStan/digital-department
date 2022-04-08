package com.atcdi.digital.entity.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Set;

@Data
public class Menu {
    int menuId;
    int menuPid;
    @ApiModelProperty("菜单名称")
    String name;
    @ApiModelProperty("菜单显示名称")
    String title;
    @ApiModelProperty("对应的组件")
    String component;
    @ApiModelProperty("对应的路由")
    String path;
    @ApiModelProperty("图标")
    String icon;
    @ApiModelProperty("排序")
    int orderNo = 1;
    @ApiModelProperty("菜单内的权限，不会动态发生变化")
    Set<Permission> permissions;
    @JsonIgnore
    boolean merge = false;

}
