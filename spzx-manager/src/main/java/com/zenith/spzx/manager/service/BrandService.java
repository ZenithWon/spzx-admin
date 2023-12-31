package com.zenith.spzx.manager.service;

import com.github.pagehelper.PageInfo;
import com.zenith.spzx.model.entity.product.Brand;

import java.util.List;

public interface BrandService {
    PageInfo<Brand> findByPage(Integer page , Integer limit);

    void saveBrand(Brand brand);

    void editBrand(Brand brand);

    void deleteById(Long id);

    List<Brand> findAll();
}
