package com.zenith.spzx.manager.controller;

import com.github.pagehelper.PageInfo;
import com.zenith.spzx.manager.service.BrandService;
import com.zenith.spzx.model.entity.product.Brand;
import com.zenith.spzx.model.vo.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/product/brand")
@Tag(name="Brand Api")
public class BrandController {
    @Autowired
    private BrandService brandService ;

    @GetMapping("/{page}/{limit}")
    @Operation(summary = "Get all brand info with page limit")
    public Result<PageInfo<Brand>> findByPage(@PathVariable Integer page, @PathVariable Integer limit) {
        PageInfo<Brand> pageInfo = brandService.findByPage(page, limit);
        return Result.success(pageInfo) ;
    }

    @GetMapping
    @Operation(summary = "Get all brand info")
    public Result<List<Brand>> findAll() {
        List<Brand> list = brandService.findAll();
        return Result.success(list);
    }

    @PostMapping
    @Operation(summary = "Insert a new brand")
    public Result<Object> saveBrand(@RequestBody Brand brand){
        brandService.saveBrand(brand);
        return Result.success(null);
    }

    @PutMapping
    @Operation(summary = "Edit brand info")
    public Result<Object> editBrand(@RequestBody Brand brand){
        brandService.editBrand(brand);
        return Result.success(null);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete brand by id")
    public Result<Object> deleteBrand(@PathVariable Long id){
        brandService.deleteById(id);
        return Result.success(null);
    }
}
