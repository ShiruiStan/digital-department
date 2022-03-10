package com.atcdi.digital.service;

import com.atcdi.digital.dao.UserDao;
import com.atcdi.digital.entity.User;
import com.atcdi.digital.utils.SessionUtil;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Set;


@Service
public class UserService implements UserDetailsService {
    @Resource
    UserDao userDao;
    @Resource
    SessionUtil sessionUtil;

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
        User loginUser = userDao.getUserById(user.getUserId());
        sessionUtil.registryUser(loginUser);
    }


    public Set<String> getCurrentUserRoles(){
        return sessionUtil.getCurrentUserRoles();
    }



}
