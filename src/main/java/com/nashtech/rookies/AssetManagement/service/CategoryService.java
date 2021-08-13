package com.nashtech.rookies.AssetManagement.service;

import com.nashtech.rookies.AssetManagement.model.dto.CategoryDTO;
import com.nashtech.rookies.AssetManagement.model.entity.Category;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryService {

    ResponseEntity<CategoryDTO> createCategory(CategoryDTO newCategory);

    List<CategoryDTO> getAllCategory();

    Category getCategoryById(long id);
}
