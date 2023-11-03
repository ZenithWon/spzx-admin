package com.zenith.spzx.order.feign;

import com.zenith.spzx.model.entity.order.OrderItem;
import com.zenith.spzx.model.entity.product.ProductSku;
import com.zenith.spzx.model.vo.common.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "service-product")
public interface ProductClient {
    @GetMapping("/api/product/sku/{skuId}")
    public Result<ProductSku> getSkuById(@PathVariable Long skuId);

    @PostMapping("/api/product/paySuccess/update")
    public Result<Object> updateAfterPay(@RequestBody List<OrderItem> orderItemList);
}
