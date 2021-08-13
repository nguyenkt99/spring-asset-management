package com.nashtech.AssetManagement_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "assignment")
public class AssignmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "note")
    private String note;

    @Enumerated(EnumType.STRING)
    @Column(length = 60,name = "state")
    private AssignmentState state;

    @Column(name = "assign_date")
    private Date assignDate;

    @Column(name = "accept_date")
    private Date acceptDate;

    @ManyToOne
    @JoinColumn(name="asset_id")
    private AssetEntity assetEntity;

    @ManyToOne
    @JoinColumn(name="user_id")
    private UsersEntity userAssignTo;

    @ManyToOne
    @JoinColumn(name="assign_by")
    private UsersEntity userAssignBy;

    @ManyToOne
    @JoinColumn(name="accept_by")
    private UsersEntity userAccept;


}
