package com.nashtech.rookies.AssetManagement.model.dto;

import com.nashtech.rookies.AssetManagement.model.entity.Category;
import com.nashtech.rookies.AssetManagement.model.entity.State;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CreateAssetDTO {
    @NotEmpty
    private String assetId;

    @NotEmpty
    private String assetName;

    private Category category;

    @NotEmpty
    private String specification;

    private Date installedDate;

    @NotEmpty
    private String location;

    private State state;
}
