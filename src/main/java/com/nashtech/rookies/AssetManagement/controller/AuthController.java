package com.nashtech.rookies.AssetManagement.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.nashtech.rookies.AssetManagement.model.dto.CreateUserRequest;
import com.nashtech.rookies.AssetManagement.service.AuthService;
import com.nashtech.rookies.AssetManagement.model.dto.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, HttpServletRequest servletRequest) throws Exception{
        return this.authService.authenticateUser(request, servletRequest);
    }



}
