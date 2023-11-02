package com.zenith.spzx.product.service.impl;

import cn.hutool.json.JSONArray;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zenith.spzx.common.exception.MyException;
import com.zenith.spzx.model.dto.h5.ProductSkuDto;
import com.zenith.spzx.model.entity.product.Product;
import com.zenith.spzx.model.entity.product.ProductDetails;
import com.zenith.spzx.model.entity.product.ProductSku;
import com.zenith.spzx.model.vo.common.ResultCodeEnum;
import com.zenith.spzx.model.vo.h5.ProductItemVo;
import com.zenith.spzx.product.mapper.ProductDetailsMapper;
import com.zenith.spzx.product.mapper.ProductMapper;
import com.zenith.spzx.product.mapper.ProductSkuMapper;
import com.zenith.spzx.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductSkuMapper productSkuMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductDetailsMapper productDetailsMapper;

    @Override
    public List<ProductSku> findTopProduct(Integer topCount) {
        List<ProductSku> productSkuList = productSkuMapper.selectList(
                new LambdaQueryWrapper<ProductSku>()
                        .eq(ProductSku::getStatus,1)
                        .orderByDesc(ProductSku::getSaleNum)
        );
        return productSkuList.subList(0,topCount);
    }

    @Override
    public PageInfo<ProductSku> findByPage(Integer page , Integer limit , ProductSkuDto productSkuDto) {
        PageHelper.startPage(page,limit);
        if(productSkuDto==null){
            return new PageInfo<ProductSku>(productSkuMapper.selectList(
                    new LambdaQueryWrapper<ProductSku>().eq(ProductSku::getStatus,1)
            ));
        }
        Integer order=productSkuDto.getOrder();
        productSkuDto.setMethod("asc");
        if(order.equals(1)){
            productSkuDto.setField("sale_num");
            productSkuDto.setMethod("desc");
        }else{
            productSkuDto.setField("sale_price");
            if(order.equals(3)){
                productSkuDto.setMethod("desc");
            }
        }
        PageHelper.startPage(page,limit);
        List<ProductSku> productSkuList=productSkuMapper.queryByDto(productSkuDto);
        return new PageInfo<ProductSku>(productSkuList);
    }

    @Override
    public ProductItemVo getItem(Long skuId) {
        ProductItemVo vo=new ProductItemVo();
        ProductSku sku=productSkuMapper.selectById(skuId);
        if(sku==null||!sku.getStatus().equals(1)){
            throw new MyException(ResultCodeEnum.DATABASE_ERROR);
        }
        Long productId=sku.getProductId();

        Product product=productMapper.selectById(productId);
        ProductDetails details=productDetailsMapper.selectOne(
                new LambdaQueryWrapper<ProductDetails>().eq(ProductDetails::getProductId,productId)
        );
        Map<String,Object> skuSpecValueMap=new HashMap<>();
        List<ProductSku> productSkuList = productSkuMapper.selectList(
                new LambdaQueryWrapper<ProductSku>()
                        .eq(ProductSku::getProductId , productId)
                        .eq(ProductSku::getStatus , 1)
        );
        productSkuList.stream().forEach(item->{
            skuSpecValueMap.put(item.getSkuSpec(),item.getId());
        });

        vo.setProduct(product);
        vo.setProductSku(sku);
        vo.setSkuSpecValueMap(skuSpecValueMap);
        vo.setDetailsImageUrlList(Arrays.asList(details.getImageUrls().split(",")));
        vo.setSliderUrlList(Arrays.asList(product.getSliderUrls().split(",")));
        vo.setSpecValueList(JSON.parseArray(product.getSpecValue()));



        return vo;
    }

    @Override
    public ProductSku getSkuById(Long skuId) {
        return productSkuMapper.selectById(skuId);
    }
}
