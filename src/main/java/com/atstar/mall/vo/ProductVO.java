package com.atstar.mall.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: Dawn
 * @Date: 2022/3/8 12:34
 */
@Data
public class ProductVO {

    private Integer id;

    private Integer categoryId;

    private String name;

    private String subtitle;

    private String mainImage;

    private Integer status;

    private BigDecimal price;
}
