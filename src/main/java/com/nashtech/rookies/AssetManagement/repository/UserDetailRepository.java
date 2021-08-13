package com.nashtech.rookies.AssetManagement.repository;

import com.nashtech.rookies.AssetManagement.model.entity.Account;
import com.nashtech.rookies.AssetManagement.model.entity.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserDetailRepository extends JpaRepository<UserDetail,String> {

    Optional<UserDetail> findByAccount(Account account);
    List<UserDetail> findByIdOrFirstNameContainingOrLastNameContaining(String searchStr1,String searchStr2,String searchStr3);
}
