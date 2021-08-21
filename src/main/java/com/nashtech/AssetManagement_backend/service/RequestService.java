package com.nashtech.AssetManagement_backend.service;

import com.nashtech.AssetManagement_backend.dto.RequestDTO;
import com.nashtech.AssetManagement_backend.entity.UsersEntity;

import java.util.List;

public interface RequestService {
    RequestDTO create(RequestDTO requestDTO);

    List<RequestDTO> getAllByAdminLocation(String adminUsername);

    void delete(Long id);

    void accept(Long requestId, String staffCode);
}

