package com.zenith.spzx.order.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zenith.spzx.common.exception.MyException;
import com.zenith.spzx.model.constant.RedisPathConstant;
import com.zenith.spzx.model.dto.h5.OrderInfoDto;
import com.zenith.spzx.model.entity.h5.CartInfo;
import com.zenith.spzx.model.entity.order.OrderInfo;
import com.zenith.spzx.model.entity.order.OrderItem;
import com.zenith.spzx.model.entity.order.OrderLog;
import com.zenith.spzx.model.entity.product.ProductSku;
import com.zenith.spzx.model.entity.user.UserAddress;
import com.zenith.spzx.model.entity.user.UserInfo;
import com.zenith.spzx.model.vo.common.ResultCodeEnum;
import com.zenith.spzx.model.vo.h5.TradeVo;
import com.zenith.spzx.order.feign.ProductClient;
import com.zenith.spzx.order.feign.UserClient;
import com.zenith.spzx.order.mapper.OrderInfoMapper;
import com.zenith.spzx.order.mapper.OrderItemMapper;
import com.zenith.spzx.order.mapper.OrderLogMapper;
import com.zenith.spzx.order.service.CartService;
import com.zenith.spzx.order.service.OrderInfoService;
import com.zenith.spzx.utils.AuthContextUtil;
import com.zenith.spzx.utils.UUIDUitils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class OrderInfoServiceImpl implements OrderInfoService {
    @Autowired
    private CartService cartService;
    @Autowired
    private ProductClient productClient;
    @Autowired
    private UserClient userClient;
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private OrderLogMapper orderLogMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

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

    @Override
    @Transactional
    public Long submitOrder(OrderInfoDto orderInfoDto) {
        List<OrderItem> orderItemList = orderInfoDto.getOrderItemList();
        if(CollectionUtil.isEmpty(orderItemList)){
            throw new MyException(ResultCodeEnum.DATA_ERROR);
        }

        BigDecimal totalAmount=BigDecimal.valueOf(0);
        for(OrderItem item:orderItemList){
            ProductSku sku = productClient.getSkuById(item.getSkuId()).getData();
            if(item.getSkuNum()>sku.getStockNum()){
                throw new MyException(ResultCodeEnum.STOCK_LESS);
            }
            totalAmount=totalAmount.add(item.getSkuPrice().multiply(BigDecimal.valueOf(item.getSkuNum())));
        }

        UserInfo userInfo = AuthContextUtil.getUserInfo();
        UserAddress userAddress=userClient.getUserAddress(orderInfoDto.getUserAddressId()).getData();
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderNo(UUIDUitils.generateKey());
        orderInfo.setUserId(userInfo.getId());
        orderInfo.setNickName(userInfo.getNickName());
        orderInfo.setReceiverName(userAddress.getName());
        orderInfo.setReceiverPhone(userAddress.getPhone());
        orderInfo.setReceiverTagName(userAddress.getTagName());
        orderInfo.setReceiverProvince(userAddress.getProvinceCode());
        orderInfo.setReceiverCity(userAddress.getCityCode());
        orderInfo.setReceiverDistrict(userAddress.getDistrictCode());
        orderInfo.setReceiverAddress(userAddress.getFullAddress());
        orderInfo.setTotalAmount(totalAmount);
        orderInfo.setCouponAmount(new BigDecimal(0));
        orderInfo.setOriginalTotalAmount(totalAmount);
        orderInfo.setFeightFee(orderInfoDto.getFeightFee());
        orderInfo.setPayType(2);
        orderInfo.setOrderStatus(0);

        orderInfoMapper.insert(orderInfo);

        String cartKey = RedisPathConstant.USER_CART+userInfo.getId().toString();
        orderItemList.stream().forEach(item->{
            item.setOrderId(orderInfo.getId());
            orderItemMapper.insert(item);
            redisTemplate.opsForHash().delete(cartKey,String.valueOf(item.getSkuId()));
        });

        OrderLog orderLog = new OrderLog();
        orderLog.setOrderId(orderInfo.getId());
        orderLog.setProcessStatus(0);
        orderLog.setNote("提交订单");
        orderLogMapper.insert(orderLog);

        return orderInfo.getId();
    }

    @Override
    public OrderInfo getOrderInfo(Long orderId) {
        return orderInfoMapper.selectById(orderId);
    }

    @Override
    public TradeVo buy(Long skuId) {
        ProductSku sku=productClient.getSkuById(skuId).getData();

        List<OrderItem> orderItemList=new ArrayList<>();
        OrderItem orderItem=new OrderItem();
        orderItem.setSkuId(skuId);
        orderItem.setSkuName(sku.getSkuName());
        orderItem.setSkuNum(1);
        orderItem.setSkuPrice(sku.getSalePrice());
        orderItem.setThumbImg(sku.getThumbImg());
        orderItemList.add(orderItem);


        TradeVo vo=new TradeVo();
        vo.setTotalAmount(sku.getSalePrice());
        vo.setOrderItemList(orderItemList);

        return vo;
    }

    @Override
    public PageInfo<OrderInfo> findUserPage(Integer page , Integer limit , Integer orderStatus) {
        PageHelper.startPage(page,limit);
        List<OrderInfo> orderInfoList = orderInfoMapper.selectList(
                new LambdaQueryWrapper<OrderInfo>()
                        .eq(orderStatus != null , OrderInfo::getOrderStatus , orderStatus)
                        .eq(OrderInfo::getUserId,AuthContextUtil.getUserInfo().getId())
        );

        for(OrderInfo orderInfo:orderInfoList){
            List<OrderItem> orderItemList = orderItemMapper.selectList(
                    new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId , orderInfo.getId())
            );
            orderInfo.setOrderItemList(orderItemList);
        }
        return new PageInfo<OrderInfo>(orderInfoList);
    }

    @Override
    public OrderInfo getOrderByNo(String orderNo) {
        OrderInfo orderInfo = orderInfoMapper.selectOne(
                new LambdaQueryWrapper<OrderInfo>().eq(OrderInfo::getOrderNo , orderNo)
        );

        List<OrderItem> orderItemList = orderItemMapper.selectList(
                new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId , orderInfo.getId())
        );
        orderInfo.setOrderItemList(orderItemList);
        return orderInfo;
    }
}
