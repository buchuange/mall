package com.atstar.mall.service.impl;

import com.atstar.mall.domain.Product;
import com.atstar.mall.enums.ResponseEnum;
import com.atstar.mall.mapper.ProductMapper;
import com.atstar.mall.service.CategoryService;
import com.atstar.mall.service.ProductService;
import com.atstar.mall.vo.ProductDetailVO;
import com.atstar.mall.vo.ProductVO;
import com.atstar.mall.vo.ResponseVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.atstar.mall.enums.ProductStatusEnum.DELETE;
import static com.atstar.mall.enums.ProductStatusEnum.OFF_SALE;

/**
 * @Author: Dawn
 * @Date: 2022/3/8 12:40
 */
@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public ResponseVO<PageInfo> listProducts(Integer categoryId, Integer pageNum, Integer pageSize) {

        Set<Integer> categoryIdSet = new HashSet<>();

        if (categoryId != null) {
            categoryService.findSubCategoryId(categoryId, categoryIdSet);
            categoryIdSet.add(categoryId);
        }

        PageHelper.startPage(pageNum, pageSize);

        List<Product> productList = productMapper.selectByCategoryIdSet(categoryIdSet);
        List<ProductVO> productVOList = productList.stream()
                .map(p -> {
                    ProductVO productVO = new ProductVO();
                    BeanUtils.copyProperties(p, productVO);
                    return productVO;
                })
                .collect(Collectors.toList());

        PageInfo pageInfo = new PageInfo<>(productList);
        pageInfo.setList(productVOList);

        return ResponseVO.success(pageInfo);
    }

    @Override
    public ResponseVO<ProductDetailVO> getProductById(Integer productId) {

        Product product = productMapper.selectByPrimaryKey(productId);

        if (product.getStatus().equals(OFF_SALE.getCode()) || product.getStatus().equals(DELETE.getCode())) {
            return ResponseVO.error(ResponseEnum.PRODUCT_OFF_SALE_OR_DELETE);
        }

        ProductDetailVO productDetailVO = new ProductDetailVO();
        BeanUtils.copyProperties(product, productDetailVO);

        // 敏感数据处理
        productDetailVO.setStock(productDetailVO.getStock() > 100 ? 100 : productDetailVO.getStock());

        return ResponseVO.success(productDetailVO);
    }
}
