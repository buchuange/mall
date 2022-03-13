package com.atstar.mall.listener;

import com.atstar.mall.service.OrderService;
import com.atstar.pay.domain.PayInfo;
import com.google.gson.Gson;
import com.lly835.bestpay.enums.OrderStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: Dawn
 * @Date: 2022/3/13 00:11
 */
@Slf4j
@Component
@RabbitListener(queues = "payNotify")
public class PayMsgListener {


    @Autowired
    private OrderService orderService;

    /**
     * 关于payInfo 应该是Pay项目提供client.jar, mall项目引用client.jar
     * @param msg
     */
    @RabbitHandler
    public void process(String msg) {
        log.info("【接收到消息】=> {}", msg);

        PayInfo payInfo = new Gson().fromJson(msg, PayInfo.class);
        if (payInfo.getPlatformStatus().equals(OrderStatusEnum.SUCCESS.name())) {
            // 修改订单里的状态
            orderService.paid(payInfo.getOrderNo());
        }
    }
}
