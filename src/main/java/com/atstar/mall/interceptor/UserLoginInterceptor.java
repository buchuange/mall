package com.atstar.mall.interceptor;

import com.atstar.mall.domain.User;
import com.atstar.mall.exception.UserLoginException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.atstar.mall.consts.MallConst.CURRENT_USER;

/**
 * @Author: Dawn
 * @Date: 2022/3/6 21:24
 */
@Slf4j
public class UserLoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        log.info("preHandle...");

        User user = (User) request.getSession().getAttribute(CURRENT_USER);
        if (ObjectUtils.isEmpty(user)) {
            log.info("user = null");
            throw new UserLoginException();
        }

        return true;
    }
}
