package com.nashtech.rookies.AssetManagement.repository;

import java.util.Optional;

import com.nashtech.rookies.AssetManagement.constant.RoleName;
import com.nashtech.rookies.AssetManagement.model.entity.Role;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long>{
    Optional<Role> findByName(RoleName roleName);
}