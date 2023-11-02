package com.zenith.spzx.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.zenith.spzx.model.constant.RedisPathConstant;
import com.zenith.spzx.model.entity.h5.CartInfo;
import com.zenith.spzx.model.entity.product.ProductSku;
import com.zenith.spzx.model.vo.common.Result;
import com.zenith.spzx.order.feign.ProductClient;
import com.zenith.spzx.order.service.CartService;
import com.zenith.spzx.utils.AuthContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class CartServiceImpl implements CartService {
    @Autowired
    private ProductClient productClient;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public void addToCart(Long skuId , Integer skuNum) {
        Long userId = AuthContextUtil.getUserInfo().getId();
        String cartKey = RedisPathConstant.USER_CART+userId.toString();
        if(skuNum.equals(0)){
            return;
        }

        Object cartInfoJson = redisTemplate.opsForHash().get(cartKey , String.valueOf(skuId));
        CartInfo cartInfo=null;
        if(cartInfoJson==null){
            ProductSku productSku = productClient.getSkuById(skuId).getData();
            cartInfo=new CartInfo();
            cartInfo.setUserId(userId);
            cartInfo.setSkuId(skuId);
            cartInfo.setCartPrice(productSku.getCostPrice());
            cartInfo.setSkuNum(skuNum);
            cartInfo.setImgUrl(productSku.getThumbImg());
            cartInfo.setSkuName(productSku.getSkuName());
            cartInfo.setIsChecked(1);
            cartInfo.setCreateTime(new Date());
            cartInfo.setUpdateTime(new Date());
            redisTemplate.opsForHash().put(cartKey,String.valueOf(skuId),JSON.toJSONString(cartInfo));
        }else{
            cartInfo= JSON.parseObject(cartInfoJson.toString(),CartInfo.class);
            Integer updateSkuNum=cartInfo.getSkuNum()+skuNum;
            if(updateSkuNum<=0){
                redisTemplate.opsForHash().delete(cartKey,String.valueOf(skuId));
            }else{
                cartInfo.setSkuNum(updateSkuNum);
                cartInfo.setIsChecked(1);
                cartInfo.setUpdateTime(new Date());
                redisTemplate.opsForHash().put(cartKey,String.valueOf(skuId),JSON.toJSONString(cartInfo));
            }
        }

    }

    @Override
    public List<CartInfo> listCart() {
        Collection<Object> cartList = getCartList();
        List<CartInfo> cartInfoList=new ArrayList<>();
        for(Object item:cartList){
            cartInfoList.add(JSON.parseObject(item.toString(),CartInfo.class));
        }
        return cartInfoList;
    }

    @Override
    public void deleteSku(Long skuId) {
        Long userId = AuthContextUtil.getUserInfo().getId();
        String cartKey = RedisPathConstant.USER_CART+userId.toString();
        redisTemplate.opsForHash().delete(cartKey,String.valueOf(skuId));
    }

    @Override
    public void updateCartCheck(Long skuId , Integer isChecked) {
        Long userId = AuthContextUtil.getUserInfo().getId();
        String cartKey = RedisPathConstant.USER_CART+userId.toString();

        Object cartInfoJson = redisTemplate.opsForHash().get(cartKey , String.valueOf(skuId));
        CartInfo cartInfo=JSON.parseObject(cartInfoJson.toString(),CartInfo.class);
        cartInfo.setIsChecked(isChecked);

        redisTemplate.opsForHash().put(cartKey,String.valueOf(skuId),JSON.toJSONString(cartInfo));
    }

    @Override
    public void updateAllCartCheck(Integer isChecked) {
        Collection<Object> cartList = getCartList();
        for(Object item:cartList){
            CartInfo cartInfo = JSON.parseObject(item.toString() , CartInfo.class);
            this.updateCartCheck(cartInfo.getSkuId(),isChecked);
        }
    }

    @Override
    public void clearCart() {
        Long userId = AuthContextUtil.getUserInfo().getId();
        String cartKey = RedisPathConstant.USER_CART+userId.toString();
        redisTemplate.delete(cartKey);

    }

    private Collection<Object> getCartList(){
        Long userId=AuthContextUtil.getUserInfo().getId();
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(RedisPathConstant.USER_CART + String.valueOf(userId));
        Collection<Object> cartInfoList = entries.values();
        return cartInfoList;
    }
}
