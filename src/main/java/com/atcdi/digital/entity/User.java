package com.atcdi.digital.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;


@Data
public class User implements UserDetails {


    private int userId;
    private String username;
    private String password;
    private String nickname;
    private String phone;
    private Set<Role> roles = new HashSet<>();
    private int enabled;


    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
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
