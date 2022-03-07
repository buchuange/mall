package com.atstar.mall.controller;

import com.atstar.mall.service.CategoryService;
import com.atstar.mall.vo.CategoryVO;
import com.atstar.mall.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: Dawn
 * @Date: 2022/3/7 11:54
 */
@Slf4j
@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public ResponseVO<List<CategoryVO>> listCategories() {

        return categoryService.listCategories();
    }
}
