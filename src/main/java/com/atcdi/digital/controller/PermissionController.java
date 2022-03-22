package com.atcdi.digital.controller;


import com.atcdi.digital.entity.StandardResponse;
import com.atcdi.digital.service.PermissionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/permission")
@Api(tags  = "权限相关接口")
public class PermissionController {
    @Resource
    PermissionService permissionService;
    @Resource
    ObjectMapper mapper;

    @GetMapping("/permissions")
    public StandardResponse getRolePermissionMap(){
        return StandardResponse.success(permissionService.getRolePermissionMap());
    }
}
