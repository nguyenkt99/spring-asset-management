package com.nashtech.AssetManagement_backend.repository;

import com.nashtech.AssetManagement_backend.entity.LocationEntity;
import com.nashtech.AssetManagement_backend.entity.RequestAssignEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestAssignRepository extends JpaRepository<RequestAssignEntity, Long> {
    List<RequestAssignEntity> findAllByRequestBy_Location(LocationEntity location);
    List<RequestAssignEntity> findAllByRequestBy_StaffCode(String staffCode);
}
