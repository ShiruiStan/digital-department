package com.atcdi.digital.entity.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Set;

@Data
public class Role {
    @JsonIgnore
    private int roleId;
    @ApiModelProperty("角色名称，英文")
    private String roleName;
    @ApiModelProperty("角色名称，中文用于显示")
    private String label;
    @ApiModelProperty("角色拥有的菜单，均为二级菜单，根据一级菜单进行分组")
    private Set<Menu> menus;
    @ApiModelProperty("角色拥有的权限")
    private Set<Permission> permissions;


}
