package com.atstar.mall.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author: Dawn
 * @Date: 2022/3/5 21:09
 */
@Data
public class UserLoginForm {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
