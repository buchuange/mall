package com.atstar.mall.handler;

import com.atstar.mall.exception.UserLoginException;
import com.atstar.mall.vo.ResponseVO;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Objects;

import static com.atstar.mall.enums.ResponseEnum.*;

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
    public ResponseVO handle() {

        return ResponseVO.error(NEED_LOGIN);
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseVO notValidExceptionHandle(MethodArgumentNotValidException e) {

        BindingResult bindingResult = e.getBindingResult();
        return ResponseVO.error(PARAM_ERROR,
                Objects.requireNonNull(bindingResult.getFieldError()).getField() + " " +
                bindingResult.getFieldError().getDefaultMessage());
    }

}
