package com.atstar.mall.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: Dawn
 * @Date: 2022/3/12 18:43
 */
@Data
public class OrderCreateForm {

    @NotNull
    private Integer shippingId;
}
