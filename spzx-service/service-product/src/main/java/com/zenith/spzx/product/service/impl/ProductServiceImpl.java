package com.zenith.spzx.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zenith.spzx.model.entity.product.ProductSku;
import com.zenith.spzx.product.mapper.ProductSkuMapper;
import com.zenith.spzx.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductSkuMapper productSkuMapper;

    @Override
    public List<ProductSku> findTopProduct(Integer topCount) {
        List<ProductSku> productSkuList = productSkuMapper.selectList(
                new LambdaQueryWrapper<ProductSku>().orderByDesc(ProductSku::getSaleNum)
        );
        return productSkuList.subList(0,topCount);
    }
}
