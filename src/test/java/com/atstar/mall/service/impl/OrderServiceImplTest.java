package com.atstar.mall.service.impl;

import com.atstar.mall.MallApplicationTests;
import com.atstar.mall.domain.Shipping;
import com.atstar.mall.enums.ResponseEnum;
import com.atstar.mall.service.OrderService;
import com.atstar.mall.vo.OrderVO;
import com.atstar.mall.vo.ResponseVO;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class OrderServiceImplTest extends MallApplicationTests {

    @Autowired
    private OrderService orderService;

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private Integer uid = 4;

    private Integer shippingId = 4;

    private String orderNo = "491f00c756c04a6abfc8ea9d161d09ec";

    @Test
    void saveOrder() {

        ResponseVO<OrderVO> responseVO = orderService.saveOrder(uid, shippingId);

        log.info("result={}", gson.toJson(responseVO));

        Assert.state(ResponseEnum.SUCCESS.getCode().equals(responseVO.getStatus()), "测试失败");
    }

    @Test
    void listOrder() {

        ResponseVO<PageInfo> responseVO = orderService.listOrder(uid, 1, 10);

        log.info("result={}", gson.toJson(responseVO));

        Assert.state(ResponseEnum.SUCCESS.getCode().equals(responseVO.getStatus()), "测试失败");
    }

    @Test
    void getOrder() {

        ResponseVO<OrderVO> responseVO = orderService.getOrder(uid, orderNo);

        log.info("result={}", gson.toJson(responseVO));

        Assert.state(ResponseEnum.SUCCESS.getCode().equals(responseVO.getStatus()), "测试失败");
    }

    @Test
    void cancelOrder() {

        ResponseVO responseVO = orderService.cancelOrder(uid, orderNo);

        log.info("result={}", gson.toJson(responseVO));

        Assert.state(ResponseEnum.SUCCESS.getCode().equals(responseVO.getStatus()), "测试失败");

    }
}