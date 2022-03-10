package com.atcdi.digital.entity;

import lombok.Data;



@Data
public class Permission {
    private int permissionId;
    private int permissionPId;
    private String name;
    private String url;
    private int permType; // 0代表目录，1代表权限

}
