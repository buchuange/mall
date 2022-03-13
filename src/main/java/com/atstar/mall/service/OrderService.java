package com.atstar.mall.service;

import com.atstar.mall.vo.OrderVO;
import com.atstar.mall.vo.ResponseVO;
import com.github.pagehelper.PageInfo;

/**
 * @Author: Dawn
 * @Date: 2022/3/11 23:45
 */
public interface OrderService {

    ResponseVO<OrderVO> saveOrder(Integer uid, Integer shippingId);

    ResponseVO<PageInfo> listOrder(Integer uid, Integer pageNum, Integer pageSize);

    ResponseVO<OrderVO> getOrder(Integer uid, String orderNo);

    ResponseVO cancelOrder(Integer uid, String orderNo);

    void paid(String orderNo);
}
