package com.zenith.spzx.product.service;

import com.zenith.spzx.model.entity.product.Category;

import java.util.List;

public interface CategoryService {
    List<Category> findCategoryByParent(Long parentId);

    List<Category> getCategoryTree(Long parentId);
}
