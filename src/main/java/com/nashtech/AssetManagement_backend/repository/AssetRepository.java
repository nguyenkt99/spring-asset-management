package com.nashtech.AssetManagement_backend.repository;

import com.nashtech.AssetManagement_backend.dto.StateQuantity;
import com.nashtech.AssetManagement_backend.entity.AssetEntity;
import com.nashtech.AssetManagement_backend.entity.CategoryEntity;
import com.nashtech.AssetManagement_backend.entity.Location;
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

    int countByCategoryEntityAndLocation(CategoryEntity category, Location location);

    @Query(nativeQuery = true, value = "select a.state, count(*) quantity \n" +
            "from asset a \n" +
            "where a.category_id = ?1 and a.location = ?2 \n" +
            "group by a.state \n")
    List<StateQuantity> countState(long categoryId, String location);

}
