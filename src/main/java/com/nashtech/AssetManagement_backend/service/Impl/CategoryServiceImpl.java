package com.nashtech.AssetManagement_backend.service.Impl;

import com.nashtech.AssetManagement_backend.dto.CategoryDTO;
import com.nashtech.AssetManagement_backend.entity.CategoryEntity;
import com.nashtech.AssetManagement_backend.exception.BadRequestException;
import com.nashtech.AssetManagement_backend.exception.ConflictException;
import com.nashtech.AssetManagement_backend.repository.CategoryRepository;
import com.nashtech.AssetManagement_backend.service.CategoryService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Data
@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final String EXIST_PREFIX_CATEGORY_ERROR = "Prefix is already existed. Please enter a different prefix";

    private final String EXIST_NAME_CATEGORY_ERROR = " Category is already existed. " +
            "Please enter a different category ";

    private final CategoryRepository categoryRepo;

    @Override
    public List<CategoryDTO> showAll() {
        return categoryRepo.findAll().stream().map(CategoryDTO::toDTO).collect(Collectors.toList());
    }

    @Override
    public CategoryDTO create(CategoryDTO dto) {
        if (categoryRepo.getByName(dto.getName()) != null)
            throw new ConflictException(EXIST_NAME_CATEGORY_ERROR);
        if (categoryRepo.getByPrefix(dto.getPrefix()) != null)
            throw new BadRequestException(EXIST_PREFIX_CATEGORY_ERROR);
        CategoryEntity cate = CategoryDTO.toEntity(dto);
        return CategoryDTO.toDTO(categoryRepo.save(cate));
    }
}
