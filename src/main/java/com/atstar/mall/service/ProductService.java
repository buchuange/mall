package com.atstar.mall.service;

import com.atstar.mall.vo.ProductDetailVO;
import com.atstar.mall.vo.ResponseVO;
import com.github.pagehelper.PageInfo;

public interface ProductService {

    ResponseVO<PageInfo> listProducts(Integer categoryId, Integer pageNum, Integer pageSize);

    ResponseVO<ProductDetailVO> getProductById(Integer productId);
}
