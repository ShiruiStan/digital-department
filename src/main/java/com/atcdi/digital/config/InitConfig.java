package com.atcdi.digital.config;

import com.atcdi.digital.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class InitConfig  implements ApplicationRunner {

    @Resource
    PermissionService permissionService;

    @Override
    public void run(ApplicationArguments args) {
        permissionService.setPermissionRoleMap();
        log.info("权限列表初始化成功");
    }


}
