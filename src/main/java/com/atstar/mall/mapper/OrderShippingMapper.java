package com.atstar.mall.mapper;

import com.atstar.mall.domain.OrderShipping;

import java.util.List;
import java.util.Set;

public interface OrderShippingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderShipping record);

    int insertSelective(OrderShipping record);

    OrderShipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderShipping record);

    int updateByPrimaryKey(OrderShipping record);

    List<OrderShipping> selectByOrderNoSet(Set<String> orderNoSet);

    OrderShipping selectByOrderNo(String orderNo);
}