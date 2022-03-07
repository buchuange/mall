package com.atstar.mall.service.impl;

import com.atstar.mall.domain.User;
import com.atstar.mall.enums.RoleEnum;
import com.atstar.mall.mapper.UserMapper;
import com.atstar.mall.service.UserService;
import com.atstar.mall.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

import static com.atstar.mall.enums.ResponseEnum.*;

/**
 * @Author: Dawn
 * @Date: 2022/3/4 19:36
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Transactional
    @Override
    public ResponseVO<User> register(User user) {


        // username不能重复
        int countByUsername = userMapper.countByUsername(user.getUsername());
        if (countByUsername > 0) {
           return ResponseVO.error(USERNAME_EXIST);
        }

        // email不能重复
        int countByEmail = userMapper.countByEmail(user.getEmail());
        if (countByEmail > 0) {
            return ResponseVO.error(EMAIL_EXIST);
        }

        // MD5摘要算法(Spring自带)
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes(StandardCharsets.UTF_8)));
        user.setRole(RoleEnum.CUSTOMER.getCode());

        // 写入数据库
        int resultCount = userMapper.insertSelective(user);
        if (resultCount != 1) {
            return ResponseVO.error(ERROR);
        }

        return ResponseVO.successByMsg("注册成功");
    }


    @Override
    public ResponseVO<User> login(String username, String password) {

        User user = userMapper.selectByUsername(username);

        if (user == null) {
            // 用户名不存在（返回用户名或密码错误）
            return ResponseVO.error(USERNAME_OR_PASSWORD_ERROR);
        }

        if (!user.getPassword().equalsIgnoreCase(
                DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8)))) {
            // 密码错误（返回用户名或密码错误）
            return ResponseVO.error(USERNAME_OR_PASSWORD_ERROR);
        }


        user.setPassword(null);
        return ResponseVO.success(user);
    }
}
