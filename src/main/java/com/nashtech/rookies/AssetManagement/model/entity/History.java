package com.nashtech.rookies.AssetManagement.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "history")
@Getter
@Setter
public class History implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long historyId;

    private Date assignDate;

    private Date returnDate;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assign_to")
    private Account assignTo;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assign_by")
    private Account assignBy;

    @OneToOne
    @JoinColumn(name = "asset_id")
    private Asset asset;

}
