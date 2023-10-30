package com.zenith.spzx.manager.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zenith.spzx.common.exception.MyException;
import com.zenith.spzx.manager.mapper.BrandMapper;
import com.zenith.spzx.manager.service.BrandService;
import com.zenith.spzx.model.entity.product.Brand;
import com.zenith.spzx.model.vo.common.ResultCodeEnum;
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
    public PageInfo<Brand> findByPage(Integer page , Integer limit) {
        PageHelper.startPage(page,limit);
        List<Brand> brands = brandMapper.selectList(null);

        return new PageInfo<Brand>(brands);
    }

    @Override
    public void saveBrand(Brand brand) {
        int res = brandMapper.insert(brand);
        if(res<=0){
            throw new MyException(ResultCodeEnum.DATABASE_ERROR);
        }
    }

    @Override
    public void editBrand(Brand brand) {
        int res = brandMapper.updateById(brand);
        if(res<=0){
            throw new MyException(ResultCodeEnum.DATABASE_ERROR);
        }
    }

    @Override
    public void deleteById(Long id) {
        brandMapper.deleteById(id);
    }

    @Override
    public List<Brand> findAll() {
        return brandMapper.selectList(null);
    }
}
