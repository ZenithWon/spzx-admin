package com.zenith.spzx.product.controller;

import com.github.pagehelper.PageInfo;
import com.zenith.spzx.model.dto.h5.ProductSkuDto;
import com.zenith.spzx.model.entity.product.ProductSku;
import com.zenith.spzx.model.vo.common.Result;
import com.zenith.spzx.model.vo.h5.ProductItemVo;
import com.zenith.spzx.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "商品列表管理")
@RestController
@RequestMapping(value="/api/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Operation(summary = "分页查询")
    @GetMapping(value = "/{pageNo}/{pageSize}")
    public Result<PageInfo<ProductSku>> findByPage(@PathVariable Integer pageNo, @PathVariable Integer pageSize, ProductSkuDto productSkuDto) {
        PageInfo<ProductSku> pageInfo = productService.findByPage(pageNo, pageSize, productSkuDto);
        return Result.success(pageInfo) ;
    }

    @GetMapping("/item/{skuId}")
    @Operation(summary = "商品详情")
    public Result<ProductItemVo> getItemInfo(@PathVariable Long skuId){
        ProductItemVo vo=productService.getItem(skuId);
        return Result.success(vo);
    }

    @GetMapping("/sku/{skuId}")
    public Result<ProductSku> getSkuById(@PathVariable Long skuId){
        ProductSku sku=productService.getSkuById(skuId);
        return Result.success(sku);
    }
}
