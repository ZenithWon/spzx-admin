package com.zenith.spzx.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.zenith.spzx.common.exception.MyException;
import com.zenith.spzx.model.entity.pay.PaymentInfo;
import com.zenith.spzx.model.vo.common.ResultCodeEnum;
import com.zenith.spzx.order.porperties.AlipayProperties;
import com.zenith.spzx.order.service.AlipayService;
import com.zenith.spzx.order.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class AlipayServiceImpl implements AlipayService {
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private AlipayProperties alipayProperties;
    @Autowired
    private AlipayClient alipayClient;

    @Override
    public String submitAlipay(String orderNo) {
        PaymentInfo paymentInfo = paymentService.savePaymentInfo(orderNo);
        AlipayTradeWapPayRequest alipayTradeWapPayRequest=new AlipayTradeWapPayRequest();
        alipayTradeWapPayRequest.setReturnUrl(alipayProperties.getReturnPaymentUrl());
        alipayTradeWapPayRequest.setNotifyUrl(alipayProperties.getNotifyPaymentUrl());

        Map<String,Object> map=new HashMap<>();
        map.put("out_trade_no",paymentInfo.getOrderNo());
        map.put("product_code","QUICK_WAP_WAY");
        //map.put("total_amount",paymentInfo.getAmount());
        map.put("total_amount",new BigDecimal("0.01"));
        map.put("subject",paymentInfo.getContent());
        alipayTradeWapPayRequest.setBizContent(JSON.toJSONString(map));

        try {
            AlipayTradeWapPayResponse response = alipayClient.pageExecute(alipayTradeWapPayRequest);
            if(response.isSuccess()){
                return response.getBody();
            }else{
                throw new MyException(ResultCodeEnum.PAY_ERROR);
            }
        } catch (AlipayApiException e) {
            throw new MyException(ResultCodeEnum.PAY_ERROR);
        }

    }
}
