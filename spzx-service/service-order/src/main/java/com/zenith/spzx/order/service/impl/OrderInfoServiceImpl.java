package com.zenith.spzx.order.service.impl;

import com.zenith.spzx.model.entity.h5.CartInfo;
import com.zenith.spzx.model.entity.order.OrderItem;
import com.zenith.spzx.model.vo.h5.TradeVo;
import com.zenith.spzx.order.service.CartService;
import com.zenith.spzx.order.service.OrderInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class OrderInfoServiceImpl implements OrderInfoService {
    @Autowired
    private CartService cartService;

    @Override
    public TradeVo getTrade() {
        List<CartInfo> cartInfoList = cartService.listCart();
        BigDecimal totalAmount=BigDecimal.valueOf(0);
        List<OrderItem> orderItemList=new ArrayList<>();
        for (CartInfo cartInfo:cartInfoList){
            if(cartInfo.getIsChecked()==0){
                continue;
            }
            OrderItem orderItem=new OrderItem();
            orderItem.setSkuId(cartInfo.getSkuId());
            orderItem.setSkuName(cartInfo.getSkuName());
            orderItem.setSkuNum(cartInfo.getSkuNum());
            orderItem.setSkuPrice(cartInfo.getCartPrice());
            orderItem.setThumbImg(cartInfo.getImgUrl());
            orderItemList.add(orderItem);

            totalAmount=totalAmount.add(cartInfo.getCartPrice().multiply(BigDecimal.valueOf(cartInfo.getSkuNum())));
        }

        TradeVo vo=new TradeVo();
        vo.setTotalAmount(totalAmount);
        vo.setOrderItemList(orderItemList);

        return vo;
    }
}
