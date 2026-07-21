package com.iy.api.service;

import com.iy.api.model.entity.SysLoginUserEntity;
import com.iy.api.model.security.LoginUser;
import com.iy.api.service.impl.SysLoginUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysLoginUserServiceImpl sysLoginUserService;

    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        SysLoginUserEntity user = sysLoginUserService.getByAccount(account);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new LoginUser(user);
    }
}