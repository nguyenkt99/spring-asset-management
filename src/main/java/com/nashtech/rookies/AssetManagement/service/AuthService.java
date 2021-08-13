package com.nashtech.rookies.AssetManagement.service;




import com.nashtech.rookies.AssetManagement.model.dto.CreateUserRequest;
import com.nashtech.rookies.AssetManagement.model.dto.LoginRequest;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface AuthService {
    public ResponseEntity<?> authenticateUser(LoginRequest request, HttpServletRequest servletRequest) throws Exception;

    public ResponseEntity<?> createNew(CreateUserRequest request,String action) throws Exception;

    public ResponseEntity<?> changePasswordOnFirstLogin(String newPassword, String userId);

    ResponseEntity<?> delete(String userId);
}
