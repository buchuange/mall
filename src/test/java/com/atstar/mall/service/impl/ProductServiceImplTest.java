package com.atstar.mall.service.impl;

import com.atstar.mall.MallApplicationTests;
import com.atstar.mall.enums.ResponseEnum;
import com.atstar.mall.service.ProductService;
import com.atstar.mall.vo.ProductDetailVO;
import com.atstar.mall.vo.ProductVO;
import com.atstar.mall.vo.ResponseVO;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.List;


@Slf4j
public class ProductServiceImplTest extends MallApplicationTests {


    @Autowired
    private ProductService productService;

    @Test
    public void listProducts() {
        ResponseVO<PageInfo> listProducts = productService.listProducts(100002, 1, 2);

        log.info("vo={}", listProducts);
        Assert.state(ResponseEnum.SUCCESS.getCode().equals(listProducts.getStatus()), "测试失败");

    }

    @Test
    public void getProductById() {

        ResponseVO<ProductDetailVO> responseVO = productService.getProductById(26);
        Assert.state(ResponseEnum.SUCCESS.getCode().equals(responseVO.getStatus()), "测试失败");

    }
}