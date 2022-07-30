package com.study.service;


import com.study.domain.ResponseResult;
import com.study.domain.User;

public interface LoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
