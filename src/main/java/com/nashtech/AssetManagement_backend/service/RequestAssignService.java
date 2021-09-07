package com.nashtech.AssetManagement_backend.service;

import com.nashtech.AssetManagement_backend.dto.RequestAssignDTO;

import java.util.List;

public interface RequestAssignService {
    RequestAssignDTO save(RequestAssignDTO requestAssignDTO);
    List<RequestAssignDTO> getAll(String username);
    RequestAssignDTO updateState(RequestAssignDTO requestAssignDTO);
}
