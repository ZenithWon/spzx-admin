package com.zenith.spzx.product.service.impl;

import com.zenith.spzx.model.entity.product.Brand;
import com.zenith.spzx.product.mapper.BrandMapper;
import com.zenith.spzx.product.service.BrandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandMapper brandMapper;

    @Override
    public List<Brand> fidnAll() {
        return brandMapper.selectList(null);
    }
}
