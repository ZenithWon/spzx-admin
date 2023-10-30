package com.zenith.spzx.manager.service;

import com.github.pagehelper.PageInfo;
import com.zenith.spzx.model.dto.product.CategoryBrandDto;
import com.zenith.spzx.model.entity.product.CategoryBrand;

public interface CategoryBrandService {
    PageInfo<CategoryBrand> findByPage(Integer page , Integer limit , CategoryBrandDto categoryBrandDto);

    void save(CategoryBrand categoryBrand);

    void edit(CategoryBrand categoryBrand);

    void delete(Long id);
}
