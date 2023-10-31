package com.zenith.spzx.manager.service;

import com.github.pagehelper.PageInfo;
import com.zenith.spzx.model.entity.product.ProductSpec;

import java.util.List;

public interface ProductSpecService {
    PageInfo<ProductSpec> findByPage(Integer page , Integer limit);

    void save(ProductSpec productSpec);

    void update(ProductSpec productSpec);

    void deleteById(Long id);

    List<ProductSpec> list();
}
