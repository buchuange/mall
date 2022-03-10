package com.atstar.mall.service.impl;

import com.atstar.mall.MallApplicationTests;
import com.atstar.mall.enums.ResponseEnum;
import com.atstar.mall.form.CartAddForm;
import com.atstar.mall.form.CartUpdateForm;
import com.atstar.mall.service.CartService;
import com.atstar.mall.vo.CartVO;
import com.atstar.mall.vo.ResponseVO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class CartServiceImplTest extends MallApplicationTests {

    @Autowired
    private CartService cartService;

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Test
    void saveCart() {

        CartAddForm cartAddForm = new CartAddForm();
        cartAddForm.setProductId(29);
        cartAddForm.setSelected(true);

        ResponseVO<CartVO> listCarts= cartService.saveCart(1, cartAddForm);
        log.info("result={}", gson.toJson(listCarts));

        Assert.state(ResponseEnum.SUCCESS.getCode().equals(listCarts.getStatus()), "测试失败");
    }

    @Test
    void listCarts() {
        ResponseVO<CartVO> listCarts = cartService.listCarts(1);

        log.info("result={}", gson.toJson(listCarts));
        Assert.state(ResponseEnum.SUCCESS.getCode().equals(listCarts.getStatus()), "测试失败");

    }

    @Test
    void updateCart() {

        CartUpdateForm cartUpdateForm = new CartUpdateForm();
        cartUpdateForm.setQuantity(10);
        cartUpdateForm.setSelected(false);

        ResponseVO<CartVO> listCarts= cartService.updateCart(1, 29, cartUpdateForm);
        log.info("result={}", gson.toJson(listCarts));

        Assert.state(ResponseEnum.SUCCESS.getCode().equals(listCarts.getStatus()), "测试失败");
    }

    @Test
    void deleteCart() {

        ResponseVO<CartVO> listCarts = cartService.deleteCart(1, 29);
        log.info("result={}", gson.toJson(listCarts));

        Assert.state(ResponseEnum.SUCCESS.getCode().equals(listCarts.getStatus()), "测试失败");
    }

    @Test
    void selectAll() {

        ResponseVO<CartVO> listCarts = cartService.selectAll(1);
        log.info("result={}", gson.toJson(listCarts));

        Assert.state(ResponseEnum.SUCCESS.getCode().equals(listCarts.getStatus()), "测试失败");
    }

    @Test
    void unSelectAll() {

        ResponseVO<CartVO> listCarts = cartService.unSelectAll(1);
        log.info("result={}", gson.toJson(listCarts));

        Assert.state(ResponseEnum.SUCCESS.getCode().equals(listCarts.getStatus()), "测试失败");
    }

    @Test
    void countCart() {

        ResponseVO<Integer> countCart = cartService.countCart(1);

        log.info("result={}", gson.toJson(countCart));

        Assert.state(ResponseEnum.SUCCESS.getCode().equals(countCart.getStatus()), "测试失败");
    }
}