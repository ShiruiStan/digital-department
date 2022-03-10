package com.atcdi.digital.utils;

import com.atcdi.digital.entity.Permission;
import com.atcdi.digital.entity.Role;
import com.atcdi.digital.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class SessionUtil {

    @Resource
    private HttpSession session;
    AntPathMatcher antPathMatcher = new AntPathMatcher();

    public void registryUser(User user){
        Set<Role> roleSet = user.getRoles();
        Set<String> roles = roleSet.stream().map(Role::getRoleName).collect(Collectors.toSet());
        session.setAttribute("roles", roles);
        Set<Permission> menuSet = roleSet.stream().map(Role::getMenus).collect(HashSet::new, HashSet::addAll, HashSet::addAll);
        session.setAttribute("menus", menuSet);
        Set<Permission> permissionSet = roleSet.stream().map(Role::getPermissions).collect(HashSet::new, HashSet::addAll, HashSet::addAll);
        Set<String> permissions = permissionSet.stream().map(Permission::getUrl).collect(Collectors.toSet());
        session.setAttribute("permissions", permissions);
    }

    public Set<String> getCurrentUserRoles(){
        return (Set<String>) session.getAttribute("roles");
    }

    public boolean hasPermission(String url){
        Set<String> permissions = (Set<String>) session.getAttribute("permissions");
        for (String permission : permissions){
            if (antPathMatcher.match(permission, url)){
                return true;
            }
        }
        return false;
    }


}
