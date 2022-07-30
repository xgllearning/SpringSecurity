package com.study.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.study.domain.LoginUser;
import com.study.domain.User;
import com.study.mapper.MenuMapper;
import com.study.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MenuMapper menuMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户信息--通过mybatis-plus查询数据库
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(username),User::getUserName,username);
        User user = userMapper.selectOne(queryWrapper);
        //如果查询不到数据就通过抛出异常来给出提示
        if (Objects.isNull(user)){
            throw new RuntimeException("用户名或者密码不正确");
        }
        //TODO 根据用户查询权限信息 添加到LoginUser中
//        ArrayList<String> list = new ArrayList<>(Arrays.asList("test","admin"));改进！
        List<String> permissionKeyList = menuMapper.selectPermsByUserId(user.getId());
        //扩充LoginUser的属性(成员变量)-->目的是将list封装进去
        //封装成UserDetails对象返回,因为LoginUser实现了UserDetails，所以可以返回
        return  new LoginUser(user,permissionKeyList);
    }
}