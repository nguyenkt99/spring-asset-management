package com.nashtech.rookies.AssetManagement.repository;

import com.nashtech.rookies.AssetManagement.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsCategoriesByCateName(String cateName);

    boolean existsCategoriesByCatePrefix(String catePrefix);

    Category getCategoryByCateId(Long cateId);

}
