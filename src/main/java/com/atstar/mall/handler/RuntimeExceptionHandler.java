package com.atstar.mall.handler;

import com.atstar.mall.exception.UserLoginException;
import com.atstar.mall.vo.ResponseVo;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.atstar.mall.enums.ResponseEnum.ERROR;
import static com.atstar.mall.enums.ResponseEnum.NEED_LOGIN;

/**
 * @Author: Dawn
 * @Date: 2022/3/6 00:52
 */
@ControllerAdvice
public class RuntimeExceptionHandler {

    @ResponseBody
    @ExceptionHandler(RuntimeException.class)
    public ResponseVo handle(RuntimeException e) {

        return ResponseVo.error(ERROR, e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(UserLoginException.class)
    public ResponseVo handle(UserLoginException e) {

        return ResponseVo.error(NEED_LOGIN);
    }
}
