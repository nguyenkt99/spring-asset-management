package com.nashtech.AssetManagement_backend.service.Impl;

import com.nashtech.AssetManagement_backend.dto.AssignmentDTO;
import com.nashtech.AssetManagement_backend.entity.*;
import com.nashtech.AssetManagement_backend.exception.BadRequestException;
import com.nashtech.AssetManagement_backend.exception.ConflictException;
import com.nashtech.AssetManagement_backend.exception.ResourceNotFoundException;
import com.nashtech.AssetManagement_backend.repository.AssetRepository;
import com.nashtech.AssetManagement_backend.repository.AssignmentRepository;
import com.nashtech.AssetManagement_backend.repository.UserRepository;
import com.nashtech.AssetManagement_backend.service.AssignmentService;
import com.nashtech.AssetManagement_backend.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.util.*;
import java.util.concurrent.TimeUnit;
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
        List<AssignmentDTO> assignmentDTOs = assignmentRepository.findAllByAdmimLocation(location)
                .stream().map(AssignmentDTO::toDTO).collect(Collectors.toList());
        return assignmentDTOs;
    }

    @Override
    public List<AssignmentDTO> getAssignmentsByUser(String username) {
        List<AssignmentDTO> assignmentDTOs = assignmentRepository.findAssignmentsByUser(username)
                .stream().map(AssignmentDTO::toDTO).collect(Collectors.toList());
        assignmentDTOs.sort(Comparator.comparing(AssignmentDTO::getId));
        return assignmentDTOs;
    }

    @Override
    public AssignmentDTO getAssignmentById(Long assignmentId) {
        AssignmentEntity assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found!"));
        return AssignmentDTO.toDTO(assignment);
    }

    @Override
    public AssignmentDTO save(AssignmentDTO assignmentDTO) {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        String checkDate = checkDate(format.format(assignmentDTO.getAssignedDate()));
        if (!checkDate.equals("")) {
            throw new DateTimeException(checkDate);
        }
        AssignmentEntity assignment = AssignmentDTO.toEntity(assignmentDTO);
        UsersEntity assignTo = userRepository.findByUserName(assignmentDTO.getAssignedTo())
                .orElseThrow(() -> new ResourceNotFoundException("AssignTo not found!"));
        UsersEntity assignBy = userRepository.findByUserName(assignmentDTO.getAssignedBy())
                .orElseThrow(() -> new ResourceNotFoundException("AssignBy not found!"));

        if (assignTo.getLocation() != assignBy.getLocation()) {
            throw new ConflictException("The location of assignTo difference from admin!");
        }

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
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        String checkDate = checkDate(format.format(assignmentDTO.getAssignedDate()));
        if (!checkDate.equals("")) {
            throw new DateTimeException(checkDate);
        }
        AssignmentEntity assignment = assignmentRepository.findById(assignmentDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found!"));
        if (assignment.getState() != AssignmentState.WAITING_FOR_ACCEPTANCE) {
            throw new ConflictException("Assignment is editable while in state Waiting for acceptance!");
        }
        UsersEntity assignTo = assignment.getAssignTo();
        UsersEntity assignBy;

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
            assignTo = userRepository.findByUserName(assignmentDTO.getAssignedTo())
                    .orElseThrow(() -> new ResourceNotFoundException("AssignTo not found!"));
            assignment.setAssignTo(assignTo);
        }

        // case: new assign by
        if (!assignment.getAssignBy().getUserName().equalsIgnoreCase(assignmentDTO.getAssignedBy())) {
            assignBy = userRepository.findByUserName(assignmentDTO.getAssignedBy())
                    .orElseThrow(() -> new ResourceNotFoundException("AssignBy not found!"));
            assignment.setAssignTo(assignBy);

            // check location
            if (assignTo.getLocation() != assignBy.getLocation()) {
                throw new ConflictException("The location of assignTo difference from admin!");
            }
        }

        assignment.setNote(assignmentDTO.getNote());
        if (assignmentDTO.getAssignedDate() != null)
            assignment.setAssignedDate(assignmentDTO.getAssignedDate());

        return AssignmentDTO.toDTO(assignmentRepository.save(assignment));
    }

    @Override
    public boolean deleteAssignment(Long assignmentId, Location location) {

        AssignmentEntity assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found!"));

        if (assignment.getAssignBy().getLocation() != location) {
            throw new BadRequestException("Invalid access!");
        }

        if (assignment.getState() != AssignmentState.WAITING_FOR_ACCEPTANCE) {
            throw new BadRequestException("Assignment delete when state is Waiting for acceptance!");
        }

        AssetEntity assetEntity = assetRepository.getById(assignment.getAssetEntity().getAssetCode());
        assetEntity.setState(AssetState.AVAILABLE);
        assetRepository.save(assetEntity);

        assignmentRepository.deleteById(assignmentId);

        return true;

    }

    @Override
    public AssignmentDTO updateStateAssignment(Long assignmentId, String username, AssignmentState state) {
        AssignmentEntity assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found!"));
        if (!assignment.getAssignTo().getUserName().equals(username))
            throw new BadRequestException("Assignment updated when it assigns you!");
        if (assignment.getState() != AssignmentState.WAITING_FOR_ACCEPTANCE)
            throw new BadRequestException("Assignment updated when state is Waiting for acceptance!");
        if (state != AssignmentState.ACCEPTED && state != AssignmentState.CANCELED_ASSIGN)
            throw new BadRequestException("Assignment can not be updated!");
        assignment.setState(state);
        return AssignmentDTO.toDTO(assignmentRepository.save(assignment));
    }
    private String checkDate(String date) {
        Date currentDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        format.setLenient(false);
        Date assignedDate = new Date();
        try {
            assignedDate = format.parse(date);
        } catch (Exception ex) {
            return "Wrong date! (dd/MM/yyyy)";
        }

        long diff = TimeUnit.DAYS.convert(assignedDate.getTime() - currentDate.getTime(), TimeUnit.MILLISECONDS);
        if (diff < 0) {
            return "Assigned Date need to be equal or bigger than current date";
        }
        return "";
    }
}
