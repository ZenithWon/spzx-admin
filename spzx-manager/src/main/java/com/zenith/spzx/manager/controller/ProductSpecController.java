package com.zenith.spzx.manager.controller;

import com.github.pagehelper.PageInfo;
import com.zenith.spzx.manager.service.ProductSpecService;
import com.zenith.spzx.model.entity.product.ProductSpec;
import com.zenith.spzx.model.vo.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="/admin/product/productSpec")
public class ProductSpecController {

    @Autowired
    private ProductSpecService productSpecService ;

    @GetMapping("/{page}/{limit}")
    public Result<PageInfo<ProductSpec>> findByPage(@PathVariable Integer page, @PathVariable Integer limit) {
        PageInfo<ProductSpec> pageInfo = productSpecService.findByPage(page, limit);
        return Result.success(pageInfo) ;
    }

    @PostMapping
    public Result save(@RequestBody ProductSpec productSpec) {
        productSpecService.save(productSpec);
        return Result.success(null) ;
    }

    @PutMapping
    public Result updateById(@RequestBody ProductSpec productSpec) {
        productSpecService.update(productSpec);
        return Result.success(null) ;
    }

    @DeleteMapping("/{id}")
    public Result removeById(@PathVariable Long id) {
        productSpecService.deleteById(id);
        return Result.success(id);
    }

}
