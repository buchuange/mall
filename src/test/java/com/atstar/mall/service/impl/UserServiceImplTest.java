package com.atstar.mall.service.impl;

import com.atstar.mall.MallApplicationTests;
import com.atstar.mall.domain.User;
import com.atstar.mall.enums.ResponseEnum;
import com.atstar.mall.enums.RoleEnum;
import com.atstar.mall.service.UserService;
import com.atstar.mall.vo.ResponseVO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;


@Transactional
public class UserServiceImplTest extends MallApplicationTests {

    private static final String USERNAME = "Jack";
    private static final String PASSWORD = "123456";


    @Autowired
    private UserService userService;

    @BeforeEach
    public void register() {
        User user = new User(USERNAME, PASSWORD, "Jac445k@qq.com", RoleEnum.CUSTOMER.getCode());
        userService.register(user);
    }

    @Test
    public void login() {
        ResponseVO<User> responseVo = userService.login(USERNAME, PASSWORD);
        Assert.state(ResponseEnum.SUCCESS.getCode().equals(responseVo.getStatus()), "登录失败");
    }
}