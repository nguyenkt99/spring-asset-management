package com.nashtech.AssetManagement_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nashtech.AssetManagement_backend.dto.CategoryDTO;
import com.nashtech.AssetManagement_backend.entity.CategoryEntity;
import com.nashtech.AssetManagement_backend.repository.CategoryRepository;
import com.nashtech.AssetManagement_backend.service.CategoryService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categorySer;

    @MockBean
    private CategoryRepository categoryRepo;

    private CategoryEntity entity;

    private CategoryDTO dto;

    @BeforeEach
    void setUp() {
        entity = new CategoryEntity(1L, "LAPTOP ASUS", "LA", null);
        dto = new CategoryDTO("LAPTOP ASUS", "LA");
    }

    @Test
    @WithMockUser(username = "admin")
    void create_then_return_json_object() throws Exception {
        CategoryDTO category = new CategoryDTO();
        category.setPrefix("LA");
        category.setName("LAPTOP");
        Mockito.when(categorySer.create(category)).thenReturn(category);
        mockMvc.perform(post("/api/category/")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(new ObjectMapper().writeValueAsString(category))
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.equalTo(category.getName())))
                .andExpect(jsonPath("$.prefix", is(category.getPrefix())));
    }

    @Test
    void showAll_then_return_list_with_size_3() throws Exception {
        CategoryDTO category = new CategoryDTO();
        category.setPrefix("LA");
        category.setName("LAPTOP");
        List<CategoryDTO> categories = Arrays.asList(category, category, category);
        Mockito.when(categorySer.showAll()).thenReturn(categories);
        mockMvc.perform(get("/api/category/")
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(categories.size())));
    }
}