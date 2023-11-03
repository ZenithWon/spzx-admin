package com.zenith.spzx.order.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.zenith.spzx.common.exception.MyException;
import com.zenith.spzx.model.entity.pay.PaymentInfo;
import com.zenith.spzx.model.vo.common.Result;
import com.zenith.spzx.model.vo.common.ResultCodeEnum;
import com.zenith.spzx.order.porperties.AlipayProperties;
import com.zenith.spzx.order.service.AlipayService;
import com.zenith.spzx.order.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/order/alipay")
public class PayController {
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private AlipayService alipayService;
    @Autowired
    private AlipayProperties alipayProperties;

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

    @RequestMapping("callback/notify")
    public String alipayNotify(@RequestParam Map<String,String> paramMap, HttpServletRequest request){
        boolean signVerified = false; //调用SDK验证签名
        try {
            signVerified = AlipaySignature.rsaCheckV1(paramMap, alipayProperties.getAlipayPublicKey(), AlipayProperties.charset, AlipayProperties.sign_type);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

        String trade_status = paramMap.get("trade_status");
        if(signVerified){
            if ("TRADE_SUCCESS".equals(trade_status) || "TRADE_FINISHED".equals(trade_status)) {
                // 正常的支付成功，我们应该更新交易记录状态
                paymentService.updatePaymentStatus(paramMap, 2);
                return "success";
            }else {
                // TODO 验签失败则记录异常日志，并在response中返回failure.
                return "failure";
            }
        }
        return "failure";
    }

}
