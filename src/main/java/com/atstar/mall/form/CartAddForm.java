package com.atstar.mall.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 添加商品
 * @Author: Dawn
 * @Date: 2022/3/8 22:28
 */
@Data
public class CartAddForm {

    @NotNull
    private Integer productId;

    private Boolean selected;

}
