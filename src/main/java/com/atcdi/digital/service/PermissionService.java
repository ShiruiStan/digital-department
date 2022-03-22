package com.atcdi.digital.service;

import com.atcdi.digital.dao.PermissionDao;
import com.atcdi.digital.entity.auth.Menu;
import com.atcdi.digital.entity.auth.Permission;
import com.atcdi.digital.entity.auth.Role;
import com.atcdi.digital.handler.SessionHandler;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Service
public class PermissionService {
    @Resource
    PermissionDao permissionDao;
    @Resource
    SessionHandler sessionHandler;

    public void setPermissionRoleMap(){
        Set<Role> roles = permissionDao.getRolePermissionMap();
        HashMap<String, Set<String>> permissionMap = new HashMap<>();
        for (Role role : roles){
            for (Permission permission : role.getPermissions()){
                if (!permissionMap.containsKey(permission.getUrl())){
                    permissionMap.put(permission.getUrl(), new HashSet<>());
                }
                permissionMap.get(permission.getUrl()).add(role.getRoleName());
            }
        }
        sessionHandler.setPermissionMap(permissionMap);
    }

    public Set<Menu> getMenuFromRole(Role role){
        return permissionDao.findMenusByRoleId(role.getRoleId());
    }

    public Set<Role> getRolePermissionMap(){
        return permissionDao.getRolePermissionMap();
    }


}
