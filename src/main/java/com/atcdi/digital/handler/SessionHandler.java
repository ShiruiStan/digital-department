package com.atcdi.digital.handler;


import com.atcdi.digital.entity.auth.Role;
import com.atcdi.digital.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class SessionHandler {

    @Resource
    HttpSession session;

    AntPathMatcher antPathMatcher = new AntPathMatcher();
    HashMap<String, Set<String>> permissionMap = new HashMap<>();

    public void setPermissionMap(HashMap<String, Set<String>> permissionMap){
        this.permissionMap = permissionMap;
    }


    public void registryUser(User user){
        session.setAttribute("user", user);
        Set<String> roles = user.getRoles().stream().map(Role::getRoleName).collect(Collectors.toSet());
        session.setAttribute("roles", roles);
    }

    public void freeUser(){
        session.removeAttribute("user");
        session.removeAttribute("roles");
    }

    public User getCurrentUser(){
        return (User) session.getAttribute("user");
    }

    public Set<String> getCurrentRoles(){
        return (Set<String>) session.getAttribute("roles");
    }



    public boolean hasPermission(String url){
        Set<String> needRoles = new HashSet<>();
        for (Map.Entry<String, Set<String>> entry : permissionMap.entrySet()){
            if (antPathMatcher.match(entry.getKey(), url)) {
                needRoles.addAll(entry.getValue());
            }
        }
        if (needRoles.size() == 0){
            return true;
        }else{
            Set<String> roles = (Set<String>) session.getAttribute("roles");
            if (roles == null) return false;
            else if (roles.contains("ROLE_ADMIN")) return true;
            else {
                needRoles.retainAll(roles);
                return needRoles.size() > 0;
            }
        }

    }
}
