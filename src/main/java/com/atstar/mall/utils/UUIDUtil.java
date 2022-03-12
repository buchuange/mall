package com.atstar.mall.utils;

import java.util.UUID;

/**
 * @Author: Dawn
 * @Date: 2022/3/12 13:14
 */
public class UUIDUtil {

    public static String getUUID(){

        return UUID.randomUUID().toString().replaceAll("-","");

    }
}
