package com.nashtech.AssetManagement_backend.controller;

import com.nashtech.AssetManagement_backend.dto.CategoryDTO;
import com.nashtech.AssetManagement_backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping()
    public ResponseEntity<List<CategoryDTO>> showAll(){
        return ResponseEntity.ok().body(categoryService.showAll());
    }

    @PostMapping()
    public ResponseEntity<CategoryDTO> create(@Valid @RequestBody CategoryDTO dto){
        return ResponseEntity.ok().body(categoryService.create(dto));
    }

    @PutMapping("/{categoryId}")
    public CategoryDTO editCategory(@PathVariable long categoryId, @RequestBody CategoryDTO categoryDTO) {
        categoryDTO.setId(categoryId);
        return categoryService.update(categoryDTO);
    }

    @DeleteMapping("/{categoryId}")
    public void deleteCategory(@PathVariable long categoryId) {
        categoryService.delete(categoryId);
    }

}
