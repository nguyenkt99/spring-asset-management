package com.nashtech.AssetManagement_backend.service.Impl;

import com.nashtech.AssetManagement_backend.dto.CategoryDTO;
import com.nashtech.AssetManagement_backend.entity.CategoryEntity;
import com.nashtech.AssetManagement_backend.exception.ConflictException;
import com.nashtech.AssetManagement_backend.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CategoryServiceImplTest {
    private final String EXIST_CATEGORY_ERROR = "Category is already existed. " +
            "Please enter a different category Prefix is already existed. Please enter a different prefix";

    @Mock
    private CategoryRepository repository;

    @InjectMocks
    private CategoryServiceImpl service;

    @Test
    public void create_when_prefix_already_exist_then_throw_conflict_exception() {
        CategoryDTO dto = new CategoryDTO();
        dto.setPrefix("KK");
        dto.setName("Old man");

        Mockito.when(repository.existsByPrefixOrName(Mockito.anyString(), Mockito.anyString())).thenReturn(true);

        Exception exception = assertThrows(ConflictException.class, () -> service.create(dto));
        assertTrue(exception.getMessage().equalsIgnoreCase(EXIST_CATEGORY_ERROR));
    }

    @Test
    public void create_then_category_dto() {
        CategoryDTO dto = new CategoryDTO("LAPTOP ASUS", "KK");
        CategoryEntity entity = new CategoryEntity(1L, "LAPTOP ASUS S15", "KK", null);
        Mockito.when(repository.existsByPrefixOrName(Mockito.anyString(), Mockito.anyString())).thenReturn(false);
        Mockito.when(repository.save(Mockito.any())).thenReturn(entity);
        CategoryDTO result = service.create(dto);
        assertEquals(entity.getPrefix(), result.getPrefix());
        assertEquals(entity.getName(), result.getName());
    }

    @Test
    void showAll_then_return_list_with_size_3() {
        List<CategoryEntity> categories = Arrays.asList(new CategoryEntity(), new CategoryEntity(), new CategoryEntity());
        Mockito.when(repository.findAll()).thenReturn(categories);
        assertEquals(service.showAll().size(), categories.size());
    }
}