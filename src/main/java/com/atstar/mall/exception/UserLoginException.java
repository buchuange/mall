package com.atstar.mall.exception;

/**
 * @Author: Dawn
 * @Date: 2022/3/6 21:58
 */
public class UserLoginException extends RuntimeException {

    public UserLoginException() {
    }

    public UserLoginException(String message) {
        super(message);
    }
}
