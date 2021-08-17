package com.nashtech.AssetManagement_backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nashtech.AssetManagement_backend.entity.AssignmentState;
import com.nashtech.AssetManagement_backend.entity.RequestEntity;
import com.nashtech.AssetManagement_backend.entity.UsersEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestDTO {
    private Long id;
    private AssignmentState state;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date requestedDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date returnedDate;
    private String requestBy;
    private String acceptBy;
    @NotNull
    private Long assignmentId;
    private String assetCode;
    private String assetName;

    public RequestDTO(RequestEntity entity) {
        this.id = entity.getId();
        this.state = entity.getState();
        this.requestedDate = entity.getRequestedDate();
        this.returnedDate = entity.getReturnedDate();
        this.requestBy = entity.getRequestBy().getUserName();
        if(entity.getAcceptBy() != null)
            this.acceptBy = entity.getAcceptBy().getUserName();
        this.assignmentId = entity.getAssignmentEntity().getId();
        this.assetCode = entity.getAssignmentEntity().getAssetEntity().getAssetCode();
        this.assetName = entity.getAssignmentEntity().getAssetEntity().getAssetName();
    }

    public RequestEntity toEntity() {
        RequestEntity entity = new RequestEntity();
        entity.setState(this.state);
        entity.setRequestedDate(entity.getRequestedDate());
        entity.setReturnedDate(entity.getReturnedDate());
        return entity;
    }
}
