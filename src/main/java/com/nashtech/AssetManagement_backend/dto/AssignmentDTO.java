package com.nashtech.AssetManagement_backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nashtech.AssetManagement_backend.entity.AssignmentEntity;
import com.nashtech.AssetManagement_backend.entity.AssignmentState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentDTO {
    private Long id;
    private String assetCode;
    private String note;
    private AssignmentState state;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date assignedDate;
    private String assignTo;
    private String assignBy;

    public AssignmentDTO(AssignmentEntity entity) {
        this.id = entity.getId();
        this.assetCode = entity.getAssetEntity().getAssetCode();
        this.note = entity.getNote();
        this.state = entity.getState();
        this.assignedDate = entity.getAssignedDate();
        this.assignTo = entity.getAssignTo().getUserName();
        this.assignBy = entity.getAssignBy().getUserName();
    }

    public AssignmentEntity toEntity() {
        AssignmentEntity assignment = new AssignmentEntity();
        assignment.setNote(this.note);
        assignment.setState(this.state);
        assignment.setAssignedDate(this.assignedDate);
        // id, assetCode, assignTo, assignBy
        return assignment;
    }
}
