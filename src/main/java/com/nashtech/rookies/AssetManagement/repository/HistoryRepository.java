package com.nashtech.rookies.AssetManagement.repository;

import com.nashtech.rookies.AssetManagement.model.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface HistoryRepository extends JpaRepository<History,Long> {
    List<History> findAllByAsset_AssetId(String assetId);
    @Query(value = "SELECT * from history h where  h.assign_to = ?1 and " +
            "h.return_date is NULL ",nativeQuery = true)
    List<History> findAllByUserId(Long userId);
}
