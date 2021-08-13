package com.nashtech.rookies.AssetManagement.repository;

import com.nashtech.rookies.AssetManagement.model.entity.Asset;
import com.nashtech.rookies.AssetManagement.model.entity.Category;
import com.nashtech.rookies.AssetManagement.model.entity.State;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface AssetRepository extends JpaRepository<Asset, String> {
    Page<Asset> getAllByLocation(Pageable pageable, String location);

    Page<Asset> getAllByLocationAndState(Pageable pageable, String location, State state);

    Page<Asset> getAllByLocationAndCategory(Pageable pageable, String location, Category category);

    @Query(value = "SELECT * FROM Asset a WHERE a.cate_id IN ?1 AND a.location = ?2",nativeQuery = true)
    List<Asset> findAllByCatesAndLocation( Set<Long> cates, String location);

    @Query(value = "SELECT * FROM Asset a WHERE (a.cate_id IN ?1 OR a.state_id IN ?2 ) AND a.location = ?3",nativeQuery = true)
    List<Asset> findAllByCatesOrByStateAndLocation( Set<Long> cates,Set<Long> states, String location);

    @Query(value = "SELECT * FROM Asset a WHERE  a.state_id in ?1 AND a.location = ?2",nativeQuery = true)
    List<Asset> findAllByStatesAndLocation( Set<Long> states, String location);

    List<Asset> findByAssetIdContainingIgnoreCase(String assetId);

    List<Asset> findByAssetNameContainingIgnoreCaseOrAssetIdContainingIgnoreCase(String assetName, String assetId);
//    List<Asset> findAssetByAssetNameIgnoreCaseAndLocationOrAssetIdIgnoreCaseAndLocation(String assetName, String assetId, String location);
    List<Asset> findAllByLocation(String location);

    Asset findAssetByAssetId(String assetId);

}
