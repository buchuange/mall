package com.atstar.mall.service;

import com.atstar.mall.vo.CategoryVO;
import com.atstar.mall.vo.ResponseVO;

import java.util.List;

public interface CategoryService {

    ResponseVO<List<CategoryVO>> listCategories();
}
