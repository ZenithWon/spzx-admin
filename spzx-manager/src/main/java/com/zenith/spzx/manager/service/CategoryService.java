package com.zenith.spzx.manager.service;

import com.zenith.spzx.model.entity.product.Category;
import com.zenith.spzx.model.vo.product.CategoryExcelVo;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CategoryService {
    List<Category> findByPaerntId(Long parentId);

    void exportData(HttpServletResponse response);

    void importData(MultipartFile file);

    void saveData(List<CategoryExcelVo> cacheList);
}
