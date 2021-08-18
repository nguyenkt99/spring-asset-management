package com.nashtech.AssetManagement_backend.service;

import com.nashtech.AssetManagement_backend.dto.UserDto;
import com.nashtech.AssetManagement_backend.payload.request.LoginRequest;

import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<?> authenticateUser(LoginRequest loginRequest);
    UserDto changepassword(String username, String password);
    Boolean forgotpassword(String email);
    String getOTP(String email);
}
