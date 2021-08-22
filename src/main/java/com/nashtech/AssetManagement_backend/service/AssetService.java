package com.nashtech.AssetManagement_backend.service;

import com.nashtech.AssetManagement_backend.dto.AssetDTO;
import com.nashtech.AssetManagement_backend.entity.CategoryEntity;

import java.util.List;

public interface AssetService {
    AssetDTO create(AssetDTO dto, String username);

    List<AssetDTO> findAllByAdminLocation(String username);

    public AssetDTO findByAssetName(String assetName);

    public AssetDTO findbyId(String assetCode);

    Boolean canDelete(String assetCode);

    Boolean delete(String assetCode);

    AssetDTO update(AssetDTO dto);

    int countByCategory(Long categoryId, String username);
}
