package com.atstar.mall.service;

import com.atstar.mall.vo.CategoryVO;
import com.atstar.mall.vo.ResponseVO;

import java.util.List;
import java.util.Set;

public interface CategoryService {

    ResponseVO<List<CategoryVO>> listCategories();

    void findSubCategoryId(Integer id, Set<Integer> resultSet);
}
