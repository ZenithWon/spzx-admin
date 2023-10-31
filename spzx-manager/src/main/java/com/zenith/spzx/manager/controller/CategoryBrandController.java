package com.zenith.spzx.manager.controller;

import com.github.pagehelper.PageInfo;
import com.zenith.spzx.common.log.annotation.Log;
import com.zenith.spzx.common.log.enums.OperationType;
import com.zenith.spzx.manager.service.CategoryBrandService;
import com.zenith.spzx.model.dto.product.CategoryBrandDto;
import com.zenith.spzx.model.entity.product.Brand;
import com.zenith.spzx.model.entity.product.Category;
import com.zenith.spzx.model.entity.product.CategoryBrand;
import com.zenith.spzx.model.vo.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/admin/product/categoryBrand")
public class CategoryBrandController {

    @Autowired
    private CategoryBrandService categoryBrandService ;

    @GetMapping("/{page}/{limit}")
    @Operation(summary = "Get category brand with page limit")
    @Log(title = "categoryBrand:list",businessType = OperationType.OTHER)
    public Result<PageInfo<CategoryBrand>> findByPage(@PathVariable Integer page, @PathVariable Integer limit, CategoryBrandDto CategoryBrandDto) {
        PageInfo<CategoryBrand> pageInfo = categoryBrandService.findByPage(page, limit, CategoryBrandDto);
        return Result.success(pageInfo) ;
    }

    @PostMapping
    @Operation(summary = "Insert a new brandCategory relation")
    @Log(title = "categoryBrand:insert",businessType = OperationType.INSERT)
    public  Result<Object> saveCategoryBrand(@RequestBody CategoryBrand categoryBrand){
        categoryBrandService.save(categoryBrand);
        return Result.success(null);
    }

    @PutMapping
    @Operation(summary = "Edit a category brand relation")
    @Log(title = "categoryBrand:edit",businessType = OperationType.UPDATE)
    public Result<Object> edit(@RequestBody CategoryBrand categoryBrand){
        categoryBrandService.edit(categoryBrand);
        return Result.success(null);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete by id")
    @Log(title = "categoryBrand:delete",businessType = OperationType.DELETE)
    public  Result<Object> delete(@PathVariable Long id){
        categoryBrandService.delete(id);
        return Result.success(null);
    }

    @GetMapping("/getBrand/{categoryId}")
    @Operation(summary = "Get brand by category id")
    @Log(title = "categoryBrand:list",businessType = OperationType.OTHER)
    public Result<List<Brand>> getBrand(@PathVariable Long categoryId){
        List<Brand> brands= categoryBrandService.getBrandByCategoryId(categoryId);
        return Result.success(brands);
    }
}
