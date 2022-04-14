package com.atcdi.digital.service;

import com.atcdi.digital.dao.UserDao;
import com.atcdi.digital.entity.StandardException;
import com.atcdi.digital.entity.User;
import com.atcdi.digital.entity.auth.Menu;
import com.atcdi.digital.entity.auth.Permission;
import com.atcdi.digital.entity.auth.Role;
import com.atcdi.digital.handler.SessionHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import springfox.documentation.spring.web.json.Json;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


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

    public Set<String> getUserRoleNames(){
        return getCurrentUser().getRoles().stream().map(Role::getRoleName).collect(Collectors.toSet());
    }

    public Set<JsonNode> getUserMenus(){
        Set<Menu> menus = new HashSet<>();
        for (Role role : getCurrentUser().getRoles()){
            if ("ROLE_ADMIN".equals(role.getRoleName())){
                menus.addAll(permissionService.getAllMenus());
                break;
            }
            else {
                menus.addAll(permissionService.getMenuFromRole(role));
            }
        }
        Set<JsonNode> res = new HashSet<>();
        for (Menu menu : menus){
            if (menu.getMenuPid() == 0){
                res.add(packageMenu(menu, menus));
            }
        }
        return res;
    }

    // TODO
    public JsonNode packageMenu(Menu menu, Set<Menu> menus){
        ObjectNode node = mapper.createObjectNode();
        node.put("name", menu.getName());
        node.put("path", menu.getPath());
        node.put("component", menu.getComponent());
        ObjectNode meta = mapper.createObjectNode();
        meta.put("title", menu.getTitle());
        meta.put("orderNo", menu.getOrderNo());
        if (menu.getIcon() != null) meta.put("icon", menu.getIcon());
        if (menu.isMerge()) meta.put("hideMenu", true);
        Set<Menu> children = menus.stream().filter(m -> m.getMenuPid() == menu.getMenuId()).collect(Collectors.toSet());
        if (children.size() != 0){
            Set<JsonNode> childMenus = new HashSet<>();
            for (Menu child : children){
                JsonNode childNode = packageMenu(child, menus);
                JsonNode hide = childNode.get("meta").get("hideMenu");
                if (hide != null && hide.asBoolean()) {
                    meta.put("hideChildrenInMenu", true);
                    node.put("redirect", menu.getPath() + "/" + childNode.get("path").asText());
                }
                childMenus.add(childNode);
            }
            node.set("children", mapper.valueToTree(childMenus));

        }
        node.set("meta", meta);
        return node;
    }

    public Set<Permission> getPermissions(){
        Set<Permission> permissions = new HashSet<>();
        for (Role role : getCurrentUser().getRoles()){
            if ("ROLE_ADMIN".equals(role.getRoleName())){
                permissions.addAll(permissionService.getAllPermissions());
                break;
            }
            else {
                permissions.addAll(permissionService.getPermissionFromRole(role));
            }
        }
        return permissions;
    }


    public String getUserNickNameById(int userId){
        return userDao.getUserNickNameById(userId);
    }


    @Deprecated
    public boolean updateUser(User user){
//        userDao.updateUser()
        return true;
    }



    public void modifyUserPassword(String password){
        if (password.matches("[a-zA-Z0-9]+")){
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            userDao.setUserPassword(sessionHandler.getCurrentUser().getUserId(), encoder.encode(password));
        }else{
            throw new StandardException(403, "密码重置失败，请重新输入！");
        }
    }


    public User getUserById(int userId){
        return userDao.getUserById(userId);
    }

    public Set<User> getUserList(){
        return userDao.getAllUsers();
    }



}
