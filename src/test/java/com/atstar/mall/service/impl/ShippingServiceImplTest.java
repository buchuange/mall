package com.atstar.mall.service.impl;

import com.atstar.mall.MallApplicationTests;
import com.atstar.mall.enums.ResponseEnum;
import com.atstar.mall.form.ShippingForm;
import com.atstar.mall.service.ShippingService;
import com.atstar.mall.vo.ResponseVO;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Map;

@Slf4j
@Transactional
class ShippingServiceImplTest extends MallApplicationTests {

    @Autowired
    private ShippingService shippingService;

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private ShippingForm shippingForm;

    private Integer uid = 10;

    private Integer shippingId;

    @BeforeEach
    void before() {
        ShippingForm shippingForm = new ShippingForm();
        shippingForm.setReceiverName("Kyrie Irving");
        shippingForm.setReceiverAddress("篮网");
        shippingForm.setReceiverCity("纽约");
        shippingForm.setReceiverDistrict("州121");
        shippingForm.setReceiverMobile("15641561");
        shippingForm.setReceiverPhone("1346845");
        shippingForm.setReceiverZip("001");
        shippingForm.setReceiverProvince("美国");

        this.shippingForm = shippingForm;

        saveShipping();
    }

    void saveShipping() {

        ResponseVO<Map<String, Integer>> responseVO = shippingService.saveShipping(uid, shippingForm);

        this.shippingId = responseVO.getData().get("shippingId");

        log.info("result={}", gson.toJson(responseVO));

        Assert.state(ResponseEnum.SUCCESS.getCode().equals(responseVO.getStatus()), "测试失败");

    }

    @AfterEach
    void deleteShipping() {

        ResponseVO responseVO = shippingService.deleteShipping(uid, shippingId);

        log.info("result={}", gson.toJson(responseVO));

        Assert.state(ResponseEnum.SUCCESS.getCode().equals(responseVO.getStatus()), "测试失败");
    }

    @Test
    void updateShipping() {


        ResponseVO responseVO = shippingService.updateShipping(uid, shippingId, shippingForm);

        log.info("result={}", gson.toJson(responseVO));

        Assert.state(ResponseEnum.SUCCESS.getCode().equals(responseVO.getStatus()), "测试失败");

    }


    @Test
    void listShipping() {

        ResponseVO<PageInfo> responseVO = shippingService.listShipping(uid, 1, 5);

        log.info("result={}", gson.toJson(responseVO));

        Assert.state(ResponseEnum.SUCCESS.getCode().equals(responseVO.getStatus()), "测试失败");
    }
}