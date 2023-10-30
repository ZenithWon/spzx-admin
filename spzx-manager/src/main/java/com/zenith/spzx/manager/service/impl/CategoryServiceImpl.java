package com.zenith.spzx.manager.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zenith.spzx.common.exception.MyException;
import com.zenith.spzx.manager.listener.ExcelListener;
import com.zenith.spzx.manager.mapper.CategoryMapper;
import com.zenith.spzx.manager.service.CategoryService;
import com.zenith.spzx.model.entity.product.Category;
import com.zenith.spzx.model.vo.common.ResultCodeEnum;
import com.zenith.spzx.model.vo.product.CategoryExcelVo;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private CategoryService selfProxy;

    @Override
    public List<Category> findByPaerntId(Long parentId) {
        List<Category> categories = categoryMapper.selectList(
                new LambdaQueryWrapper<Category>().eq(Category::getParentId , parentId)
        );

        if(!CollectionUtils.isEmpty(categories)){
            for(Category item:categories){
                Long count = categoryMapper.selectCount(
                        new LambdaQueryWrapper<Category>()
                                .eq(Category::getParentId , item.getId())
                                .orderByAsc(Category::getOrderNum)
                );
                item.setHasChildren(count > 0);
            }
        }

        return categories;
    }

    @Override
    public void exportData(HttpServletResponse response) {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");

        String fileName = URLEncoder.encode("分类数据", StandardCharsets.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

        List<Category> categories = categoryMapper.selectList(null);
        List<CategoryExcelVo> categoryExcelVoList=new ArrayList<>();

        categories.forEach(item->{
            CategoryExcelVo categoryExcelVo=new CategoryExcelVo();
            BeanUtils.copyProperties(item,categoryExcelVo);
            categoryExcelVoList.add(categoryExcelVo);
        });


        try {
            EasyExcel.write(response.getOutputStream(), CategoryExcelVo.class)
                    .sheet("分类数据")
                    .doWrite(categoryExcelVoList);
        } catch (IOException e) {
            e.printStackTrace();
            throw new MyException(ResultCodeEnum.DATA_ERROR);
        }
    }

    @Override
    public void importData(MultipartFile file) {
        ExcelListener listener=new ExcelListener(selfProxy);

        try {
            EasyExcel.read(file.getInputStream(),CategoryExcelVo.class,listener).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
            throw new MyException(ResultCodeEnum.DATA_ERROR);
        }
    }

    @Override
    @Transactional
    public void saveData(List<CategoryExcelVo> cacheList) {
        for(CategoryExcelVo item:cacheList){
            Category dbCategory = categoryMapper.selectById(item.getId());
            if(dbCategory!=null){
                throw new MyException(ResultCodeEnum.DATABASE_ERROR);
            }

            Category category=new Category();
            BeanUtils.copyProperties(item,category);
            categoryMapper.insert(category);
        }
    }
}
