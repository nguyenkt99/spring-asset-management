package com.nashtech.rookies.AssetManagement.model.dto;

import com.nashtech.rookies.AssetManagement.model.entity.Category;
import com.nashtech.rookies.AssetManagement.model.entity.State;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
public class CreateAssetRequest {

    @NotEmpty
    private String assetName;

    @NotEmpty
    private String categoryId;

    @NotEmpty
    private String specification;

    @NotEmpty
    private String installedDate;

    @NotEmpty
    private String stateId;

    @NotEmpty
    private String adminId;
}
