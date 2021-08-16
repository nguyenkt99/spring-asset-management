package com.nashtech.AssetManagement_backend.service;

import java.util.List;

import com.nashtech.AssetManagement_backend.dto.AssignmentDTO;

public interface AssignmentService {
    List<AssignmentDTO> getAllByAdmimLocation(String username);

    AssignmentDTO save(AssignmentDTO assignmentDTO);
    AssignmentDTO update(AssignmentDTO assignmentDTO);
}
