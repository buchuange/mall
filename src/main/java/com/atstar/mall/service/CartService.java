package com.atstar.mall.service;

import com.atstar.mall.domain.Cart;
import com.atstar.mall.form.CartAddForm;
import com.atstar.mall.form.CartUpdateForm;
import com.atstar.mall.vo.CartVO;
import com.atstar.mall.vo.ResponseVO;

import java.util.List;

/**
 * @Author: Dawn
 * @Date: 2022/3/9 11:04
 */
public interface CartService {

    ResponseVO<CartVO> saveCart(Integer uid, CartAddForm cartAddForm);

    ResponseVO<CartVO> updateCart(Integer uid, Integer productId, CartUpdateForm cartUpdateForm);

    ResponseVO<CartVO> deleteCart(Integer uid, Integer productId);

    ResponseVO<CartVO> listCarts(Integer uid);

    ResponseVO<CartVO> selectAll(Integer uid);

    ResponseVO<CartVO> unSelectAll(Integer uid);

    ResponseVO<Integer> countCart(Integer uid);

    List<Cart> listCart(Integer uid);
}
