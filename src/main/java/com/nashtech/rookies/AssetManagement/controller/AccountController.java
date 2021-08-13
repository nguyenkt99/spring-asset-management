package com.nashtech.rookies.AssetManagement.controller;

import com.nashtech.rookies.AssetManagement.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/account")
public class AccountController {
    private final AuthService authService;

    @Autowired
    public AccountController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/change")
    public ResponseEntity<?> changePassword(@Valid @RequestParam("newPassword") String newPassword,
                                            @Valid @RequestParam("userId") String userId){
        return this.authService.changePasswordOnFirstLogin(newPassword, userId);
    }

}
