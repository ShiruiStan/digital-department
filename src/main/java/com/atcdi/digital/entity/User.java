package com.atcdi.digital.entity;

import com.atcdi.digital.entity.auth.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;


@Data
public class User implements UserDetails {


    private int userId;
    private String username;
    @JsonIgnore
    private String password;
    private String nickname;
    private String phone;
    @ApiModelProperty("账号角色： 均以ROLE_开头，后期做权限控制时使用")
    private Set<Role> roles = new HashSet<>();
    @JsonIgnore
    private int enabled;
    @ApiModelProperty("人员组别： 所长,生产组,研发组,市场组,综合组")
    private DepartmentGroup group;
    @JsonIgnore
    private boolean credentialsNonExpired;
    @JsonIgnore
    private boolean accountNonExpired;
    @JsonIgnore
    private boolean accountNonLocked;


    public enum DepartmentGroup{
        LEADER, // 所长
        PRODUCE, // 生产组
        DEVELOPMENT, // 研发组
        MARKET, // 市场组
        GENERAL, // 综合组
    }


    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));

        }
        return authorities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public boolean isEnabled(){
        return enabled != 0;
    }

    @Override
    public boolean isAccountNonExpired() {
        return enabled != 0;
    }

    @Override
    public boolean isAccountNonLocked() {
        return enabled != 0;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return enabled != 0;
    }


}
