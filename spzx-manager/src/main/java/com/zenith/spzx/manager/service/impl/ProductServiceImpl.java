package com.zenith.spzx.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zenith.spzx.common.exception.MyException;
import com.zenith.spzx.manager.mapper.*;
import com.zenith.spzx.manager.service.ProductService;
import com.zenith.spzx.model.dto.product.ProductDto;
import com.zenith.spzx.model.entity.base.ProductUnit;
import com.zenith.spzx.model.entity.product.Product;
import com.zenith.spzx.model.entity.product.ProductDetails;
import com.zenith.spzx.model.entity.product.ProductSku;
import com.zenith.spzx.model.vo.common.ResultCodeEnum;
import com.zenith.spzx.utils.UUIDUitils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductUnitMapper productUnitMapper;
    @Autowired
    private ProductDetailsMapper productDetailsMapper;
    @Autowired
    private ProductSkuMapper productSkuMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private BrandMapper brandMapper;


    @Override
    public PageInfo<Product> findByPage(Integer page , Integer limit , ProductDto productDto) {
        PageHelper.startPage(page,limit);
        List<Product> products=productMapper.findByCategoryOrBrand(productDto);
        return new PageInfo<Product>(products);
    }

    @Override
    public List<ProductUnit> getProductUnit() {
        return productUnitMapper.selectList(null);
    }

    @Override
    @Transactional
    public void save(Product product) {
        product.setStatus(0);
        product.setAuditStatus(0);
        if(productMapper.insert(product)<=0){
            throw new MyException(ResultCodeEnum.DATABASE_ERROR);
        }

        List<ProductSku> productSkuList = product.getProductSkuList();
        int index=0;
        for(ProductSku sku:productSkuList){
            sku.setProductId(product.getId());
            sku.setSkuCode(product.getId()+"_"+index);
            sku.setSkuName(product.getName()+sku.getSkuSpec());
            sku.setSaleNum(0);
            sku.setStatus(0);
            if(productSkuMapper.insert(sku)<=0){
                throw new MyException(ResultCodeEnum.DATABASE_ERROR);
            }
            index++;
        }

        ProductDetails productDetails=new ProductDetails();
        productDetails.setImageUrls(product.getSliderUrls());
        productDetails.setProductId(product.getId());
        if(productDetailsMapper.insert(productDetails)<=0){
            throw new MyException(ResultCodeEnum.DATABASE_ERROR);
        }
    }

    @Override
    public Product getById(Long id) {
        Product product=productMapper.selectById(id);
        if(product==null){
            return  null;
        }
        product.setBrandName(brandMapper.selectById(product.getBrandId()).getName());
        product.setCategory1Name(categoryMapper.selectById(product.getCategory1Id()).getName());
        product.setCategory2Name(categoryMapper.selectById(product.getCategory2Id()).getName());
        product.setCategory3Name(categoryMapper.selectById(product.getCategory3Id()).getName());

        List<ProductSku> productSkuList = productSkuMapper.selectList(
                new LambdaQueryWrapper<ProductSku>().eq(ProductSku::getProductId , id)
        );
        product.setProductSkuList(productSkuList);

        ProductDetails productDetails = productDetailsMapper.selectOne(
                new LambdaQueryWrapper<ProductDetails>().eq(ProductDetails::getProductId , id)
        );
        product.setDetailsImageUrls(productDetails.getImageUrls());

        return product;
    }

    @Override
    @Transactional
    public void edit(Product product) {
        if(productMapper.updateById(product)<=0){
            throw new MyException(ResultCodeEnum.DATABASE_ERROR);
        }

        List<ProductSku> productSkuList = product.getProductSkuList();
        for(ProductSku item:productSkuList){
            if(productSkuMapper.updateById(item)<=0){
                throw new MyException(ResultCodeEnum.DATABASE_ERROR);
            }
        }

        ProductDetails productDetails=productDetailsMapper.selectOne(
                new LambdaQueryWrapper<ProductDetails>().eq(ProductDetails::getProductId,product.getId())
        );
        productDetails.setImageUrls(product.getDetailsImageUrls());
        if(productDetailsMapper.updateById(productDetails)<=0){
            throw new MyException(ResultCodeEnum.DATABASE_ERROR);
        }

    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        productMapper.deleteById(id);

        productSkuMapper.delete(
                new LambdaQueryWrapper<ProductSku>().eq(ProductSku::getProductId,id)
        );

        productDetailsMapper.delete(
                new LambdaQueryWrapper<ProductDetails>().eq(ProductDetails::getProductId,id)
        );
    }

    @Override
    public void updateAuditStatus(Long id , Integer auditStatus) {
        if(!auditStatus.equals(1)&&!auditStatus.equals(-1)&&auditStatus.equals(0)){
            throw new MyException(ResultCodeEnum.ILLEGAL_REQUEST);
        }

        Product product = productMapper.selectById(id);
        if(product==null){
            throw new MyException(ResultCodeEnum.DATA_ERROR);
        }
        if(auditStatus.equals(1)){
            product.setAuditMessage("审批通过");
            product.setAuditStatus(auditStatus);
        }

        if(auditStatus.equals(-1)){
            product.setAuditMessage("审批不通过");
            product.setAuditStatus(auditStatus);
        }

        productMapper.updateById(product);
    }

    @Override
    public void updateStatus(Long id , Integer status) {
        if(!status.equals(1)&&!status.equals(-1)&&status.equals(0)){
            throw new MyException(ResultCodeEnum.ILLEGAL_REQUEST);
        }

        Product product = productMapper.selectById(id);
        if(product==null){
            throw new MyException(ResultCodeEnum.DATA_ERROR);
        }

        product.setStatus(status);
        productMapper.updateById(product);
    }
}
