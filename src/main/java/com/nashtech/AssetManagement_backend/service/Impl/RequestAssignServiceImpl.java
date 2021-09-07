package com.nashtech.AssetManagement_backend.service.Impl;

import com.nashtech.AssetManagement_backend.dto.AssignmentDTO;
import com.nashtech.AssetManagement_backend.dto.RequestAssignDTO;
import com.nashtech.AssetManagement_backend.entity.*;
import com.nashtech.AssetManagement_backend.exception.BadRequestException;
import com.nashtech.AssetManagement_backend.exception.ConflictException;
import com.nashtech.AssetManagement_backend.exception.ResourceNotFoundException;
import com.nashtech.AssetManagement_backend.repository.*;
import com.nashtech.AssetManagement_backend.service.AssignmentService;
import com.nashtech.AssetManagement_backend.service.RequestAssignService;
import com.nashtech.AssetManagement_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequestAssignServiceImpl implements RequestAssignService {
    @Autowired
    RequestAssignRepository requestAssignRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public RequestAssignDTO save(RequestAssignDTO requestAssignDTO) {
        RequestAssignEntity requestAssign = requestAssignDTO.toEntity();

        CategoryEntity category = categoryRepository.findById(requestAssignDTO.getPrefix())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found!"));
        UserDetailEntity requestBy = userRepository.findByUserName(requestAssignDTO.getRequestedBy())
                .orElseThrow(() -> new ResourceNotFoundException("RequestBy not found!")).getUserDetail();

        requestAssign.setState(RequestAssignState.WAITING_FOR_ASSIGNING);
        requestAssign.setCategoryEntity(category);
        requestAssign.setRequestBy(requestBy);
        requestAssign.setRequestedDate(new Date());
        return new RequestAssignDTO(requestAssignRepository.save(requestAssign));
    }

    @Override
    public List<RequestAssignDTO> getAll(String username) {
        UserDetailEntity user = userRepository.findByUserName(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!")).getUserDetail();

        List<RequestAssignEntity> requestAssignEntities = new ArrayList<>();
        if(user.getUser().getRole().getName().equals(RoleName.ROLE_STAFF)) {
            requestAssignEntities = requestAssignRepository.findAllByRequestBy_StaffCode(user.getStaffCode());
        } else {
            requestAssignEntities = requestAssignRepository.findAllByRequestBy_Location(user.getLocation());
        }

        return requestAssignEntities.stream().map(RequestAssignDTO::new).collect(Collectors.toList());
    }

    @Override
    public RequestAssignDTO updateState(RequestAssignDTO requestAssignDTO) {
        RequestAssignEntity requestAssign = requestAssignRepository.findById(requestAssignDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Request for assigning not found!"));
        if (requestAssign.getState() != RequestAssignState.WAITING_FOR_ASSIGNING)
            throw new BadRequestException("Request for assigning can be update when state is waiting for assigning!");

        if(requestAssignDTO.getState().equals(RequestAssignState.DECLINED)) {
            requestAssign.setNote(requestAssignDTO.getNote());
        }

        requestAssign.setState(requestAssignDTO.getState());
        return new RequestAssignDTO(requestAssignRepository.save(requestAssign));
    }

}
