package com.atstar.mall.service.impl;

import com.atstar.mall.domain.*;
import com.atstar.mall.enums.OrderStatusEnum;
import com.atstar.mall.enums.PaymentTypeEnum;
import com.atstar.mall.enums.ResponseEnum;
import com.atstar.mall.mapper.*;
import com.atstar.mall.service.CartService;
import com.atstar.mall.service.OrderService;
import com.atstar.mall.utils.UUIDUtil;
import com.atstar.mall.vo.OrderItemVO;
import com.atstar.mall.vo.OrderVO;
import com.atstar.mall.vo.ResponseVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.Data;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.atstar.mall.enums.ProductStatusEnum.DELETE;
import static com.atstar.mall.enums.ProductStatusEnum.OFF_SALE;

/**
 * @Author: Dawn
 * @Date: 2022/3/11 23:52
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private OrderShippingMapper orderShippingMapper;

    @Autowired
    private ShippingMapper shippingMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CartService cartService;

    @Transactional
    @Override
    public ResponseVO<OrderVO> saveOrder(Integer uid, Integer shippingId) {

        // 收获地址校验
        Shipping shipping = shippingMapper.selectByUidAndShippingId(uid, shippingId);

        if (ObjectUtils.isEmpty(shipping)) {
            return ResponseVO.error(ResponseEnum.SHIPPING_NOT_EXIST);
        }

        // 获取购物车，校验（是否有商品，库存）
        List<Cart> cartList = cartService.listCart(uid).stream()
                .filter(Cart::getProductSelected)
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(cartList)) {
            return ResponseVO.error(ResponseEnum.CART_SELECTED_IS_EMPTY);
        }

        // 获取cartList中的productIds
        Set<Integer> productIdSet = cartList.stream()
                .map(Cart::getProductId).collect(Collectors.toSet());

        List<Product> products = productMapper.selectByProductIds(productIdSet);

        Map<Integer, Product> map = products.stream()
                .collect(Collectors.toMap(Product::getId, product -> product));

        // 生成订单号
        String orderNo = UUIDUtil.getUUID();

        List<OrderItem> orderItemList = new ArrayList<>();
        for (Cart cart : cartList) {
            // 根据productId查询数据库
            Product product = map.get(cart.getProductId());
            // 是否有商品
            if (ObjectUtils.isEmpty(product)) {
                return ResponseVO.error(ResponseEnum.PRODUCT_NOT_EXIST, cart.getProductId() + "：该商品不存在");
            }

            // 商品是否正常在售
            if (OFF_SALE.getCode().equals(product.getStatus()) || DELETE.getCode().equals(product.getStatus())) {
                return ResponseVO.error(ResponseEnum.PRODUCT_OFF_SALE_OR_DELETE, product.getName() + ": 该商品已下架或删除");
            }

            // 库存是否充足
            if (product.getStock() < cart.getQuantity()) {
                return ResponseVO.error(ResponseEnum.PRODUCT_STOCK_ERROR, product.getName() + "：该商品库存不足");
            }

            OrderItem orderItem =  buildOrderItem(uid, orderNo, cart.getQuantity(), product);
            orderItemList.add(orderItem);

            // 减库存
            Integer residueStock = product.getStock() - cart.getQuantity();
            product.setStock(residueStock);

            int resultCountForProduct = productMapper.updateByPrimaryKeySelective(product);
            if (resultCountForProduct != 1) {
                return ResponseVO.error(ResponseEnum.ERROR);
            }
        }

        // 生成订单 入库  三张表 需要事务
        Order order = buildOrder(uid, orderNo, orderItemList);
        int resultCountForOrderShipping = orderMapper.insertSelective(order);
        if (resultCountForOrderShipping != 1) {
            return ResponseVO.error(ResponseEnum.ERROR);
        }

        int resultCountForOrderItem = orderItemMapper.insertBatch(orderItemList);
        if (resultCountForOrderItem != orderItemList.size()) {
            return ResponseVO.error(ResponseEnum.ERROR);
        }

        OrderShipping orderShipping = buildOrderShipping(orderNo, shipping);
        int resultCountForOrder = orderShippingMapper.insertSelective(orderShipping);
        if (resultCountForOrder != 1) {
            return ResponseVO.error(ResponseEnum.ERROR);
        }

        // 更新购物车
        // Redis中事务是打包一组命令 原子性  不能回滚 一定要最后做这个操作
        for (Cart cart : cartList) {
            cartService.deleteCart(uid, cart.getProductId());
        }

        // 构造orderVO
        OrderVO orderVO = buildOrderVO(order, orderItemList, orderShipping);

        return ResponseVO.success(orderVO);
    }

    @Override
    public ResponseVO<PageInfo> listOrder(Integer uid, Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        List<Order> orderList = orderMapper.selectByUid(uid);

        Set<String> orderNoSet = orderList.stream().map(Order::getOrderNo).collect(Collectors.toSet());

        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNoSet(orderNoSet);
        Map<String, List<OrderItem>> orderItemMap = orderItemList.stream()
                .collect(Collectors.groupingBy(OrderItem::getOrderNo));


        List<OrderShipping> orderShippingList = orderShippingMapper.selectByOrderNoSet(orderNoSet);
        Map<String, OrderShipping> orderShippingMap = orderShippingList.stream()
                .collect(Collectors.toMap(OrderShipping::getOrderNo, orderShipping -> orderShipping));

        List<OrderVO> orderVOList = new ArrayList<>();

        for (Order order : orderList) {

            OrderVO orderVO = buildOrderVO(order, orderItemMap.get(order.getOrderNo()), orderShippingMap.get(order.getOrderNo()));

            orderVOList.add(orderVO);
        }


        PageInfo pageInfo = new PageInfo<>(orderList);
        pageInfo.setList(orderVOList);

        return ResponseVO.success(pageInfo);
    }

    @Override
    public ResponseVO<OrderVO> getOrder(Integer uid, String orderNo) {

        Order order = orderMapper.selectByOrderNo(orderNo);
        if (ObjectUtils.isEmpty(order) || !uid.equals(order.getUserId())) {
            return ResponseVO.error(ResponseEnum.ORDER_NOT_EXIST);
        }

        Set<String> orderNoSet = new HashSet<>();
        orderNoSet.add(order.getOrderNo());

        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNoSet(orderNoSet);

        OrderShipping orderShipping = orderShippingMapper.selectByOrderNo(orderNo);

        OrderVO orderVO = buildOrderVO(order, orderItemList, orderShipping);

        return ResponseVO.success(orderVO);
    }

    @Override
    public ResponseVO cancelOrder(Integer uid, String orderNo) {

        Order order = orderMapper.selectByOrderNo(orderNo);
        if (ObjectUtils.isEmpty(order) || !uid.equals(order.getUserId())) {
            return ResponseVO.error(ResponseEnum.ORDER_NOT_EXIST);
        }

        if (!OrderStatusEnum.NO_PAY.getCode().equals(order.getStatus())) {
            return ResponseVO.error(ResponseEnum.ORDER_STATUS_ERROR);
        }

        order.setStatus(OrderStatusEnum.CANCELED.getCode());
        order.setCloseTime(new Date());

        int resultCount = orderMapper.updateByPrimaryKeySelective(order);
        if (resultCount != 1) {
            return ResponseVO.error(ResponseEnum.ERROR);
        }

        return ResponseVO.success();
    }

    private OrderVO buildOrderVO(Order order, List<OrderItem> orderItemList, OrderShipping shipping) {

        OrderVO orderVO = new OrderVO();

        BeanUtils.copyProperties(order, orderVO);

        List<OrderItemVO> orderItemVOList = orderItemList.stream()
                .map(o -> {
                    OrderItemVO orderItemVO = new OrderItemVO();
                    BeanUtils.copyProperties(o, orderItemVO);
                    return orderItemVO;
                }).collect(Collectors.toList());

        orderVO.setOrderItemVOList(orderItemVOList);
        orderVO.setShippingVO(shipping);

        return orderVO;
    }

    private Order buildOrder(Integer uid, String orderNo, List<OrderItem> orderItemList) {

        BigDecimal payment = orderItemList.stream().map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setUserId(uid);
        order.setOrderNo(orderNo);
        order.setPayment(payment);
        order.setPaymentType(PaymentTypeEnum.PAY_ONLINE.getCode());
        order.setPostage(10);
        order.setStatus(OrderStatusEnum.NO_PAY.getCode());



        return order;
    }

    private OrderShipping buildOrderShipping(String orderNo, Shipping shipping) {

        OrderShipping orderShipping = new OrderShipping();
        BeanUtils.copyProperties(shipping, orderShipping);
        orderShipping.setId(null);
        orderShipping.setShippingId(shipping.getId());
        orderShipping.setOrderNo(orderNo);
        orderShipping.setCreateTime(null);
        orderShipping.setUpdateTime(null);

        return orderShipping;
    }

    private OrderItem buildOrderItem(Integer uid, String orderNo, Integer quantity, Product product) {

        OrderItem orderItem = new OrderItem();

        orderItem.setUserId(uid);
        orderItem.setOrderNo(orderNo);
        orderItem.setProductId(product.getId());
        orderItem.setProductName(product.getName());
        orderItem.setProductImage(product.getMainImage());
        orderItem.setCurrentUnitPrice(product.getPrice());
        orderItem.setQuantity(quantity);
        orderItem.setTotalPrice(BigDecimal.valueOf(quantity).multiply(product.getPrice()));

        return orderItem;
    }
}
