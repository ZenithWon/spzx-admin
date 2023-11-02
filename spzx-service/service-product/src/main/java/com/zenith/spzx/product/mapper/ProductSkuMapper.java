package com.zenith.spzx.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zenith.spzx.model.dto.h5.ProductSkuDto;
import com.zenith.spzx.model.entity.product.ProductSku;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductSkuMapper extends BaseMapper<ProductSku> {
    List<ProductSku> queryByDto(ProductSkuDto dto);
}
