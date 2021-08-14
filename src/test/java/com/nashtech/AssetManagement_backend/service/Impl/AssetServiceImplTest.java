package com.nashtech.AssetManagement_backend.service.Impl;

import com.nashtech.AssetManagement_backend.dto.AssetDTO;
import com.nashtech.AssetManagement_backend.dto.UserDto;
import com.nashtech.AssetManagement_backend.entity.*;
import com.nashtech.AssetManagement_backend.exception.BadRequestException;
import com.nashtech.AssetManagement_backend.exception.ResourceNotFoundException;
import com.nashtech.AssetManagement_backend.repository.AssetRepository;
import com.nashtech.AssetManagement_backend.repository.CategoryRepository;
import com.nashtech.AssetManagement_backend.repository.UserRepository;
import com.nashtech.AssetManagement_backend.entity.AssetEntity;
import com.nashtech.AssetManagement_backend.entity.AssignmentEntity;
import com.nashtech.AssetManagement_backend.exception.ConflictException;

import org.junit.Rule;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.rules.ExpectedException;
import javax.validation.constraints.NotNull;
import java.util.*;

import java.util.Date;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class AssetServiceImplTest {
    private final String ASSET_BAD_STATE_ERROR = "Asset must be AVAILABLE or NOT_AVAILABLE.";
    private final String CATEGORY_NOT_FOUND_ERROR = "Category prefix not exists.";
    private final String USER_NOT_FOUND_ERROR = "User not exists.";
    private final String ASSET_NOT_FOUND_ERROR = "Asset not found.";
    private final String ASSET_CONFLICT_ERROR = "Asset belongs to one or more historical assignments.";

    @Mock
    private AssetRepository assetRepo;

    @Mock
    private CategoryRepository categoryRepo;

    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private AssetServiceImpl assetSer;

    private List<AssetEntity> assets;

    @BeforeEach
    void setUp() {
        AssetEntity asset1 = new AssetEntity();
        AssetEntity asset2 = new AssetEntity();
        CategoryEntity category = new CategoryEntity();
        category.setId(1L);
        asset1.setAssetCode("LA000001");
        asset1.setAssetName("LAPTOP 01");
        asset1.setState(AssetState.AVAILABLE);
        asset2.setAssetCode("LA000002");
        asset2.setAssetName("LAPTOP 02");
        asset2.setState(AssetState.AVAILABLE);

        asset1.setCategoryEntity(category);
        asset2.setCategoryEntity(category);
        assets = Stream.of(asset1, asset2).collect(Collectors.toList());
    }

    @Test
    void create_when_category_not_exist_then_throw_resource_not_found_exception() {
        AssetDTO dto = new AssetDTO("LA000001", "LAPTOP", AssetState.AVAILABLE
                , "LAPTOP ASUS", new Date(), Location.HCM, "LA", "");
        Mockito.when(categoryRepo.findByPrefix(Mockito.anyString())).thenReturn(Optional.empty());
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> assetSer.create(dto, Mockito.anyString()));
        assertEquals(exception.getMessage(), CATEGORY_NOT_FOUND_ERROR);
    }

    private static final String ASSETCODE = "LA000001";
    private static final String ASSETNAME = "LAPTOP";
    private static final AssetState STATE = AssetState.AVAILABLE;
    private static final String SPECIFICATION = "LAPTOP ASUS";
    Date date = new Date();
    private static final Location LOCATION = Location.HCM;

    @Rule
    public final ExpectedException exception = ExpectedException.none();


    @NotNull
    private AssetEntity newEntity() {
        AssetEntity entity = new AssetEntity();
        entity.setAssetCode(ASSETCODE);
        entity.setAssetName(ASSETNAME);
        entity.setState(STATE);
        entity.setSpecification(SPECIFICATION);
        entity.setInstalledDate(date);
        entity.setLocation(LOCATION);
        CategoryEntity cate = new CategoryEntity();
        cate.setId(1L);
        cate.setPrefix("LA");
        cate.setName("LAPTOP");
        entity.setCategoryEntity(cate);
        return entity;
    }

    @Test
    void create_when_state_is_not_AVAILABLE_and_NOT_AVAILABLE_then_throw_bad_request_exception() {
        AssetDTO dto = new AssetDTO();
        CategoryEntity cate = new CategoryEntity();
        cate.setPrefix("LA");
        UsersEntity user = new UsersEntity();
        user.setLocation(Location.HCM);
        Mockito.when(categoryRepo.findByPrefix(Mockito.anyString())).thenReturn(Optional.of(new CategoryEntity()));
        Mockito.when(userRepo.findByUserName(Mockito.anyString())).thenReturn(Optional.of(user));

        dto.setState(AssetState.ASSIGNED);
        Exception exception = assertThrows(BadRequestException.class,
                () -> assetSer.create(dto, Mockito.anyString()));
        assertEquals(exception.getMessage(), ASSET_BAD_STATE_ERROR);

        dto.setState(AssetState.RECYCLED);
        exception = assertThrows(BadRequestException.class,
                () -> assetSer.create(dto, Mockito.anyString()));
        assertEquals(exception.getMessage(), ASSET_BAD_STATE_ERROR);

        dto.setState(AssetState.WAITING_FOR_RECYCLING);
        exception = assertThrows(BadRequestException.class,
                () -> assetSer.create(dto, Mockito.anyString()));
        assertEquals(exception.getMessage(), ASSET_BAD_STATE_ERROR);
    }

    @Test
    void create_then_return_asset_dto() {
        Date date = new Date();
        AssetDTO dto = new AssetDTO("LA000001", "LAPTOP", AssetState.AVAILABLE, "LAPTOP ASUS"
                , date, Location.HCM, "LA", "");
        CategoryEntity cate = new CategoryEntity();
        cate.setPrefix("LA");
        AssetEntity asset = new AssetEntity("LA000001", "LAPTOP", AssetState.AVAILABLE, "LAPTOP ASUS"
                , date, Location.HCM, cate, null);
        UsersEntity user = new UsersEntity();
        user.setLocation(Location.HCM);

        Mockito.when(categoryRepo.findByPrefix(Mockito.anyString())).thenReturn(Optional.of(new CategoryEntity()));
        Mockito.when(userRepo.findByUserName(Mockito.anyString())).thenReturn(Optional.of(user));
        Mockito.when((assetRepo.save(Mockito.any()))).thenReturn(asset);

        AssetDTO result = assetSer.create(dto, "");

        assertEquals(dto.getAssetCode(), result.getAssetCode());
        assertEquals(dto.getAssetName(), result.getAssetName());
        assertEquals(dto.getLocation(), result.getLocation());
        assertEquals(dto.getCategoryPrefix(), result.getCategoryPrefix());
        assertEquals(dto.getSpecification(), result.getSpecification());
        assertEquals(dto.getInstalledDate(), result.getInstalledDate());
        assertEquals(dto.getState(), result.getState());
    }

    @NotNull
    private AssetDTO newDTO() {
        AssetDTO dto = new AssetDTO();
        dto.setAssetCode(ASSETCODE);
        dto.setAssetName(ASSETNAME);
        dto.setState(STATE);
        dto.setSpecification(SPECIFICATION);
        dto.setInstalledDate(date);
        dto.setLocation(LOCATION);
        CategoryEntity cate = new CategoryEntity();
        cate.setId(1L);
        cate.setPrefix("LA");
        cate.setName("LAPTOP");
        dto.setCategoryPrefix(cate.getPrefix());
        dto.setCategoryName(cate.getName());
        return dto;
    }


    ////Test must find all assets
    @Test
    public void findAllUserDto() {
        assertNotNull(assetSer);
        List<AssetEntity> list = new ArrayList<>();
        AssetEntity asset1 = new AssetEntity();
        AssetEntity asset2 = new AssetEntity();
        list.add(asset1);
        list.add(asset2);
        when(assetRepo.findAll()).thenReturn(list);
        Collection<AssetDTO> result = assetSer.findAllByAdminLocation("admin");
        assertEquals(2, result.size());
    }

    /////Test find by id success:
    @Test
    public void findByCode_success() {
        AssetEntity asset = newEntity();
        when(assetRepo.findById(Mockito.anyString())).thenReturn(Optional.ofNullable(asset));
        AssetDTO assetDTO = assetSer.findbyId(Mockito.anyString());
        assertThat(assetDTO).usingRecursiveComparison().isEqualTo(newDTO());

    }

    ////Test find by id fail:
    @Test
    public void findByCode_fail() {
        when(assetRepo.findById(Mockito.anyString())).thenReturn(Optional.ofNullable(null));
        try {
            AssetDTO assetDTO = assetSer.findbyId(Mockito.anyString());
        } catch (ResourceNotFoundException e) {
            exception.expect(ResourceNotFoundException.class);
        }
    }

    @Test
    void editAsset_ReturnAssetDto_WhenSuccessful() {
        AssetDTO assetDTO = AssetDTO.toDTO(assets.get(0));
        assetDTO.setAssetName("HP PROBOOK 450 G8");
        assetDTO.setState(AssetState.NOT_AVAILABLE);
        Mockito.when((assetRepo.findById(Mockito.any()))).thenReturn(Optional.ofNullable(assets.get(0)));
        Mockito.when((assetRepo.save(Mockito.any()))).thenReturn(assets.get(0));
        Assertions.assertThat(assetDTO)
                .usingRecursiveComparison()
                .isEqualTo(assetSer.update(assetDTO));
    }

    @Test
    void updateAsset_ThrowError_WhenAssetNotFound() {
        AssetDTO assetDTO = AssetDTO.toDTO(assets.get(0));
        when(assetRepo.findById(Mockito.anyString())).thenThrow(new ResourceNotFoundException(ASSET_NOT_FOUND_ERROR));
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            assetSer.update(assetDTO);
        });
        assertEquals(exception.getMessage(), ASSET_NOT_FOUND_ERROR);
    }

    @Test
    void updateAsset_ThrowError_WhenAssetHasBeenAssigned() {
        AssetDTO assetDTO = AssetDTO.toDTO(assets.get(0));
        when(assetRepo.findById(Mockito.anyString())).thenThrow(new ConflictException("Asset has been assigned"));
        Exception exception = assertThrows(ConflictException.class, () -> {
            assetSer.update(assetDTO);
        });
        assertEquals(exception.getMessage(), "Asset has been assigned");
    }
    @Test
    void create_when_username_not_exist_then_throw_resource_not_found_exception() {
        AssetDTO dto = new AssetDTO("LA000001", "LAPTOP", AssetState.AVAILABLE, "LAPTOP ASUS"
                , new Date(), Location.HCM, "LA","");
        Mockito.when(categoryRepo.findByPrefix(Mockito.anyString())).thenReturn(Optional.of(new CategoryEntity()));
        Mockito.when(userRepo.findByUserName(Mockito.anyString())).thenReturn(Optional.empty());
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> assetSer.create(dto, Mockito.anyString()));
        assertEquals(exception.getMessage(), USER_NOT_FOUND_ERROR);
    }
    @Test
    void canDelete_when_asset_code_not_exists_then_throw_resource_not_found_exception() {
        Mockito.when(assetRepo.findById(Mockito.anyString())).thenReturn(Optional.empty());
        Exception exception = assertThrows(ResourceNotFoundException.class,
                () -> assetSer.canDelete(Mockito.anyString()));
        assertEquals(exception.getMessage(), ASSET_NOT_FOUND_ERROR);
    }
    @Test
    void canDelete_then_return_instance_of_boolean() {
        AssetEntity asset = new AssetEntity();
        Mockito.when(assetRepo.findById(Mockito.anyString())).thenReturn(Optional.of(asset));
        assertTrue(assetSer.canDelete(Mockito.anyString()));
    }

    @Test
    void delete_when_asset_code_not_exists_then_throw_resource_not_found_exception() {
        Mockito.when(assetRepo.findById(Mockito.anyString())).thenReturn(Optional.empty());
        Exception exception = assertThrows(ResourceNotFoundException.class,
                () -> assetSer.canDelete(Mockito.anyString()));
        assertEquals(exception.getMessage(), ASSET_NOT_FOUND_ERROR);
    }
    @Test
    void delete_when_asset_has_any_assignment_then_throw_conflict_exception() {
        AssetEntity asset = new AssetEntity();
        asset.setAssignmentEntities(Arrays.asList(new AssignmentEntity(), new AssignmentEntity()));
        Mockito.when(assetRepo.findById(Mockito.anyString())).thenReturn(Optional.of(asset));
        Exception exception = assertThrows(ConflictException.class,
                () -> assetSer.delete(Mockito.anyString()));
        assertEquals(exception.getMessage(), ASSET_CONFLICT_ERROR);
    }

    @Test
    void delete_then_return_true() {
        Mockito.when(assetRepo.findById(Mockito.anyString())).thenReturn(Optional.of(new AssetEntity()));
        Mockito.doNothing().when(assetRepo).deleteById(Mockito.anyString());
        assertTrue(assetSer.delete(Mockito.anyString()));
    }
}