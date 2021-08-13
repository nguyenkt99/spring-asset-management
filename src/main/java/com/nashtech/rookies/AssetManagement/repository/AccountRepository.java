package com.nashtech.rookies.AssetManagement.repository;
import java.util.Optional;

import com.nashtech.rookies.AssetManagement.model.entity.Account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>{
    
    Account findByUsername(String username);

    Optional <Account> findByAccountId(Long accountId);
    @Query("FROM Account WHERE username = ?1")
    Account getAccountByUsername(String username);

    Boolean existsByUsername(String username);

    @Query("FROM Account WHERE accountId=?1")
    Account getUserById(long id);

}
