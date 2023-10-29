package com.zenith.spzx.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zenith.spzx.manager.mapper.CategoryMapper;
import com.zenith.spzx.manager.service.CategoryService;
import com.zenith.spzx.model.entity.product.Category;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> findByPaerntId(Long parentId) {
        List<Category> categories = categoryMapper.selectList(
                new LambdaQueryWrapper<Category>().eq(Category::getParentId , parentId)
        );

        if(!CollectionUtils.isEmpty(categories)){
            for(Category item:categories){
                Long count = categoryMapper.selectCount(
                        new LambdaQueryWrapper<Category>().eq(Category::getParentId , item.getId())
                );
                item.setHasChildren(count > 0);
            }
        }

        return categories;
    }
}
