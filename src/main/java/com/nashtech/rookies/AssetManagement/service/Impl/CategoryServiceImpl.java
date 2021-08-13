package com.nashtech.rookies.AssetManagement.service.Impl;

import com.nashtech.rookies.AssetManagement.exception.DuplicateException;
import com.nashtech.rookies.AssetManagement.exception.EmptyException;
import com.nashtech.rookies.AssetManagement.exception.NotFoundException;
import com.nashtech.rookies.AssetManagement.model.dto.CategoryDTO;
import com.nashtech.rookies.AssetManagement.model.entity.Category;
import com.nashtech.rookies.AssetManagement.repository.CategoryRepository;
import com.nashtech.rookies.AssetManagement.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper mapper;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper mapper) {
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
    }

    @Override
    public ResponseEntity<CategoryDTO> createCategory(CategoryDTO newCategory) {
        if(newCategory == null){
            throw new EmptyException("Object Empty!!");
        }
        if(newCategory.getCateName().isEmpty()){
            throw new EmptyException("Category name must not empty!!");
        }
        if(newCategory.getCatePrefix().isEmpty()){
            throw new EmptyException("Category prefix must not empty!!");
        }
        Category cateE = mapper.map(newCategory, Category.class);
        checkDuplicate(cateE);
        CategoryDTO newCate = mapper.map(categoryRepository.save(cateE), CategoryDTO.class);

        return ResponseEntity.ok().body(newCate);
    }

    @Override
    public List<CategoryDTO> getAllCategory() {
        List<Category> listCateE = categoryRepository.findAll();
        List<CategoryDTO> listCateDTO = new ArrayList<>();
        for (Category cate : listCateE) {
            CategoryDTO cateDTO = mapper.map(cate, CategoryDTO.class);
            listCateDTO.add(cateDTO);
        }
        return listCateDTO;
    }

    @Override
    public Category getCategoryById(long cateId) {
        Optional<Category> cate = Optional.ofNullable(categoryRepository.findById(cateId)
                .orElseThrow(() -> new NotFoundException("This category does not exist")));

        return cate.get();
    }

    private void checkDuplicate(Category newCate){
        if(categoryRepository.existsCategoriesByCateName(newCate.getCateName())){
            throw new DuplicateException("Category is already existed. Please enter a different category");
        }
        if(categoryRepository.existsCategoriesByCatePrefix(newCate.getCatePrefix())){
            throw new DuplicateException("Prefix is already existed. Please enter a different prefix");
        }
    }

}
