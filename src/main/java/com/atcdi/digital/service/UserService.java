package com.atcdi.digital.service;

import com.atcdi.digital.dao.UserDao;
import com.atcdi.digital.entity.User;
import com.atcdi.digital.entity.auth.Menu;
import com.atcdi.digital.entity.auth.Role;
import com.atcdi.digital.handler.SessionHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;


@Service
public class UserService implements UserDetailsService {
    @Resource
    UserDao userDao;
    @Resource
    SessionHandler sessionHandler;
    @Resource
    PermissionService permissionService;
    @Resource
    ObjectMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =  userDao.getUserByName(username);
        if (user == null ){
            throw new UsernameNotFoundException("");
        }else if (!user.isEnabled()) {
            throw new LockedException("");
        }
        return user;
    }

    public void userLogin(User user){
        User loginUser = userDao.getUserByName(user.getUsername());
        sessionHandler.registryUser(loginUser);
    }

    public void userLogout(){
        sessionHandler.freeUser();
    }


    public User getCurrentUser(){
        return sessionHandler.getCurrentUser();
    }

    public Set<String> getUserRoles(){
        return sessionHandler.getCurrentRoles();
    }

    public Set<Menu> getUserMenus(){
        Set<Role> roles = sessionHandler.getCurrentUser().getRoles();
        Set<Menu> menus = new HashSet<>();
        for (Role role : roles){
            menus.addAll(permissionService.getMenuFromRole(role));
        }
        return menus;
    }


    public String getUserNickNameById(int userId){
        return userDao.getUserNickNameById(userId);
    }


    @Deprecated
    public boolean updateUser(User user){
        //userDao.updateUser()
        return true;
    }

    public boolean resetUserPassword(){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return userDao.setUserPassword(sessionHandler.getCurrentUser().getUserId(), encoder.encode("123456"));
    }


    public ObjectNode getUserDisplayLabel(int userId){
        User user = userDao.getUserById(userId);
        ObjectNode res = mapper.createObjectNode();
        res.put("userId", user.getUserId());
        res.put("username", user.getUsername());
        if (user.getNickname() != null) res.put("nickname", user.getNickname());
        if (user.getPhone() != null) res.put("phone", user.getPhone());
        if (user.getGroup() != null) res.put("group", user.getGroup().toString());
        return res;
    }


}
