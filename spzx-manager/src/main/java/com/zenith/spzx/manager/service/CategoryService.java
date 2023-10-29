package com.zenith.spzx.manager.service;

import com.zenith.spzx.model.entity.product.Category;

import java.util.List;

public interface CategoryService {
    List<Category> findByPaerntId(Long parentId);
}
