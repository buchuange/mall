package com.atstar.mall.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: Dawn
 * @Date: 2022/3/8 22:21
 */
@Data
public class CartProductVO {

    private Integer productId;

    /**
     * 购买的数量
     */
    private Integer quantity;

    private String productName;

    private String productSubtitle;

    private String productMainImage;

    private BigDecimal productPrice;

    private Integer productStatus;

    /**
     * 商品总价：quantity * productPrice
     */
    private BigDecimal productTotalPrice;

    private Integer productStock;

    /**
     * 是否选中
     */
    private Boolean productSelected;
}
