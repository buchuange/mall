package com.atstar.mall.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: Dawn
 * @Date: 2022/3/8 22:17
 */
@Data
public class CartVO {

    private List<CartProductVO> cartProductVOList;

    private Boolean selectedAll;

    private BigDecimal cartTotalPrice;

    private Integer cartTotalQuantity;
}
