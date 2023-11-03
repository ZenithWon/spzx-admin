package com.zenith.spzx.order.service;

import com.zenith.spzx.model.entity.pay.PaymentInfo;

import java.util.Map;

public interface PaymentService {
    public PaymentInfo savePaymentInfo(String orderNo);

    void updatePaymentStatus(Map<String, String> paramMap , int i);
}
