package com.atstar.mall.service.impl;

import com.atstar.mall.domain.Category;
import com.atstar.mall.mapper.CategoryMapper;
import com.atstar.mall.service.CategoryService;
import com.atstar.mall.vo.CategoryVO;
import com.atstar.mall.vo.ResponseVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.atstar.mall.consts.MallConst.ROOT_PARENT_ID;

/**
 * @Author: Dawn
 * @Date: 2022/3/7 11:37
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;


    /**
     * 耗时 Http(请求微信API) > 磁盘 > 内存
     * mysql(内网+磁盘)
     * @return
     */
    @Override
    public ResponseVO<List<CategoryVO>> listCategories() {

        List<Category> categories = categoryMapper.listCategories();

        List<CategoryVO> categoryVOList = categories.stream()
                .filter(c -> c.getParentId().equals(ROOT_PARENT_ID))
                .map(this::categoryToCategoryVO)
                .sorted(Comparator.comparing(CategoryVO::getSortOrder).reversed())
                .collect(Collectors.toList());

        // 设置子目录
        listSubCategories(categories, categoryVOList);

        return ResponseVO.success(categoryVOList);
    }


    private void listSubCategories(List<Category> categories, List<CategoryVO> categoryVOList) {

        for (CategoryVO vo : categoryVOList) {
            List<CategoryVO> subCategories = new ArrayList<>();

            for (Category category : categories) {

                if (vo.getId().equals(category.getParentId())) {
                    CategoryVO categoryVO = categoryToCategoryVO(category);
                    subCategories.add(categoryVO);
                }

                listSubCategories(categories, subCategories);

            }

            subCategories.sort(Comparator.comparing(CategoryVO::getSortOrder).reversed());
            vo.setSubCategories(subCategories);
        }
    }

    private CategoryVO categoryToCategoryVO(Category category) {

        CategoryVO categoryVO = new CategoryVO();
        BeanUtils.copyProperties(category, categoryVO);

        return categoryVO;
    }

    @Override
    public void findSubCategoryId(Integer id, Set<Integer> resultSet) {
        List<Category> categories = categoryMapper.listCategories();

        findSubCategoryId(id, resultSet, categories);
    }

    private void findSubCategoryId(Integer id, Set<Integer> resultSet, List<Category> categories) {
        for (Category category : categories) {
            if (category.getParentId().equals(id)) {
                resultSet.add(category.getId());
                findSubCategoryId(category.getId(), resultSet, categories);
            }
        }
    }
}
