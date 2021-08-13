package com.nashtech.rookies.AssetManagement.service;

import com.nashtech.rookies.AssetManagement.model.dto.AdminAssetListDTO;
import com.nashtech.rookies.AssetManagement.model.dto.CreateAssetDTO;
import com.nashtech.rookies.AssetManagement.model.dto.CreateAssetRequest;
import com.nashtech.rookies.AssetManagement.model.entity.Asset;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;

public interface AssetService {
    ResponseEntity<Asset> createAsset(CreateAssetRequest request);

    ResponseEntity<?> delete(String assetId);

//    List<AdminAssetListDTO> getAllAssetInAdminLocation(int pageNo, int pageSize, String filterBy, String sortBy);

//    ResponseEntity<?> getAllAssetByAdminLocation(int pageNo, int pageSize, String location);

    ResponseEntity<?> getAllByLocation(String location);

    ResponseEntity<?> searchAsset(String searchValue);

    ResponseEntity<?> getAssetById(String assetId);

    ResponseEntity<?> findAllByState(Set<Long> state, String location);

    ResponseEntity<?> findAllByStateAndCate(Set<Long> cates, Set<Long> states, String location);

    ResponseEntity<?> findAllByCate(Set<Long> cates, String location);
}