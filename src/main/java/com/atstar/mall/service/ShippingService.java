package com.atstar.mall.service;

import com.atstar.mall.form.ShippingForm;
import com.atstar.mall.vo.ResponseVO;
import com.github.pagehelper.PageInfo;

import java.util.Map;

public interface ShippingService {

    ResponseVO<Map<String, Integer>> saveShipping(Integer uid, ShippingForm shippingForm);

    ResponseVO deleteShipping(Integer uid, Integer shippingId);

    ResponseVO updateShipping(Integer uid, Integer shippingId, ShippingForm shippingForm);

    ResponseVO<PageInfo> listShipping(Integer uid, Integer pageNum, Integer pageSize);
}
