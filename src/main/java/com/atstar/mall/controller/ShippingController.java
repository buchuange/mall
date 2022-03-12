package com.atstar.mall.controller;

import com.atstar.mall.domain.User;
import com.atstar.mall.form.ShippingForm;
import com.atstar.mall.service.ShippingService;
import com.atstar.mall.vo.ResponseVO;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Map;

import static com.atstar.mall.consts.MallConst.CURRENT_USER;

/**
 * @Author: Dawn
 * @Date: 2022/3/11 12:50
 */
@RestController
public class ShippingController {

    @Autowired
    private ShippingService shippingService;

    @PostMapping("/shipping")
    public ResponseVO<Map<String, Integer>> saveShipping(@Valid @RequestBody ShippingForm shippingForm,
                                                         HttpSession session) {

        User user = (User) session.getAttribute(CURRENT_USER);

        return shippingService.saveShipping(user.getId(), shippingForm);
    }

    @DeleteMapping("/shipping/{shippingId}")
    public ResponseVO deleteShipping(@PathVariable("shippingId") Integer shippingId,
                                     HttpSession session) {

        User user = (User) session.getAttribute(CURRENT_USER);

        return shippingService.deleteShipping(user.getId(), shippingId);
    }

    @PutMapping("/shipping/{shippingId}")
    public ResponseVO updateShipping(@PathVariable("shippingId") Integer shippingId,
                                     @Valid @RequestBody ShippingForm shippingForm,
                                     HttpSession session) {

        User user = (User) session.getAttribute(CURRENT_USER);

        return shippingService.updateShipping(user.getId(), shippingId, shippingForm);
    }

    @GetMapping("/shipping")
    public ResponseVO<PageInfo> listShipping(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                             @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                             HttpSession session) {

        User user = (User) session.getAttribute(CURRENT_USER);

        return shippingService.listShipping(user.getId(), pageNum, pageSize);
    }
}
