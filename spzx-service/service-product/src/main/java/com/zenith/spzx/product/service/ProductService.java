package com.zenith.spzx.product.service;

import com.github.pagehelper.PageInfo;
import com.zenith.spzx.model.dto.h5.ProductSkuDto;
import com.zenith.spzx.model.entity.product.ProductSku;
import com.zenith.spzx.model.vo.h5.ProductItemVo;

import java.util.List;

public interface ProductService {
    List<ProductSku> findTopProduct(Integer topCount);

    PageInfo<ProductSku> findByPage(Integer page , Integer limit , ProductSkuDto productSkuDto);

    ProductItemVo getItem(Long skuId);

    ProductSku getSkuById(Long skuId);
}
