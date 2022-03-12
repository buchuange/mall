package com.atstar.mall.service.impl;

import com.atstar.mall.domain.Cart;
import com.atstar.mall.domain.Product;
import com.atstar.mall.enums.ResponseEnum;
import com.atstar.mall.form.CartAddForm;
import com.atstar.mall.form.CartUpdateForm;
import com.atstar.mall.mapper.ProductMapper;
import com.atstar.mall.service.CartService;
import com.atstar.mall.vo.CartProductVO;
import com.atstar.mall.vo.CartVO;
import com.atstar.mall.vo.ResponseVO;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static com.atstar.mall.enums.ProductStatusEnum.DELETE;
import static com.atstar.mall.enums.ProductStatusEnum.OFF_SALE;
import static com.atstar.mall.enums.ResponseEnum.*;

/**
 * @Author: Dawn
 * @Date: 2022/3/9 11:05
 */
@Service
@Slf4j
public class CartServiceImpl implements CartService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String CART_REDIS_KEY_TEMPLATE = "cart_%d";

    private Gson gson = new Gson();

    @Override
    public ResponseVO<CartVO> saveCart(Integer uid, CartAddForm cartAddForm) {

        Integer quantity = 1;

        // 商品是否存在
        Product product = productMapper.selectByPrimaryKey(cartAddForm.getProductId());
        if (ObjectUtils.isEmpty(product)) {
            return ResponseVO.error(PRODUCT_NOT_EXIST);
        }

        // 商品是否正常在售
        if (OFF_SALE.getCode().equals(product.getStatus()) || DELETE.getCode().equals(product.getStatus())) {
            return ResponseVO.error(ResponseEnum.PRODUCT_OFF_SALE_OR_DELETE, product.getName() + ": 该商品已下架或删除");
        }

        // 商品库存是否充足
        if (product.getStock() <= 0) {
            return ResponseVO.error(PRODUCT_STOCK_ERROR);
        }

        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();

        // 先从Redis中获取该购物车
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uid);

        String value =  opsForHash.get(redisKey, String.valueOf(product.getId()));
        Cart cart;
        if (!StringUtils.hasLength(value)) {
            // 没有该商品，新增
           cart = new Cart(product.getId(), quantity, cartAddForm.getSelected());
        } else {
            // 已经有了，数量+1
            cart = gson.fromJson(value, Cart.class);
            cart.setQuantity(cart.getQuantity() + quantity);
        }

        // 将购物车写入到Redis key: cart_1
        opsForHash.put(redisKey, String.valueOf(product.getId()), gson.toJson(cart));


        return listCarts(uid);
    }

    @Override
    public ResponseVO<CartVO> updateCart(Integer uid, Integer productId, CartUpdateForm cartUpdateForm) {

        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();

        // 先从Redis中获取该购物车
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uid);

        String value = opsForHash.get(redisKey, String.valueOf(productId));

        if (!StringUtils.hasLength(value)) {
            // 没有该商品，报错
            return ResponseVO.error(CART_PRODUCT_NOT_EXIST);
        }

        // 已经有了，修改内容
        Cart cart = gson.fromJson(value, Cart.class);
        if (cartUpdateForm.getQuantity() != null && cartUpdateForm.getQuantity() >= 0) {
            cart.setQuantity(cartUpdateForm.getQuantity());
        }
        if (cartUpdateForm.getSelected() != null) {
            cart.setProductSelected(cartUpdateForm.getSelected());
        }

        // 将购物车写入到Redis
        opsForHash.put(redisKey, String.valueOf(productId), gson.toJson(cart));

        return listCarts(uid);
    }

    @Override
    public ResponseVO<CartVO> deleteCart(Integer uid, Integer productId) {

        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();

        // 先从Redis中获取该购物车
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uid);

        String value = opsForHash.get(redisKey, String.valueOf(productId));

        if (!StringUtils.hasLength(value)) {
            // 没有该商品，报错
            return ResponseVO.error(CART_PRODUCT_NOT_EXIST);
        }

        opsForHash.delete(redisKey, String.valueOf(productId));

        return listCarts(uid);
    }

    /**
     * 从Redis中获取该购物车列表
     * @param uid 用户ID
     * @return
     */
    @Override
    public ResponseVO<CartVO> listCarts(Integer uid) {

        List<Cart> carts = listCart(uid);

        if (CollectionUtils.isEmpty(carts)) {
            return ResponseVO.error(ResponseEnum.CART_IS_EMPTY);
        }

        Set<Integer> collect = carts.stream().map(Cart::getProductId).collect(Collectors.toSet());

        List<Product> products = productMapper.selectByProductIds(collect);

        Map<Integer, Product> map = products.stream()
                .collect(Collectors.toMap(Product::getId, product -> product));

        boolean selectAll = true;
        Integer cartTotalQuantity = 0;
        BigDecimal cartTotalPrice = BigDecimal.ZERO;


        List<CartProductVO> cartProductVOList = new ArrayList<>();

        for (Cart cart : carts) {

            Product product = map.get(cart.getProductId());
            if (!ObjectUtils.isEmpty(product)) {
                CartProductVO cartProductVO = new CartProductVO();
                cartProductVO.setProductId(product.getId());
                cartProductVO.setQuantity(cart.getQuantity());
                cartProductVO.setProductName(product.getName());
                cartProductVO.setProductSubtitle(product.getSubtitle());
                cartProductVO.setProductMainImage(product.getMainImage());
                cartProductVO.setProductPrice(product.getPrice());
                cartProductVO.setProductStatus(product.getStatus());
                cartProductVO.setProductTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())));
                cartProductVO.setProductStock(product.getStock());
                cartProductVO.setProductSelected(cart.getProductSelected());

                cartProductVOList.add(cartProductVO);

                if (!cart.getProductSelected()) {
                    selectAll = false;
                }


                if (cart.getProductSelected()) {
                    // 计算总价（只计算选中的）
                    cartTotalPrice = cartTotalPrice.add(cartProductVO.getProductTotalPrice());

                    cartTotalQuantity += cart.getQuantity();
                }
            }
        }

        CartVO cartVO = new CartVO();
        cartVO.setCartProductVOList(cartProductVOList);
        cartVO.setSelectedAll(selectAll);
        cartVO.setCartTotalPrice(cartTotalPrice);
        cartVO.setCartTotalQuantity(cartTotalQuantity);

        return ResponseVO.success(cartVO);
    }

    @Override
    public ResponseVO<CartVO> selectAll(Integer uid) {

        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();

        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uid);

        List<Cart> carts = listCart(uid);
        for (Cart cart : carts) {
            cart.setProductSelected(true);
            opsForHash.put(redisKey,
                    String.valueOf(cart.getProductId()), gson.toJson(cart));
        }


        return listCarts(uid);
    }

    @Override
    public ResponseVO<CartVO> unSelectAll(Integer uid) {

        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();

        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uid);

        List<Cart> carts = listCart(uid);
        for (Cart cart : carts) {
            cart.setProductSelected(false);
            opsForHash.put(redisKey,
                    String.valueOf(cart.getProductId()), gson.toJson(cart));
        }


        return listCarts(uid);
    }

    @Override
    public ResponseVO<Integer> countCart(Integer uid) {

        Integer countCart = listCart(uid).stream()
                .map(Cart::getQuantity)
                .reduce(0, Integer::sum);

        return ResponseVO.success(countCart);
    }

    @Override
    public List<Cart> listCart(Integer uid) {

        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();

        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uid);

        Map<String, String> map = opsForHash.entries(redisKey);

        List<Cart> carts = new ArrayList<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {

            Cart cart = gson.fromJson(entry.getValue(), Cart.class);

            carts.add(cart);
        }

        return carts;
    }

}
