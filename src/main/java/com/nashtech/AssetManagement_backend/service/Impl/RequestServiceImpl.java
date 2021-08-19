package com.nashtech.AssetManagement_backend.service.Impl;

import com.nashtech.AssetManagement_backend.dto.AssignmentDTO;
import com.nashtech.AssetManagement_backend.dto.RequestDTO;
import com.nashtech.AssetManagement_backend.entity.*;
import com.nashtech.AssetManagement_backend.exception.ConflictException;
import com.nashtech.AssetManagement_backend.exception.ResourceNotFoundException;
import com.nashtech.AssetManagement_backend.repository.AssignmentRepository;
import com.nashtech.AssetManagement_backend.repository.RequestRepository;
import com.nashtech.AssetManagement_backend.repository.UserRepository;
import com.nashtech.AssetManagement_backend.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequestServiceImpl implements RequestService {
    @Autowired
    RequestRepository requestRepository;

    @Autowired
    AssignmentRepository assignmentRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public RequestDTO create(RequestDTO requestDTO) {
        RequestEntity request = requestDTO.toEntity();

        AssignmentEntity assignment = assignmentRepository.findById(requestDTO.getAssignmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found!"));
        if (assignment.getState() != AssignmentState.ACCEPTED) {
            throw new ConflictException("Assignment must have accepted state!");
        }

        if(assignment.getRequests().size() > 0) {
            throw new ConflictException("Request has already been created!");
        }

        UsersEntity requestBy = userRepository.getByUserName(requestDTO.getRequestBy());

        request.setRequestedDate(new Date());
        request.setAssignmentEntity(assignment);
        request.setRequestBy(requestBy);
        request.setState(RequestState.WAITING_FOR_RETURNING);

        return new RequestDTO(requestRepository.save(request));
    }

    @Override
    public List<RequestDTO> getAllByAdminLocation(String adminUsername) {
        Location location = userRepository.findByUserName(adminUsername).get().getLocation();
        List<RequestDTO> requestDTOs = requestRepository.findAll().stream()
                .filter(request -> (request.getRequestBy().getLocation().equals(location)))
                .sorted((o1, o2) -> (int) (o1.getId() - o2.getId()))
                .map(RequestDTO::new).collect(Collectors.toList());
        return requestDTOs;
    }

}
