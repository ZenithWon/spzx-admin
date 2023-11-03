package com.zenith.spzx.order.service;

import com.zenith.spzx.model.entity.h5.CartInfo;

import java.util.List;

public interface CartService {
    void addToCart(Long skuId , Integer skuNum);

    List<CartInfo> listCart();

    void deleteSku(Long skuId);

    void updateCartCheck(Long skuId , Integer isChecked);

    void updateAllCartCheck(Integer isChecked);

    void clearCart();
}
