package com.zenith.spzx.order.feign;

import com.zenith.spzx.model.entity.product.ProductSku;
import com.zenith.spzx.model.vo.common.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "service-product")
public interface ProductClient {
    @GetMapping("/api/product/sku/{skuId}")
    public Result<ProductSku> getSkuById(@PathVariable Long skuId);
}
