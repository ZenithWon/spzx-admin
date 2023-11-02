package com.zenith.spzx.order.controller;

import com.zenith.spzx.model.entity.pay.PaymentInfo;
import com.zenith.spzx.model.vo.common.Result;
import com.zenith.spzx.order.service.AlipayService;
import com.zenith.spzx.order.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order/alipay")
public class PayController {
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private AlipayService alipayService;

    @GetMapping("/auth/savePayment/{orderNo}")
    public Result<Object> test(@PathVariable String orderNo){
        PaymentInfo paymentInfo= paymentService.savePaymentInfo(orderNo);

        return Result.success(paymentInfo);
    }

    @GetMapping("submitAlipay/{orderNo}")
    public Result<String> submitAlipay(@PathVariable(value = "orderNo") String orderNo) {
        String form = alipayService.submitAlipay(orderNo);
        return Result.success(form);
    }
}
