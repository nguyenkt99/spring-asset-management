package com.nashtech.AssetManagement_backend.service.Impl;

import com.nashtech.AssetManagement_backend.entity.RoleName;
import com.nashtech.AssetManagement_backend.entity.RolesEntity;
import com.nashtech.AssetManagement_backend.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    private List<RolesEntity> roles;

    @BeforeEach
    void setUp() {
        RolesEntity role1 = new RolesEntity(1L, RoleName.STAFF,null);
        RolesEntity role2 = new RolesEntity(2L, RoleName.ADMIN,null);
        roles = Stream.of(role1,role2).collect(Collectors.toList());
    }

    @Test
    void getListRole_Success() {
        Mockito.when(roleRepository.findAll()).thenReturn(roles);
        assertEquals(2,roleService.listRole().size());
    }
}