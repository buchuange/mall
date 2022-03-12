package com.atstar.mall.service.impl;

import com.atstar.mall.MallApplicationTests;
import com.atstar.mall.enums.ResponseEnum;
import com.atstar.mall.service.ProductService;
import com.atstar.mall.vo.ProductDetailVO;
import com.atstar.mall.vo.ResponseVO;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;


@Slf4j
@Transactional
public class ProductServiceImplTest extends MallApplicationTests {

    @Autowired
    private ProductService productService;

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private Integer categoryId = 100002;

    private Integer productId = 26;

    @Test
    public void listProducts() {
        ResponseVO<PageInfo> listProducts = productService.listProducts(categoryId, 1, 2);

        log.info("vo={}", gson.toJson(listProducts));
        Assert.state(ResponseEnum.SUCCESS.getCode().equals(listProducts.getStatus()), "测试失败");

    }

    @Test
    public void getProductById() {

        ResponseVO<ProductDetailVO> responseVO = productService.getProductById(productId);
        Assert.state(ResponseEnum.SUCCESS.getCode().equals(responseVO.getStatus()), "测试失败");

    }
}