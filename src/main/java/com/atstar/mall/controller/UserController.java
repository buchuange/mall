package com.atstar.mall.controller;

import com.atstar.mall.domain.User;
import com.atstar.mall.form.UserLoginForm;
import com.atstar.mall.form.UserRegisterForm;
import com.atstar.mall.service.IUserService;
import com.atstar.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.util.Objects;

import static com.atstar.mall.consts.MallConst.CURRENT_USER;
import static com.atstar.mall.enums.ResponseEnum.NEED_LOGIN;
import static com.atstar.mall.enums.ResponseEnum.PARAM_ERROR;

/**
 * @Author: Dawn
 * @Date: 2022/3/5 18:30
 */
@Slf4j
@RestController
public class UserController {

    @Autowired
    private IUserService userService;

    @PostMapping("/user/register")
    public ResponseVo register(@Valid @RequestBody UserRegisterForm userRegisterForm,
                               BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            log.info("注册提交的参数有误：{} {}",
                    Objects.requireNonNull(bindingResult.getFieldError()).getField(),
                    bindingResult.getFieldError().getDefaultMessage());

            return ResponseVo.error(PARAM_ERROR, bindingResult);
        }

        User user = new User();
        BeanUtils.copyProperties(userRegisterForm , user);


        return userService.register(user);
    }


    @PostMapping("/user/login")
    public ResponseVo<User> login(@Valid @RequestBody UserLoginForm userLoginForm,
                                  BindingResult bindingResult, HttpSession session) {

        if (bindingResult.hasErrors()) {
            return ResponseVo.error(PARAM_ERROR, bindingResult);
        }

        ResponseVo<User> userResponseVo = userService.login(userLoginForm.getUsername(), userLoginForm.getPassword());

        session.setAttribute(CURRENT_USER, userResponseVo.getData());

        return userResponseVo;
    }

    // session保存在内存中，改进版 token+redis
    @GetMapping("/user")
    public ResponseVo<User> userInfo(HttpSession session) {

        log.info("/user sessionId={}", session.getId());
        User user = (User) session.getAttribute(CURRENT_USER);

        return ResponseVo.success(user);
    }

    //TODO 判断登录状态，拦截器

    /**
     *{@link org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory} getSessionTimeoutInMinutes
     */
    @PostMapping("/user/logout")
    public ResponseVo logout(HttpSession session) {

        log.info("/user/logout sessionId={}", session.getId());
        session.removeAttribute(CURRENT_USER);

        return ResponseVo.successByMsg("退出成功");
    }
}
