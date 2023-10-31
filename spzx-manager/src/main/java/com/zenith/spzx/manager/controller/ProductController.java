package com.zenith.spzx.manager.controller;

import com.github.pagehelper.PageInfo;
import com.zenith.spzx.common.log.annotation.Log;
import com.zenith.spzx.common.log.enums.OperationType;
import com.zenith.spzx.manager.service.ProductService;
import com.zenith.spzx.model.dto.product.ProductDto;
import com.zenith.spzx.model.entity.base.ProductUnit;
import com.zenith.spzx.model.entity.product.Product;
import com.zenith.spzx.model.entity.product.ProductSku;
import com.zenith.spzx.model.vo.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="/admin/product/product")
public class ProductController {
    @Autowired
    private ProductService productService ;

    @GetMapping(value = "/{page}/{limit}")
    @Log(title = "product:list",businessType = OperationType.OTHER)
    public Result<PageInfo<Product>> findByPage(@PathVariable Integer page, @PathVariable Integer limit, ProductDto productDto) {
        PageInfo<Product> pageInfo = productService.findByPage(page, limit, productDto);
        return Result.success(pageInfo);
    }

    @GetMapping("/getUnit")
    @Log(title = "product:list",businessType = OperationType.OTHER)
    public Result<List<ProductUnit>> getProductUnit(){
        return Result.success(productService.getProductUnit());
    }

    @PostMapping
    @Log(title = "product:insert",businessType = OperationType.INSERT)
    public Result<Object> save(@RequestBody Product product){
        productService.save(product);
        return Result.success(null);
    }

    @GetMapping("/{id}")
    @Log(title = "product:list",businessType = OperationType.OTHER)
    public Result<Product> getProductById(@PathVariable Long id){
        return Result.success(productService.getById(id));
    }

    @PutMapping
    @Log(title = "product:edit",businessType = OperationType.UPDATE)
    public Result<Object> editProduct(@RequestBody Product product){
        productService.edit(product);
        return Result.success(null);
    }

    @DeleteMapping("/{id}")
    @Log(title = "product:delete",businessType = OperationType.DELETE)
    public Result<Object> delete(@PathVariable Long id){
        productService.deleteById(id);
        return Result.success(null);
    }

    @PutMapping("/auditStatus/{id}/{auditStatus}")
    @Log(title = "product:edit",businessType = OperationType.UPDATE)
    public Result<Object> upadteAuditStatus(@PathVariable Long id,@PathVariable Integer auditStatus){
        productService.updateAuditStatus(id,auditStatus);
        return Result.success(null);
    }

    @PutMapping("/status/{id}/{status}")
    @Log(title = "product:edit",businessType = OperationType.UPDATE)
    public Result<Object> upadteStatus(@PathVariable Long id,@PathVariable Integer status){
        productService.updateStatus(id,status);
        return Result.success(null);
    }

}
