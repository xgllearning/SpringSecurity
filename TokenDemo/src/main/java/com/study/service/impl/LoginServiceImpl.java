package com.study.service.impl;

import com.study.domain.LoginUser;
import com.study.domain.ResponseResult;
import com.study.domain.User;
import com.study.service.LoginService;
import com.study.utils.JwtUtil;
import com.study.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class LoginServiceImpl implements LoginService {
    //从容器中获取AuthenticationManager authenticate进行用户认证
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {

        //调用authenticationManager的认证方法,需要传入authentication对象==>UsernamePasswordAuthenticationToken
        //TODO：authenticationToken为封装的Authentication对象，参数一(认证主体，传入用户名),参数二(传入的密码)
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        //调用AuthenticationManager的authenticate方法进行验证，//TODO:最终会调用到UserDetailsServiceImpl中的方法去进行用户校验，最终返回Authentication对象
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        System.out.println(authenticate);
        //如果认证没通过，给出对应的提示，没通过为null
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("登录失败");
        }
        //如果认证通过了，使用userid生成一个jwt，jwt存入ResponseResult返回(authenticate中包含用户所有属性)
        //强转为loginUser对象，里面含有user对象
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();//为long类型，需要转为字符串
        String jwt = JwtUtil.createJWT(userId);
        Map<String, String> map = new HashMap<>();
        map.put("token", jwt);
        //把完整的用户信息存入redis，userid作为key
        redisCache.setCacheObject("login" + userId, loginUser);
        return new ResponseResult(200, "登录成功", map);
    }


    @Override
    public ResponseResult logout() {
        //获取SecurityContextHolder中的用户id
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userid = loginUser.getUser().getId();
        //删除redis中的值
        redisCache.deleteObject("login" + userid);
        return new ResponseResult(200, "退出成功");
    }
}
