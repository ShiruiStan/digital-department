package com.atcdi.digital.handler;


import com.atcdi.digital.entity.StandardException;
import com.atcdi.digital.entity.auth.Role;
import com.atcdi.digital.entity.User;
import org.springframework.session.MapSession;
import org.springframework.session.SessionRepository;
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
    @Resource
    SessionRepository<MapSession> sessionRepository;

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

    public boolean checkUserDownload(String token){
        MapSession loginSession = sessionRepository.findById(token);
        if (loginSession == null){
            throw new StandardException(401, "token已失效，请重新登录");
        }else{
            User user = loginSession.getAttribute("user");
            if (user == null){
                throw new StandardException(403, "token已失效，请重新登录");
            }else{
//                registryUser(user);
                // TODO 确定下载权限
                sessionRepository.deleteById(session.getId());
                return true;
            }
        }
    }
}
