package com.zenith.spzx.manager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zenith.spzx.model.dto.product.CategoryBrandDto;
import com.zenith.spzx.model.entity.product.CategoryBrand;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryBrandMapper extends BaseMapper<CategoryBrand> {
    List<CategoryBrand> findByBrandOrCategory(CategoryBrandDto dto);
}
