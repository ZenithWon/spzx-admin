package com.zenith.spzx.manager.controller;

import com.zenith.spzx.common.log.annotation.Log;
import com.zenith.spzx.common.log.enums.OperationType;
import com.zenith.spzx.manager.service.CategoryService;
import com.zenith.spzx.model.entity.product.Category;
import com.zenith.spzx.model.vo.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/admin/product/category")
@Tag(name="Category Api")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/{parentId}")
    @Operation(summary = "Find categories by parent id, lazy loading")
    @Log(title = "category:list",businessType = OperationType.OTHER)
    public Result<List<Category>> findByPaerntId(@PathVariable Long parentId){
        List<Category> categories=categoryService.findByPaerntId(parentId);
        return Result.success(categories);
    }

    @PostMapping("/importData")
    @Operation(summary = "Import data with excel table")
    @Log(title = "category:import",businessType = OperationType.OTHER)
    public Result<Object> importData(MultipartFile file){
        categoryService.importData(file);
        return Result.success(null);
    }

    @GetMapping("/exportData")
    @Operation(summary = "Export category data and generate excel table")
    @Log(title = "category:export",businessType = OperationType.OTHER)
    public Result<Object> exportData(HttpServletResponse response){
        categoryService.exportData(response);
        return Result.success(null);
    }
}
