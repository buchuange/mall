package com.atstar.mall.vo;

import com.atstar.mall.domain.OrderShipping;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author: Dawn
 * @Date: 2022/3/11 23:26
 */
@Data
public class OrderVO {

    private String orderNo;

    private BigDecimal payment;

    private Integer paymentType;

    private Integer postage;

    private Integer status;

    private Date paymentTime;

    private Date sendTime;

    private Date endTime;

    private Date closeTime;

    private Date createTime;

    private List<OrderItemVO> orderItemVOList;

    private OrderShipping shippingVO;

}
