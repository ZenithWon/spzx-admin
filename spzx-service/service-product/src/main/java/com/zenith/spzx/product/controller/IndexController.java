package com.zenith.spzx.product.controller;

import com.zenith.spzx.model.vo.common.Result;
import com.zenith.spzx.model.vo.common.ResultCodeEnum;
import com.zenith.spzx.model.vo.h5.IndexVo;
import com.zenith.spzx.product.service.CategoryService;
import com.zenith.spzx.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "首页接口管理")
@RestController
@RequestMapping(value="/api/product/index")
public class IndexController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;

    @GetMapping
    @Operation(summary = "Get index page info, including categories and top products")
    public Result<IndexVo> getIndexInfo(){
        IndexVo indexVo=new IndexVo();
        indexVo.setCategoryList(categoryService.findCategoryByParent(0L));
        indexVo.setProductSkuList(productService.findTopProduct(10));
        return Result.success(indexVo);
    }
}

