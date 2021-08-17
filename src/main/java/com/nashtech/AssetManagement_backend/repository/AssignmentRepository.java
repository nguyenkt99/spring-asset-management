package com.nashtech.AssetManagement_backend.repository;

import com.nashtech.AssetManagement_backend.entity.AssignmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AssignmentRepository extends JpaRepository<AssignmentEntity, Long> {



}
