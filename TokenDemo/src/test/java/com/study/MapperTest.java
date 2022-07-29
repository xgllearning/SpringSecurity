package com.study;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.study.domain.User;

import com.study.mapper.UserMapper;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootTest
public class MapperTest {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Test
    public void TestBCryptPasswordEncoder(){
        //BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String encode = passwordEncoder.encode("123456");//加密：CharSequence rawPassword
        String encode2 = passwordEncoder.encode("123456");//加密：CharSequence rawPassword
        System.out.println(encode);//"$2a$10$S.1VjhJVe3m8noKrDp8TmuqvWZ5eU0/wqHWwecMujVLK1Ki6aRwm6"
        System.out.println(encode2);//"$2a$10$GeY5VdSuWYFnBUDi6WZ89ulIbN5B5G3BAaVKbF2fDQ8tEuZNeAMMG"
        //每次都会不一样，原因是加盐导致
        //明文加密后和数据库进行比较：CharSequence rawPassword, String encodedPassword
        boolean result = passwordEncoder.matches("123456",
                "$2a$10$S.1VjhJVe3m8noKrDp8TmuqvWZ5eU0/wqHWwecMujVLK1Ki6aRwm6");
        System.out.println(result);//true
    }

    @Test
    public void testUserMapper(){
        List<User> users = userMapper.selectList(null);
        System.out.println(users);
    }
}