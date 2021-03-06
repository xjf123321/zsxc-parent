package com.zs.create.modules.shiro.vo;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * @Description UserBean
 * @Author HeLiu
 * @Date 2019/9/28 9:23
 **/
@Data
public class UserBean {
    private String username;
    private String password;
    //用户所有角色值，用于shiro做角色权限的判断
    private Set<String> roles = new HashSet<>();
    //用户所有权限值，用于shiro做资源权限的判断
    private Set<String> perms = new HashSet<>();
}
