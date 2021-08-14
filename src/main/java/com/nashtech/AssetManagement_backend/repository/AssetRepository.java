package com.nashtech.AssetManagement_backend.repository;

import com.nashtech.AssetManagement_backend.entity.AssetEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AssetRepository extends JpaRepository<AssetEntity, String> {
    // //Search by asset code
    // AssetEntity findbyId(String assetCode);

    // Optional<AssetEntity> findByAssetCode(String assetCode);

    //Search by asset name
    AssetEntity getByAssetName(String assetName);
    Optional<AssetEntity> findByAssetName(String assetName);


}
