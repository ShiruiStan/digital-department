package com.atcdi.digital.entity;

import lombok.Data;

import java.util.Set;

@Data
public class Role {
    private int roleId;
    private String roleName;
    private String label;
    private Set<Permission> permissions;
    private Set<Permission> menus;
}
