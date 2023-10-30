package com.zenith.spzx.manager.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.zenith.spzx.manager.mapper.CategoryMapper;
import com.zenith.spzx.manager.service.CategoryService;
import com.zenith.spzx.model.entity.product.Category;
import com.zenith.spzx.model.vo.product.CategoryExcelVo;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;

import java.util.List;

public class ExcelListener implements ReadListener<CategoryExcelVo> {
    private CategoryService categoryService;
    private static final int BATCH_COUNT=100;
    private List<CategoryExcelVo> cacheList= ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    public ExcelListener(CategoryService categoryService){
        this.categoryService=categoryService;
    }

    @Override
    public void invoke(CategoryExcelVo t , AnalysisContext analysisContext) {
        cacheList.add(t);
        if(cacheList.size()>=BATCH_COUNT){
            categoryService.saveData(cacheList);
            cacheList= ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        categoryService.saveData(cacheList);
    }

}
