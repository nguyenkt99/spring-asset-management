package com.nashtech.rookies.AssetManagement.model.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CategoryDTO implements Serializable {
    private long cateId;
    @NotEmpty
    private String catePrefix;
    @NotEmpty
    private String cateName;
}
