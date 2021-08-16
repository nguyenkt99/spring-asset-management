package com.nashtech.AssetManagement_backend.service.Impl;

import com.nashtech.AssetManagement_backend.dto.AssignmentDTO;
import com.nashtech.AssetManagement_backend.entity.*;
import com.nashtech.AssetManagement_backend.exception.ConflictException;
import com.nashtech.AssetManagement_backend.exception.ResourceNotFoundException;
import com.nashtech.AssetManagement_backend.repository.AssetRepository;
import com.nashtech.AssetManagement_backend.repository.AssignmentRepository;
import com.nashtech.AssetManagement_backend.repository.UserRepository;
import com.nashtech.AssetManagement_backend.service.AssignmentService;
import com.nashtech.AssetManagement_backend.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssignmentServiceImpl implements AssignmentService {
    @Autowired
    AssignmentRepository assignmentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AssetRepository assetRepository;

    @Autowired
    UserService userService;

    @Override
    public List<AssignmentDTO> getAllByAdmimLocation(String username) {
        Location location = userService.findByUserName(username).getLocation();
        List<AssignmentDTO> assignmentDTOs = assignmentRepository.findAll().stream()
                .filter(p -> (p.getAssignBy().getLocation().equals(location)
                        && !p.getState().equals(AssignmentState.COMPLETED)
                        && !p.getState().equals(AssignmentState.CANCELED_ASSIGN)))
                .map(AssignmentDTO::toDTO).collect(Collectors.toList());
        assignmentDTOs.sort(Comparator.comparing(AssignmentDTO::getAssignedDate));
        return assignmentDTOs;
    }

    @Override
    public AssignmentDTO save(AssignmentDTO assignmentDTO) {
        AssignmentEntity assignment = AssignmentDTO.toEntity(assignmentDTO);
        UsersEntity assignTo = userRepository.findByUserName(assignmentDTO.getAssignedTo())
                .orElseThrow(() -> new ResourceNotFoundException("AssignTo not found!"));
        UsersEntity assignBy = userRepository.findByUserName(assignmentDTO.getAssignedBy())
                .orElseThrow(() -> new ResourceNotFoundException("AssignBy not found!"));

        AssetEntity asset = assetRepository.findById(assignmentDTO.getAssetCode())
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found!"));
        if (asset.getState() != AssetState.AVAILABLE) {
            throw new ConflictException("Asset must available state!");
        }

        asset.setState(AssetState.ASSIGNED);
        assignment.setAssetEntity(asset);
        assignment.setState(AssignmentState.WAITING_FOR_ACCEPTANCE);
        assignment.setAssignTo(assignTo);
        assignment.setAssignBy(assignBy);
        if (assignmentDTO.getAssignedDate() == null)
            assignment.setAssignedDate(new Date());

        return AssignmentDTO.toDTO(assignmentRepository.save(assignment));
    }

    @Override
    public AssignmentDTO update(AssignmentDTO assignmentDTO) {
        AssignmentEntity assignment = assignmentRepository.findById(assignmentDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found!"));
        if (assignment.getState() != AssignmentState.WAITING_FOR_ACCEPTANCE) {
            throw new ConflictException("Assignment is editable while in state Waiting for acceptance!");
        }

        // case: new asset
        if (!assignmentDTO.getAssetCode().equalsIgnoreCase(assignment.getAssetEntity().getAssetCode())) {
            AssetEntity asset = assetRepository.findById(assignmentDTO.getAssetCode())
                    .orElseThrow(() -> new ResourceNotFoundException("Asset not found!"));
            if (asset.getState() != AssetState.AVAILABLE) {
                throw new ConflictException("Asset must available state!");
            }
            assignment.getAssetEntity().setState(AssetState.AVAILABLE); // return state for old asset
            asset.setState(AssetState.ASSIGNED);
            assignment.setAssetEntity(asset);
        }

        // case: new assign to
        if (!assignment.getAssignTo().getUserName().equalsIgnoreCase(assignmentDTO.getAssignedTo())) {
            UsersEntity assignTo = userRepository.findByUserName(assignmentDTO.getAssignedTo())
                    .orElseThrow(() -> new ResourceNotFoundException("AssignTo not found!"));
            assignment.setAssignTo(assignTo);
        }

        // case: new assign by
        if (!assignment.getAssignBy().getUserName().equalsIgnoreCase(assignmentDTO.getAssignedBy())) {
            UsersEntity assignBy = userRepository.findByUserName(assignmentDTO.getAssignedBy())
                    .orElseThrow(() -> new ResourceNotFoundException("AssignBy not found!"));
            assignment.setAssignTo(assignBy);
        }

        assignment.setNote(assignmentDTO.getNote());
        if (assignmentDTO.getAssignedDate() != null)
            assignment.setAssignedDate(assignmentDTO.getAssignedDate());

        return AssignmentDTO.toDTO(assignmentRepository.save(assignment));
    }

}
