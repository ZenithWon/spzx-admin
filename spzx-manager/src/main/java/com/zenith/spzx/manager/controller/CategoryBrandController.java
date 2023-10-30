package com.zenith.spzx.manager.controller;

import com.github.pagehelper.PageInfo;
import com.zenith.spzx.manager.service.CategoryBrandService;
import com.zenith.spzx.model.dto.product.CategoryBrandDto;
import com.zenith.spzx.model.entity.product.Category;
import com.zenith.spzx.model.entity.product.CategoryBrand;
import com.zenith.spzx.model.vo.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/admin/product/categoryBrand")
public class CategoryBrandController {

    @Autowired
    private CategoryBrandService categoryBrandService ;

    @GetMapping("/{page}/{limit}")
    @Operation(summary = "Get category brand with page limit")
    public Result<PageInfo<CategoryBrand>> findByPage(@PathVariable Integer page, @PathVariable Integer limit, CategoryBrandDto CategoryBrandDto) {
        PageInfo<CategoryBrand> pageInfo = categoryBrandService.findByPage(page, limit, CategoryBrandDto);
        return Result.success(pageInfo) ;
    }

    @PostMapping
    @Operation(summary = "Insert a new brandCategory relation")
    public  Result<Object> saveCategoryBrand(@RequestBody CategoryBrand categoryBrand){
        categoryBrandService.save(categoryBrand);
        return Result.success(null);
    }

    @PutMapping
    @Operation(summary = "Edit a category brand relation")
    public Result<Object> edit(@RequestBody CategoryBrand categoryBrand){
        categoryBrandService.edit(categoryBrand);
        return Result.success(null);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete by id")
    public  Result<Object> delete(@PathVariable Long id){
        categoryBrandService.delete(id);
        return Result.success(null);
    }
}
