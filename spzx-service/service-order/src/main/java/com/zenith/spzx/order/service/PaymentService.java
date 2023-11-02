package com.zenith.spzx.order.service;

import com.zenith.spzx.model.entity.pay.PaymentInfo;

public interface PaymentService {
    public PaymentInfo savePaymentInfo(String orderNo);

}
