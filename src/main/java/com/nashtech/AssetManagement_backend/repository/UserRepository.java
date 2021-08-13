package com.nashtech.AssetManagement_backend.repository;


import com.nashtech.AssetManagement_backend.entity.Location;
import com.nashtech.AssetManagement_backend.entity.RolesEntity;
import com.nashtech.AssetManagement_backend.entity.UsersEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<UsersEntity, Long> {


    UsersEntity getByStaffCode(String staffCode);

    Optional<UsersEntity>  findByStaffCode(String staffCode);

    Optional<UsersEntity>  findByStaffCodeAndLocation(String staffCode, Location location);


    Integer deleteByStaffCode(String s);

    List<UsersEntity> findAllByLocation(Location location, Pageable pageable);

    List<UsersEntity> findAllByLocationAndRole(Location location, RolesEntity rolesEntity, Pageable pageable);

    List<UsersEntity> findAllByLocationAndStaffCodeContainingOrFirstNameContainingOrLastNameContaining(Location location, String staffCode, String firstName, String lastname, Pageable pageable);

    List<UsersEntity> findAllByLocationAndRoleAndStaffCodeContainingOrFirstNameContainingOrLastNameContaining(Location location, RolesEntity rolesEntity, String staffCode, String firstName, String lastname,Pageable pageable);

    UsersEntity getByUserName(String username);

    Optional<UsersEntity> findByUserName(String username);

    Boolean existsByUserName(String username);

    List<UsersEntity> findAllByLocation(Location location);

    List<UsersEntity> findAllByLocationOrderByFirstNameAscLastNameAsc(Location location);
}
