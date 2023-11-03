package com.zenith.spzx.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zenith.spzx.model.constant.RedisPathConstant;
import com.zenith.spzx.model.entity.product.Category;
import com.zenith.spzx.product.mapper.CategoryMapper;
import com.zenith.spzx.product.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    @Cacheable(value = RedisPathConstant.CATEGORY_NODE,key = "#parentId")
    public List<Category> findCategoryByParent(Long parentId) {
        List<Category> categoryList= categoryMapper.selectList(
                new LambdaQueryWrapper<Category>().eq(Category::getParentId , parentId)
        );

        return categoryList;
    }

    @Override
    @Cacheable(value = RedisPathConstant.CATEGORY_NODE,key = "'all'")
    public List<Category> getCategoryTree(Long parentId) {
        List<Category> allCategories = categoryMapper.selectList(null);

        List<Category> categoryList=dfs(0L,allCategories);

        return categoryList;
    }

    private List<Category> dfs(Long noodeId , List<Category> allCategories) {
        List<Category> children= allCategories.stream()
                .filter(item -> item.getParentId().longValue() == noodeId)
                .collect(Collectors.toList());
        for(Category child:children){
            child.setChildren(dfs(child.getId(),allCategories));
            child.setHasChildren(CollectionUtils.isEmpty(child.getChildren()));
        }
        return children;
    }
}
