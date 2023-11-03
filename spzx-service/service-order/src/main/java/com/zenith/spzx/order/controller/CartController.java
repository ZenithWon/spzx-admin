package com.zenith.spzx.order.controller;

import com.zenith.spzx.model.entity.h5.CartInfo;
import com.zenith.spzx.model.vo.common.Result;
import com.zenith.spzx.order.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/order/cart")
@Tag(name="购物车相关接口")
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping("/auth/addToCart/{skuId}/{skuNum}")
    @Operation(summary = "添加购物车或者修改购物车商品数量")
    public Result<Object> addToCart(@PathVariable Long skuId,@PathVariable Integer skuNum){
        cartService.addToCart(skuId,skuNum);
        return Result.success(null);
    }

    @GetMapping("/auth/cartList")
    @Operation(summary = "查询购物车")
    public Result<List<CartInfo>> listCart(){
        List<CartInfo> cartInfoList= cartService.listCart();
        return Result.success(cartInfoList);
    }

    @Operation(summary = "删除购物车商品")
    @DeleteMapping("auth/deleteCart/{skuId}")
    public Result<Object> deleteSku(@PathVariable Long skuId){
        cartService.deleteSku(skuId);
        return Result.success(null);
    }

    @Operation(summary="更新购物车商品选中状态")
    @GetMapping("/auth/checkCart/{skuId}/{isChecked}")
    public Result checkCart( @PathVariable Long skuId, @PathVariable Integer isChecked) {
        cartService.updateCartCheck(skuId, isChecked);
        return Result.success(null);
    }

    @Operation(summary="更新购物车商品全部选中状态")
    @GetMapping("/auth/allCheckCart/{isChecked}")
    public Result allCheckCart(@PathVariable Integer isChecked){
        cartService.updateAllCartCheck(isChecked);
        return Result.success(null);
    }

    @Operation(summary="清空购物车")
    @GetMapping("/auth/clearCart")
    public Result clearCart(){
        cartService.clearCart();
        return Result.success(null);
    }
}
