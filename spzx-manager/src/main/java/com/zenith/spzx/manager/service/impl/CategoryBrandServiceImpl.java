package com.zenith.spzx.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zenith.spzx.common.exception.MyException;
import com.zenith.spzx.manager.mapper.BrandMapper;
import com.zenith.spzx.manager.mapper.CategoryBrandMapper;
import com.zenith.spzx.manager.mapper.CategoryMapper;
import com.zenith.spzx.manager.service.CategoryBrandService;
import com.zenith.spzx.model.dto.product.CategoryBrandDto;
import com.zenith.spzx.model.entity.product.Brand;
import com.zenith.spzx.model.entity.product.Category;
import com.zenith.spzx.model.entity.product.CategoryBrand;
import com.zenith.spzx.model.vo.common.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CategoryBrandServiceImpl implements CategoryBrandService {
    @Autowired
    private CategoryBrandMapper categoryBrandMapper;
    @Autowired
    private BrandMapper brandMapper;

    @Override
    public PageInfo<CategoryBrand> findByPage(Integer page , Integer limit , CategoryBrandDto categoryBrandDto) {
        PageHelper.startPage(page,limit);
        List<CategoryBrand> categoryBrandList = categoryBrandMapper.findByBrandOrCategory(categoryBrandDto);
        return new PageInfo<CategoryBrand>(categoryBrandList);
    }

    @Override
    public void save(CategoryBrand categoryBrand) {
        int res = categoryBrandMapper.insert(categoryBrand);
        if(res<=0){
            throw new MyException(ResultCodeEnum.DATABASE_ERROR);
        }
    }

    @Override
    public void edit(CategoryBrand categoryBrand) {
        int res = categoryBrandMapper.updateById(categoryBrand);
        if(res<=0){
            throw new MyException(ResultCodeEnum.DATABASE_ERROR);
        }
    }

    @Override
    public void delete(Long id) {
        categoryBrandMapper.deleteById(id);
    }

    @Override
    public List<Brand> getBrandByCategoryId(Long categoryId) {
        List<CategoryBrand> categoryBrandList=categoryBrandMapper.selectList(
                new LambdaQueryWrapper<CategoryBrand>().eq(CategoryBrand::getCategoryId,categoryId)
        );
        List<Brand> brandList=new ArrayList<>();
        for(CategoryBrand item:categoryBrandList){
            brandList.add(brandMapper.selectById(item.getBrandId()));
        }
        return brandList;
    }
}
