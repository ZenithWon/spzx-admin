package com.zenith.spzx.product.controller;

import com.zenith.spzx.model.entity.product.Brand;
import com.zenith.spzx.model.vo.common.Result;
import com.zenith.spzx.product.service.BrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/product/brand")
@Tag(name = "品牌管理")
public class BrandController {
    @Autowired
    private BrandService brandService;

    @GetMapping("/findAll")
    @Operation(summary = "获取全部品牌")
    public Result<List<Brand>> findAll(){
        List<Brand> brandList=brandService.fidnAll();
        return Result.success(brandList);
    }
}
