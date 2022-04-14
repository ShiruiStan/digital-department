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
    public HashMap<String, Set<String>> permissionMap = new HashMap<>();

    public void setPermissionMap(HashMap<String, Set<String>> permissionMap){
        this.permissionMap = permissionMap;
    }


    public void registryUser(User user){
        session.setAttribute("user", user);
        session.setAttribute("roles", user.getRoles().stream().map(Role::getRoleName).collect(Collectors.toSet()));
    }

    public void freeUser(){
        session.removeAttribute("user");
    }

    public User getCurrentUser(){
        return (User) session.getAttribute("user");
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

    public boolean hasRole(String roleName){
        Set<String> roles =  (Set<String>) session.getAttribute("roles");
        return roles != null && roles.contains(roleName);
    }


    public boolean leaderOf(User other){
        return getCurrentUser().getGroup().equals(other.getGroup()) && hasRole("ROLE_LEADER");
    }
}
