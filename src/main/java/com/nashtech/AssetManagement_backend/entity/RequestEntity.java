package com.nashtech.AssetManagement_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private AssignmentState state;

    @Column(name = "requested_date")
    private Date requestedDate;

    @Column(name = "returned_date")
    private Date returnedDate;

    @ManyToOne
    @JoinColumn(name="request_by")
    private UsersEntity requestBy;

    @ManyToOne
    @JoinColumn(name="accept_by")
    private UsersEntity acceptBy;

    @ManyToOne
    @JoinColumn(name="assignment_id")
    private AssignmentEntity assignmentEntity;
}
