package com.zenith.spzx.manager.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zenith.spzx.manager.mapper.OrderStatisticsMapper;
import com.zenith.spzx.manager.service.OrderInfoService;
import com.zenith.spzx.model.dto.order.OrderStatisticsDto;
import com.zenith.spzx.model.entity.order.OrderStatistics;
import com.zenith.spzx.model.vo.order.OrderStatisticsVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class OrderInfoServiceImpl implements OrderInfoService {

    @Autowired
    private OrderStatisticsMapper orderStatisticsMapper;

    @Override
    public OrderStatisticsVo getOrderStatisticsData(OrderStatisticsDto orderStatisticsDto) {
        OrderStatisticsVo orderStatisticsVo=new OrderStatisticsVo();
        String begin = orderStatisticsDto.getCreateTimeBegin();
        String end = orderStatisticsDto.getCreateTimeEnd();

        List<OrderStatistics> orderStatisticsList = orderStatisticsMapper.selectList(
                new LambdaQueryWrapper<OrderStatistics>().
                        ge(!StringUtils.isEmpty(begin) , OrderStatistics::getOrderDate , begin)
                        .le(!StringUtils.isEmpty(end) , OrderStatistics::getOrderDate , end)
        );

        List<String> dateList=new ArrayList<>();
        List<BigDecimal> totalAmount=new ArrayList<>();

        for(OrderStatistics item:orderStatisticsList){
            totalAmount.add(item.getTotalAmount());
            dateList.add(DateUtil.format(item.getOrderDate(),"yyyy-MM-dd"));
        }
        orderStatisticsVo.setDateList(dateList);
        orderStatisticsVo.setAmountList(totalAmount);
        return orderStatisticsVo;
    }
}
