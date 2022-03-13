package com.atstar.mall.service.impl;

import com.atstar.mall.MallApplicationTests;
import com.atstar.mall.enums.ResponseEnum;
import com.atstar.mall.service.CategoryService;
import com.atstar.mall.vo.CategoryVO;
import com.atstar.mall.vo.ResponseVO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Transactional
public class CategoryServiceImplTest extends MallApplicationTests {

    @Autowired
    private CategoryService categoryService;

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Test
    public void listCategories() {

        ResponseVO<List<CategoryVO>> listCategories = categoryService.listCategories();

        log.info("result={}", gson.toJson(listCategories));

        Assert.state(ResponseEnum.SUCCESS.getCode().equals(listCategories.getStatus()), "测试失败");

    }

    @Test
    public void findSubCategoryId() {

        Set<Integer> set = new HashSet<>();

        categoryService.findSubCategoryId(100001, set);

        log.info("ResultSet: {}", set);

    }
}