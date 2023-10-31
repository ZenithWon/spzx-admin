package com.zenith.spzx.product.service;

import com.zenith.spzx.model.entity.product.ProductSku;

import java.util.List;

public interface ProductService {
    List<ProductSku> findTopProduct(Integer topCount);
}
