package com.zenith.spzx.order.controller;

import com.github.pagehelper.PageInfo;
import com.zenith.spzx.model.dto.h5.OrderInfoDto;
import com.zenith.spzx.model.entity.order.OrderInfo;
import com.zenith.spzx.model.vo.common.Result;
import com.zenith.spzx.model.vo.h5.TradeVo;
import com.zenith.spzx.order.service.OrderInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order/orderInfo")
@Tag(name = "订单管理")
public class OrderInfoController {
    @Autowired
    private OrderInfoService orderInfoService;

    @Operation(summary = "确认下单")
    @GetMapping("/auth/trade")
    public Result<TradeVo> trade() {
        TradeVo tradeVo = orderInfoService.getTrade();
        return Result.success(tradeVo);
    }

    @Operation(summary = "提交订单")
    @PostMapping("auth/submitOrder")
    public Result<Long> submitOrder(@RequestBody OrderInfoDto orderInfoDto) {
        Long orderId = orderInfoService.submitOrder(orderInfoDto);
        return Result.success(orderId);
    }

    @Operation(summary = "获取订单信息")
    @GetMapping("auth/{orderId}")
    public Result<OrderInfo> getOrderInfo(@PathVariable Long orderId){
        OrderInfo orderInfo=orderInfoService.getOrderInfo(orderId);
        return  Result.success(orderInfo);
    }

    @Operation(summary = "立即购买")
    @GetMapping("auth/buy/{skuId}")
    public Result<TradeVo> buy( @PathVariable Long skuId) {
        TradeVo tradeVo = orderInfoService.buy(skuId);
        return Result.success(tradeVo);
    }

    @Operation(summary = "获取订单分页列表")
    @GetMapping("auth/{page}/{limit}")
    public Result<PageInfo<OrderInfo>> list(@PathVariable Integer page,
                                            @PathVariable Integer limit,
                                            @RequestParam(required = false, defaultValue = "") Integer orderStatus) {
        PageInfo<OrderInfo> pageInfo = orderInfoService.findUserPage(page, limit, orderStatus);
        return Result.success(pageInfo);
    }




}
