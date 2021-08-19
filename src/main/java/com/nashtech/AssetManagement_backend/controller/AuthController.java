package com.nashtech.AssetManagement_backend.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import com.nashtech.AssetManagement_backend.dto.UserDto;
import com.nashtech.AssetManagement_backend.payload.request.ChangePasswordRequest;
import com.nashtech.AssetManagement_backend.payload.request.LoginRequest;
import com.nashtech.AssetManagement_backend.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
//@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.authenticateUser(loginRequest);
    }

    @PostMapping("/firstlogin")
    @ResponseBody
    public UserDto changePasswordAfterfirstLogin(Authentication authentication, @RequestBody Map<String, Object> password) {
        return authService.changePasswordAfterfirstLogin(authentication.getName(), password.get("password").toString());
    }
    @PostMapping("/forgotPassword")
    @ResponseBody
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String,Object> email) {
        Map<String,String> map=new HashMap<>();
        if(authService.forgotpassword(email.get("email").toString()))
        {
            map.put("message","Your new password has been sent to your email");
            return ResponseEntity.ok(map);

        }else
        {
            map.put("message","Something went wrong");
            return ResponseEntity.badRequest().body(map);
        }

    }
    @PostMapping("/otp")
	@ResponseBody
    public ResponseEntity<?> getOTP(@RequestBody Map<String,Object> email) {
        Map<String,String> map=new HashMap<>();
        String OTP =authService.getOTP(email.get("email").toString());
        if(OTP.equals(""))
        {
            map.put("message","Something went wrong");
            return ResponseEntity.badRequest().body(map);
        }else
        {
            map.put("message",OTP);
            return ResponseEntity.ok(map);
        }

    }
        @PostMapping("/password")
	@ResponseBody
    public Boolean changepassword(Authentication authentication, @Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        return authService.changepassword(authentication.getName(), changePasswordRequest.getOldPassword(), changePasswordRequest.getNewPassword());
}}