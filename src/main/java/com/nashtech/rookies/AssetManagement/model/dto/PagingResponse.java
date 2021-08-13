package com.nashtech.rookies.AssetManagement.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PagingResponse implements Serializable {
    private List<AdminAssetListDTO> assetList;
    private int totalPages;
    private int pageNumber;
    private int pageSize;

}
