package com.nashtech.AssetManagement_backend.repository;

import com.nashtech.AssetManagement_backend.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    boolean existsByPrefixOrName(String prefix, String name);

    CategoryEntity getByName(String name);


    CategoryEntity getByPrefix(String prefix);

    Optional<CategoryEntity> findByPrefix(String prefix);
}
