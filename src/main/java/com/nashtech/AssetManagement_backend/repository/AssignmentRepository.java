package com.nashtech.AssetManagement_backend.repository;

import java.util.List;

import javax.transaction.Transactional;

import com.nashtech.AssetManagement_backend.entity.AssignmentEntity;
import com.nashtech.AssetManagement_backend.entity.Location;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface AssignmentRepository extends JpaRepository<AssignmentEntity, Long> {
    @Transactional
    @Query("select a from AssignmentEntity a "
            + "join UsersEntity u on u = a.assignBy "
            + "where u.location like :location "
            + "and (a.state like 'ACCEPTED' "
            + "or a.state like 'WAITING_FOR_ACCEPTANCE' "
            + "or a.state like 'CANCELED_ASSIGN') "
            + "order by a.id asc")
    List<AssignmentEntity> findAllByAdmimLocation(@Param("location") Location location);

    @Transactional
    @Query("select a from AssignmentEntity a "
            + "join UsersEntity u on u = a.assignBy "
            + "where u.userName like :username "
            + "and current_date >= a.assignedDate "
            + "and (a.state like 'ACCEPTED' "
            + "or a.state like 'WAITING_FOR_ACCEPTANCE' "
            + "or a.state like 'CANCELED_ASSIGN') "
            + "order by a.id asc")
    List<AssignmentEntity> findAssignmentsByUser(@Param("username") String username);
}