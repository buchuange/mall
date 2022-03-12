package com.atstar.mall.mapper;

import com.atstar.mall.domain.OrderItem;

import java.util.List;
import java.util.Set;

public interface OrderItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderItem record);

    int insertSelective(OrderItem record);

    OrderItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderItem record);

    int updateByPrimaryKey(OrderItem record);

    int insertBatch(List<OrderItem> orderItemList);

    List<OrderItem> selectByOrderNoSet(Set<String> orderNoSet);
}