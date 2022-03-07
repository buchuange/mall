package com.atstar.mall.handler;

import com.atstar.mall.exception.UserLoginException;
import com.atstar.mall.vo.ResponseVO;
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
    public ResponseVO handle(RuntimeException e) {

        return ResponseVO.error(ERROR, e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(UserLoginException.class)
    public ResponseVO handle(UserLoginException e) {

        return ResponseVO.error(NEED_LOGIN);
    }
}
