package com.nashtech.AssetManagement_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nashtech.AssetManagement_backend.dto.AssetDTO;
import com.nashtech.AssetManagement_backend.entity.AssetEntity;
import com.nashtech.AssetManagement_backend.entity.AssetState;
import com.nashtech.AssetManagement_backend.entity.CategoryEntity;
import com.nashtech.AssetManagement_backend.entity.Location;
import com.nashtech.AssetManagement_backend.repository.AssetRepository;
import com.nashtech.AssetManagement_backend.service.AssetService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class AssetControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AssetService assetServ;

    @MockBean
    private AssetRepository assetRepo;

    AssetDTO assetDTO = new AssetDTO();
    List<AssetDTO> assetDTOs = new ArrayList<>();

    private static final String ASSETCODE ="LA000001";
    private static final String ASSETNAME ="LAPTOP";
    private static final AssetState STATE =AssetState.AVAILABLE;
    private static final String SPECIFICATION ="LAPTOP ASUS";
    Date date = new Date();
    private static final Location LOCATION = Location.HCM;

    @BeforeEach
    public void setUp(){
        assetDTO.setAssetCode(ASSETCODE);
        assetDTO.setAssetName(ASSETNAME);
        assetDTO.setState(STATE);
        assetDTO.setSpecification(SPECIFICATION);
        assetDTO.setInstalledDate(date);
        assetDTO.setLocation(LOCATION);
        CategoryEntity cate = new CategoryEntity();
        cate.setId(1L);
        cate.setPrefix("LA");
        cate.setName("LAPTOP");
        assetDTO.setCategoryPrefix(cate.getPrefix());
        assetDTO.setCategoryName(cate.getName());
        assetDTOs.add(assetDTO);
    }

    @AfterAll
    public void tearDown(){
        assetDTOs = null;
        assetDTO = null;
    }

    @Test
    void findAllAsset() throws Exception{
        Mockito.when(assetServ.findAllByAdminLocation("admin")).thenReturn(assetDTOs);
        mockMvc.perform(get("/api/assets").contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void findbyId() throws Exception{
        Mockito.when(assetServ.findbyId(ArgumentMatchers.any())).thenReturn(assetDTO);

        mockMvc.perform(get("/api/assets/"+ASSETCODE).contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.assetCode", Matchers.equalTo("LA000001")));
    }

    void create_then_return_json_object() throws Exception {
        AssetDTO asset = new AssetDTO();
        asset.setAssetName("LAPTOP ASUS");
        asset.setSpecification("LAPTOP ASUS S15 SERIES");
        asset.setCategoryPrefix("LA");
        Mockito.when(assetServ.create(Mockito.any(), Mockito.anyString())).thenReturn(asset);
        mockMvc.perform(post("/api/asset/")
                .contentType(MediaType.APPLICATION_JSON)
                .requestAttr("userName","username")
                .characterEncoding("utf-8")
                .content(new ObjectMapper().writeValueAsString(asset))
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$.assetName", Matchers.equalTo(asset.getAssetName())))
                .andExpect(jsonPath("$.specification", Matchers.equalTo(asset.getSpecification())))
                .andExpect(jsonPath("$.categoryPrefix", Matchers.equalTo(asset.getCategoryPrefix())));
    }

    @Test
    void updateAsset_ReturnJsonAsset() throws Exception {
        AssetDTO asset = new AssetDTO();
        asset.setAssetCode("LA000001");
        asset.setAssetName("LAPTOP ASUS");
        asset.setSpecification("LAPTOP ASUS S15 SERIES");
        asset.setCategoryPrefix("LA");
        Mockito.when(assetServ.update(Mockito.any())).thenReturn(asset);
        mockMvc.perform(put("/api/asset/" + asset.getAssetCode())
                .contentType(MediaType.APPLICATION_JSON)
                .requestAttr("userName","username")
                .characterEncoding("utf-8")
                .content(new ObjectMapper().writeValueAsString(asset))
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$.assetName", Matchers.equalTo(asset.getAssetName())))
                .andExpect(jsonPath("$.specification", Matchers.equalTo(asset.getSpecification())))
                .andExpect(jsonPath("$.categoryPrefix", Matchers.equalTo(asset.getCategoryPrefix())));
    }
    @Test
    void canDelete_then_return_true() throws Exception {
        Mockito.when(assetServ.canDelete(ArgumentMatchers.anyString())).thenReturn(true);
        mockMvc.perform(get("/api/asset/LA000001/delete").contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("true")));
    }
    @Test
    void delete() throws Exception {
        Mockito.when(assetServ.delete(ArgumentMatchers.anyString())).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/asset/LA000001").contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("true")));
    }

}