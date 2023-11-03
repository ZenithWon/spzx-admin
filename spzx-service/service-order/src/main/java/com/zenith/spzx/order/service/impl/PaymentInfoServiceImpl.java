package com.zenith.spzx.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zenith.spzx.common.exception.MyException;
import com.zenith.spzx.model.entity.order.OrderInfo;
import com.zenith.spzx.model.entity.order.OrderItem;
import com.zenith.spzx.model.entity.pay.PaymentInfo;
import com.zenith.spzx.model.vo.common.ResultCodeEnum;
import com.zenith.spzx.order.feign.ProductClient;
import com.zenith.spzx.order.mapper.PaymentInfoMapper;
import com.zenith.spzx.order.service.OrderInfoService;
import com.zenith.spzx.order.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

@Service
@Slf4j
public class PaymentInfoServiceImpl implements PaymentService {
    @Autowired
    private PaymentInfoMapper paymentInfoMapper;
    @Autowired
    private OrderInfoService orderInfoService;
    @Autowired
    private ProductClient productClient;

    public PaymentInfo savePaymentInfo(String orderNo){
        PaymentInfo dbPaymentInfo=paymentInfoMapper.selectOne(
                new LambdaQueryWrapper<PaymentInfo>().eq(PaymentInfo::getOrderNo,orderNo)
        );

        if(dbPaymentInfo!=null){
            throw new MyException(ResultCodeEnum.PAY_ERROR);
        }

        OrderInfo orderInfo= orderInfoService.getOrderByNo(orderNo);

        PaymentInfo paymentInfo=new PaymentInfo();
        paymentInfo.setUserId(orderInfo.getUserId());
        paymentInfo.setPayType(orderInfo.getPayType());
        String content = "";
        for(OrderItem item : orderInfo.getOrderItemList()) {
            content += item.getSkuName() + " ";
        }
        paymentInfo.setContent(content);
        paymentInfo.setAmount(orderInfo.getTotalAmount());
        paymentInfo.setOrderNo(orderNo);
        paymentInfo.setPaymentStatus(0);

        paymentInfoMapper.insert(paymentInfo);

        return paymentInfoMapper.selectById(paymentInfo.getId());

    }

    @Override
    @Transactional
    public void updatePaymentStatus(Map<String, String> paramMap , int i) {
        String orderNo=paramMap.get("out_trade_no");
        PaymentInfo paymentInfo = paymentInfoMapper.selectOne(
                new LambdaQueryWrapper<PaymentInfo>().eq(PaymentInfo::getOrderNo , orderNo)
        );
        paymentInfo.setPaymentStatus(1);
        paymentInfo.setOutTradeNo(orderNo);
        paymentInfo.setCallbackTime(new Date());
        paymentInfo.setCallbackContent(JSON.toJSONString(paramMap));

        paymentInfoMapper.updateById(paymentInfo);

        OrderInfo orderInfo = orderInfoService.getOrderByNo(orderNo);
        orderInfo.setOrderStatus(1);
        orderInfo.setPaymentTime(new Date());
        orderInfo.setPayType(2);
        orderInfoService.updateOrderInfo(orderInfo);

        productClient.updateAfterPay(orderInfo.getOrderItemList());


    }

}
