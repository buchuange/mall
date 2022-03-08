package com.atstar.mall.controller;

import com.atstar.mall.service.ProductService;
import com.atstar.mall.vo.ProductDetailVO;
import com.atstar.mall.vo.ResponseVO;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.ws.Response;

/**
 * @Author: Dawn
 * @Date: 2022/3/8 14:41
 */
@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public ResponseVO<PageInfo> listProducts(@RequestParam(required = false) Integer categoryId,
                                             @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                             @RequestParam(required = false, defaultValue = "10") Integer pageSize) {

        return productService.listProducts(categoryId, pageNum, pageSize);
    }

    @GetMapping("/products/{productId}")
    public ResponseVO<ProductDetailVO> getProductById(@PathVariable("productId") Integer prductId) {

        return productService.getProductById(prductId);
    }

}
