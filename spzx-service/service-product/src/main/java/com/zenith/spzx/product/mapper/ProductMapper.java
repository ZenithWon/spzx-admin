package com.zenith.spzx.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zenith.spzx.model.entity.product.Product;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {
}
