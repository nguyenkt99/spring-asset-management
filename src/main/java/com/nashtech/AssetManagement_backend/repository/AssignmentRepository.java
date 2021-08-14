package com.nashtech.AssetManagement_backend.repository;

import com.nashtech.AssetManagement_backend.entity.AssignmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentRepository extends JpaRepository<AssignmentEntity, Long> {
}
