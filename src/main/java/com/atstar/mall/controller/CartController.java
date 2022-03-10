package com.atstar.mall.controller;

import com.atstar.mall.domain.User;
import com.atstar.mall.form.CartAddForm;
import com.atstar.mall.form.CartUpdateForm;
import com.atstar.mall.service.CartService;
import com.atstar.mall.vo.CartVO;
import com.atstar.mall.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static com.atstar.mall.consts.MallConst.CURRENT_USER;

/**
 * @Author: Dawn
 * @Date: 2022/3/8 22:31
 */
@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/carts")
    public ResponseVO<CartVO> carts(HttpSession session) {

        User user = (User) session.getAttribute(CURRENT_USER);
        return cartService.listCarts(user.getId());
    }

    @PostMapping("/carts")
    public ResponseVO<CartVO> saveCart(@Valid @RequestBody CartAddForm cartAddForm,
                                       HttpSession session) {

        User user = (User) session.getAttribute(CURRENT_USER);

        return cartService.saveCart(user.getId(), cartAddForm);
    }

    @PutMapping("/carts/{productId}")
    public ResponseVO<CartVO> updateCart(@PathVariable("productId") Integer productId,
                                         @Valid @RequestBody CartUpdateForm cartUpdateForm,
                                         HttpSession session) {

        User user = (User) session.getAttribute(CURRENT_USER);

        return cartService.updateCart(user.getId(), productId, cartUpdateForm);
    }

    @DeleteMapping("/carts/{productId}")
    public ResponseVO<CartVO> deleteCart(@PathVariable("productId") Integer productId,
                                         HttpSession session) {

        User user = (User) session.getAttribute(CURRENT_USER);

        return cartService.deleteCart(user.getId(), productId);
    }

    @PutMapping("/carts/selectAll")
    public ResponseVO<CartVO> selectAll(HttpSession session) {

        User user = (User) session.getAttribute(CURRENT_USER);

        return cartService.selectAll(user.getId());
    }

    @PutMapping("/carts/unSelectAll")
    public ResponseVO<CartVO> unSelectAll(HttpSession session) {

        User user = (User) session.getAttribute(CURRENT_USER);

        return cartService.unSelectAll(user.getId());
    }

    @GetMapping("/carts/products/sum")
    public ResponseVO<Integer> countCart(HttpSession session) {

        User user = (User) session.getAttribute(CURRENT_USER);

        return cartService.countCart(user.getId());
    }
}
