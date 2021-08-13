package com.nashtech.rookies.AssetManagement.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "asset")
@Getter
@Setter
public class Asset implements Serializable {
    @Id
    @GeneratedValue(generator = "asset-generate")
    @GenericGenerator(name = "asset-generate",
            parameters = {@org.hibernate.annotations.Parameter(name = "length", value = "6")},
            strategy = "com.nashtech.rookies.AssetManagement.utils.AssetCodeGenerator")
    private String assetId;

    private String assetName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cate_id")
    private Category category;

    private String specification;

    private Date installedDate;

    private String location;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "state_id", nullable = false)
    private State state;

    @OneToOne(mappedBy = "asset")
    private History history;

}
