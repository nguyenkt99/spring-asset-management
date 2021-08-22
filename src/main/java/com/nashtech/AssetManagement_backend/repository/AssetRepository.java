package com.nashtech.AssetManagement_backend.repository;

import com.nashtech.AssetManagement_backend.dto.StateCounterData;
import com.nashtech.AssetManagement_backend.entity.AssetEntity;
import com.nashtech.AssetManagement_backend.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AssetRepository extends JpaRepository<AssetEntity, String> {
    // //Search by asset code
    // AssetEntity findbyId(String assetCode);

    // Optional<AssetEntity> findByAssetCode(String assetCode);

    //Search by asset name
    AssetEntity getByAssetName(String assetName);
    Optional<AssetEntity> findByAssetName(String assetName);

    int countAllByCategoryEntity(CategoryEntity categoryEntity);
    @Query("select a.state , count(*) from AssetEntity a where a.categoryEntity.id= ?1 " +
            "group" +
            " by a" +
            ".state")
    List<StateCounterData> countState(long categoryId);


}
