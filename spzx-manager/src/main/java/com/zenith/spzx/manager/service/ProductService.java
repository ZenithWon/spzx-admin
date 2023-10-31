package com.zenith.spzx.manager.service;

import com.github.pagehelper.PageInfo;
import com.zenith.spzx.model.dto.product.ProductDto;
import com.zenith.spzx.model.entity.base.ProductUnit;
import com.zenith.spzx.model.entity.product.Product;

import java.util.List;

public interface ProductService {
    PageInfo<Product> findByPage(Integer page , Integer limit , ProductDto productDto);

    List<ProductUnit> getProductUnit();

    void save(Product product);

    Product getById(Long id);

    void edit(Product product);

    void deleteById(Long id);

    void updateAuditStatus(Long id , Integer auditStatus);

    void updateStatus(Long id , Integer status);
}
