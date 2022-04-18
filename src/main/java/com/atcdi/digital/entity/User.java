package com.atcdi.digital.entity;

import com.atcdi.digital.entity.auth.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;


@Data
public class User implements UserDetails {


    int userId;
    String username;
    @JsonIgnore
    String password;
    String nickname;
    String phone;
    @ApiModelProperty("头像")
    String avatar;
    @ApiModelProperty("账号角色： 均以ROLE_开头，后期做权限控制时使用")
    Set<Role> roles = new HashSet<>();
    @JsonIgnore
    boolean enabled;
    @ApiModelProperty("人员组别： 所长,生产组,研发组,市场组,综合组")
    DepartmentGroup group;
    @JsonIgnore
    boolean credentialsNonExpired;
    @JsonIgnore
    boolean accountNonExpired;
    @JsonIgnore
    boolean accountNonLocked;


    public enum DepartmentGroup{
        ADMIN, // 管理员
        MASTER, // 所长
        PRODUCE, // 生产组
        DEVELOPMENT, // 研发组
        MARKET, // 市场组
        GENERAL, // 综合组
    }


    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));

        }
        return authorities;
    }



}
