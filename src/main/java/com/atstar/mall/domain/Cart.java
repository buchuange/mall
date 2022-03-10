package com.atstar.mall.domain;

import lombok.Data;

/**
 * @Author: Dawn
 * @Date: 2022/3/9 11:32
 */
@Data
public class Cart {

    private Integer productId;

    private Integer quantity;

    private Boolean productSelected;

    public Cart() {
    }

    public Cart(Integer productId, Integer quantity, Boolean productSelected) {
        this.productId = productId;
        this.quantity = quantity;
        this.productSelected = productSelected;
    }
}
