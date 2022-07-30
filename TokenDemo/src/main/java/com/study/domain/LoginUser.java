package com.study.domain;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class LoginUser implements UserDetails {
    //封装对象信息
    private User user;

    //存储权限信息
    private List<String> permissions;
    //不存入redis中，进行忽略
    @JSONField(serialize = false)
    private List<SimpleGrantedAuthority> authorities;

    //getAuthorities获取权限信息的
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (authorities!=null){
            return authorities;
        }
        //方法一
//        newList = new ArrayList<>();
//        //把permission中的String类型的权限信息封装成(SimpleGrantedAuthority implements GrantedAuthority)对象
//        for (String permission : permissions) {
//            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(permission);//拿到了GrantedAuthority对象，符合集合泛型要求
//            newLest.add(authority);
//        }
//        return newList;
        //方法二：stream流,涉及到元素类型转换，使用map返回新的元素类型，使用方法构造器引用类名permission -> new SimpleGrantedAuthority(permission)
        //把permissions中字符串类型的权限信息转换成GrantedAuthority对象存入authorities中
        //TODO:优化，此时会每次调用都会进行转换，优化为只有第一次转换，以后再调用可以把之前转换好的直接返回，即将newList定义为成员变量
        authorities = permissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public LoginUser(User user, List<String> permissions) {
        this.user = user;
        this.permissions = permissions;
    }
}
