package com.zenith.spzx.manager.controller;

import com.zenith.spzx.manager.service.CategoryService;
import com.zenith.spzx.model.entity.product.Category;
import com.zenith.spzx.model.vo.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/product/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/{parentId}")
    @Operation(summary = "Find categories by parent id, lazy loading")
    public Result<List<Category>> findByPaerntId(@PathVariable Long parentId){
        List<Category> categories=categoryService.findByPaerntId(parentId);
        return Result.success(categories);
    }
}
