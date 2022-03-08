package com.atstar.mall.enums;

import lombok.Getter;

/**
 * @Author: Dawn
 * @Date: 2022/3/8 15:09
 */
@Getter
public enum  ProductStatusEnum {

    ON_SALE(1),

    OFF_SALE(2),

    DELETE(3);

    Integer code;

    ProductStatusEnum(Integer code) {
        this.code = code;
    }
}
