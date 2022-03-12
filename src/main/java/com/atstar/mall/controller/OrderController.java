package com.atstar.mall.controller;

import com.atstar.mall.domain.User;
import com.atstar.mall.form.OrderCreateForm;
import com.atstar.mall.service.OrderService;
import com.atstar.mall.vo.OrderVO;
import com.atstar.mall.vo.ResponseVO;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static com.atstar.mall.consts.MallConst.CURRENT_USER;

/**
 * @Author: Dawn
 * @Date: 2022/3/12 18:29
 */
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/orders")
    public ResponseVO<OrderVO> saveOrder(@Valid @RequestBody OrderCreateForm orderCreateForm,
                                         HttpSession session) {

        User user = (User) session.getAttribute(CURRENT_USER);

        return orderService.saveOrder(user.getId(), orderCreateForm.getShippingId());
    }

    @GetMapping("/orders")
    public ResponseVO<PageInfo> listOrder(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                          @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                          HttpSession session) {

        User user = (User) session.getAttribute(CURRENT_USER);

        return orderService.listOrder(user.getId(), pageNum, pageSize);
    }

    @GetMapping("/orders/{orderNo}")
    public ResponseVO<OrderVO> getOrder(@PathVariable("orderNo") String orderNo, HttpSession session) {

        User user = (User) session.getAttribute(CURRENT_USER);

        return orderService.getOrder(user.getId(), orderNo);
    }

    @PutMapping("/orders/{orderNo}")
    public ResponseVO cancelOrder(@PathVariable("orderNo") String orderNo, HttpSession session) {

        User user = (User) session.getAttribute(CURRENT_USER);

        return orderService.cancelOrder(user.getId(), orderNo);
    }
}
