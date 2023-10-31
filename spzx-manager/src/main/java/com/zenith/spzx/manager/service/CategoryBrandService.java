package com.zenith.spzx.manager.service;

import com.github.pagehelper.PageInfo;
import com.zenith.spzx.model.dto.product.CategoryBrandDto;
import com.zenith.spzx.model.entity.product.Brand;
import com.zenith.spzx.model.entity.product.Category;
import com.zenith.spzx.model.entity.product.CategoryBrand;

import java.util.List;

public interface CategoryBrandService {
    PageInfo<CategoryBrand> findByPage(Integer page , Integer limit , CategoryBrandDto categoryBrandDto);

    void save(CategoryBrand categoryBrand);

    void edit(CategoryBrand categoryBrand);

    void delete(Long id);

    List<Brand> getBrandByCategoryId(Long categoryId);
}
