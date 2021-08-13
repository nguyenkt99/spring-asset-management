package com.nashtech.rookies.AssetManagement.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nashtech.rookies.AssetManagement.model.entity.Category;
import com.nashtech.rookies.AssetManagement.model.entity.History;
import com.nashtech.rookies.AssetManagement.model.entity.State;
import lombok.Data;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.io.Serializable;
import java.util.Date;

@Data
public class AssetDetailDTO implements Serializable {
    private String assetId;
    private String assetName;
    private Category category;
    private String specification;
    private Date installedDate;
    private String location;
    private State state;
    private History history;
}
