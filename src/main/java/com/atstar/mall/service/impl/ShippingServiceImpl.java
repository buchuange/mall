package com.atstar.mall.service.impl;

import com.atstar.mall.domain.Shipping;
import com.atstar.mall.enums.ResponseEnum;
import com.atstar.mall.form.ShippingForm;
import com.atstar.mall.mapper.ShippingMapper;
import com.atstar.mall.service.ShippingService;
import com.atstar.mall.vo.ResponseVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Dawn
 * @Date: 2022/3/11 00:32
 */
@Service
@Slf4j
public class ShippingServiceImpl implements ShippingService {

    @Autowired
    private ShippingMapper shippingMapper;

    @Override
    public ResponseVO<Map<String, Integer>> saveShipping(Integer uid, ShippingForm shippingForm) {

        Shipping shipping = new Shipping();
        BeanUtils.copyProperties(shippingForm, shipping);

        shipping.setUserId(uid);

        int resultCount = shippingMapper.insertSelective(shipping);

        if (resultCount != 1) {
            return ResponseVO.error(ResponseEnum.ERROR);
        }

        Map<String, Integer> map = new HashMap<>();

        map.put("shippingId", shipping.getId());

        return ResponseVO.success("新建地址成功", map);
    }

    @Override
    public ResponseVO deleteShipping(Integer uid, Integer shippingId) {


        int resultCount = shippingMapper.deleteByUidAndId(uid, shippingId);

        if (resultCount != 1) {
            return ResponseVO.error(ResponseEnum.DELETE_SHIPPING_FAIL);
        }

        return ResponseVO.successByMsg("删除地址成功");
    }

    @Override
    public ResponseVO updateShipping(Integer uid, Integer shippingId, ShippingForm shippingForm) {

        Shipping shipping = new Shipping();
        BeanUtils.copyProperties(shippingForm, shipping);
        shipping.setUserId(uid);
        shipping.setId(shippingId);

        int resultCount = shippingMapper.updateByPrimaryKeySelective(shipping);

        if (resultCount != 1) {
            return ResponseVO.error(ResponseEnum.ERROR);
        }

        return ResponseVO.success("更新地址成功");
    }

    @Override
    public ResponseVO<PageInfo> listShipping(Integer uid, Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize);

        List<Shipping> shippingList = shippingMapper.selectByUid(uid);

        PageInfo<Shipping> pageInfo = new PageInfo<>(shippingList);

        return ResponseVO.success(pageInfo);
    }
}
