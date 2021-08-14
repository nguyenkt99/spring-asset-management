package com.nashtech.AssetManagement_backend.service.Impl;

import com.nashtech.AssetManagement_backend.dto.AssignmentDTO;
import com.nashtech.AssetManagement_backend.entity.*;
import com.nashtech.AssetManagement_backend.exception.ConflictException;
import com.nashtech.AssetManagement_backend.exception.ResourceNotFoundException;
import com.nashtech.AssetManagement_backend.repository.AssetRepository;
import com.nashtech.AssetManagement_backend.repository.AssignmentRepository;
import com.nashtech.AssetManagement_backend.repository.UserRepository;
import com.nashtech.AssetManagement_backend.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AssignmentServiceImpl implements AssignmentService {
    @Autowired
    AssignmentRepository assignmentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AssetRepository assetRepository;

    @Override
    public AssignmentDTO save(AssignmentDTO assignmentDTO) {
        AssignmentEntity assignment = assignmentDTO.toEntity();
        UsersEntity assignTo = userRepository.findByUserName(assignmentDTO.getAssignTo())
                .orElseThrow(() -> new ResourceNotFoundException("AssignTo not found!"));
        UsersEntity assignBy = userRepository.findByUserName(assignmentDTO.getAssignBy())
                .orElseThrow(() -> new ResourceNotFoundException("AssignBy not found!"));

        AssetEntity asset = assetRepository.findById(assignmentDTO.getAssetCode())
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found!"));
        if(asset.getState() != AssetState.AVAILABLE) {
            throw new ConflictException("Asset must available state!");
        }

        asset.setState(AssetState.ASSIGNED);
        assignment.setAssetEntity(asset);
        assignment.setState(AssignmentState.WAITING_FOR_ACCEPTANCE);
        assignment.setAssignTo(assignTo);
        assignment.setAssignBy(assignBy);
        if(assignmentDTO.getAssignedDate() == null)
            assignment.setAssignedDate(new Date());

        return new AssignmentDTO(assignmentRepository.save(assignment));
    }
}
