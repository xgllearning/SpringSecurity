package com.study.expression;

import com.study.domain.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("ex")
public class ExpressionRoot {

    public boolean hasAuthority(String authority){
        //SecurityContextHolder获取当前用户的权限
        //TODO：用户信息以及权限信息被封装进authenticationToken并存放进中SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        List<String> permissions = loginUser.getPermissions();
        //判断用户权限集合中是否存在authority
        return permissions.contains(authority);
    }
}