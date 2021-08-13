package com.nashtech.AssetManagement_backend.service.Impl;

import com.nashtech.AssetManagement_backend.dto.CategoryDTO;
import com.nashtech.AssetManagement_backend.entity.CategoryEntity;
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
//        if (categoryRepo.existsByPrefixOrName(dto.getPrefix(), dto.getName()))
//            throw new ConflictException(EXIST_CATEGORY_ERROR);
        String strErr= "";
        if (categoryRepo.getByName(dto.getName()) != null)
            strErr += EXIST_NAME_CATEGORY_ERROR;
        if (categoryRepo.getByPrefix(dto.getPrefix())!=null)
            strErr += EXIST_PREFIX_CATEGORY_ERROR;
        if(strErr.equals("")==false){
            throw new ConflictException(strErr);
        }
        CategoryEntity cate = CategoryDTO.toEntity(dto);
        return CategoryDTO.toDTO(categoryRepo.save(cate));
    }
}
