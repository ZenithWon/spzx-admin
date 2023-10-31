package com.zenith.spzx.manager.service;

import com.zenith.spzx.model.dto.order.OrderStatisticsDto;
import com.zenith.spzx.model.vo.order.OrderStatisticsVo;

public interface OrderInfoService {
    OrderStatisticsVo getOrderStatisticsData(OrderStatisticsDto orderStatisticsDto);
}
