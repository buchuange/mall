package com.atstar.mall.service;

import com.atstar.mall.domain.User;
import com.atstar.mall.vo.ResponseVo;

/**
 * @Author: Dawn
 * @Date: 2022/3/4 19:30
 */
public interface IUserService {


    /**
     * 注册
     */
    ResponseVo<User> register(User user);

    /**
     * 登录
     */
    ResponseVo<User> login(String username, String password);
}
