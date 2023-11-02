package com.zenith.spzx.order.service;

import com.github.pagehelper.PageInfo;
import com.zenith.spzx.model.dto.h5.OrderInfoDto;
import com.zenith.spzx.model.entity.order.OrderInfo;
import com.zenith.spzx.model.vo.h5.TradeVo;

public interface OrderInfoService {
    TradeVo getTrade();

    Long submitOrder(OrderInfoDto orderInfoDto);

    OrderInfo getOrderInfo(Long orderId);

    TradeVo buy(Long skuId);

    PageInfo<OrderInfo> findUserPage(Integer page , Integer limit , Integer orderStatus);

    OrderInfo getOrderByNo(String orderNo);
}
