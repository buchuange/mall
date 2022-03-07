package com.atstar.mall.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: Dawn
 * @Date: 2022/3/7 11:32
 */
@Data
public class CategoryVO {

    private Integer id;

    private Integer parentId;

    private String name;

    private Integer sortOrder;

    private List<CategoryVO> subCategories;
}
