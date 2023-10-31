package com.zenith.spzx.manager.task;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zenith.spzx.manager.mapper.OrderInfoMapper;
import com.zenith.spzx.manager.mapper.OrderStatisticsMapper;
import com.zenith.spzx.model.entity.order.OrderInfo;
import com.zenith.spzx.model.entity.order.OrderStatistics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class OrderStatisticsTask {
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private OrderStatisticsMapper orderStatisticsMapper;

    @Scheduled(cron = "0 0 2 1/1 * ?")
    public void orderTotalAmountCalculator(){
//        Date lastDay
//        String lastDay = .toString("yyyy-MM-dd");
        DateTime beginOfDay = DateUtil.beginOfDay(DateUtil.offsetDay(new Date() , -1));
        DateTime endOfDay = DateUtil.endOfDay(DateUtil.offsetDay(new Date() , -1));
        LambdaQueryWrapper<OrderInfo> wrapper=new LambdaQueryWrapper<>();
        wrapper.between(OrderInfo::getCreateTime,beginOfDay,endOfDay);
        List<OrderInfo> orderInfos = orderInfoMapper.selectList(wrapper);
        BigDecimal amount=BigDecimal.valueOf(0);

        for(OrderInfo item:orderInfos){
            amount=amount.add(item.getTotalAmount());
        }
        OrderStatistics orderStatistics=new OrderStatistics();
        orderStatistics.setOrderDate(DateUtil.offsetDay(new Date() , -1));
        orderStatistics.setTotalAmount(amount);
        orderStatistics.setTotalNum(orderInfos.size());

        orderStatisticsMapper.insert(orderStatistics);
    }

}
