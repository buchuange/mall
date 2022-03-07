package com.atstar.mall.service.impl;

import com.atstar.mall.MallApplicationTests;
import com.atstar.mall.enums.ResponseEnum;
import com.atstar.mall.service.CategoryService;
import com.atstar.mall.vo.CategoryVO;
import com.atstar.mall.vo.ResponseVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryServiceImplTest extends MallApplicationTests {

    @Autowired
    private CategoryService categoryService;

    @Test
    public void listCategories() {

        ResponseVO<List<CategoryVO>> listCategories = categoryService.listCategories();

        Assert.state(ResponseEnum.SUCCESS.getCode() == listCategories.getStatus(), "测试失败");

    }
}