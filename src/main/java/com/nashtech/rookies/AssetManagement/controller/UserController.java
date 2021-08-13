package com.nashtech.rookies.AssetManagement.controller;

import com.nashtech.rookies.AssetManagement.model.dto.CreateUserRequest;
import com.nashtech.rookies.AssetManagement.model.dto.UserDTO;
import com.nashtech.rookies.AssetManagement.service.AuthService;
import com.nashtech.rookies.AssetManagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    AuthService authService;

    @Autowired
    UserService userService;

    @PostMapping("")
    public ResponseEntity<?> register(@Valid @RequestBody CreateUserRequest request) throws Exception{
        return this.authService.createNew(request,"create");
    }
   @GetMapping("")
    public ResponseEntity<?>getAll(@RequestParam(required = false) String search) throws Exception{
        if(search!=null){
            return this.userService.searchByNameOrId(search);
        }
        return this.userService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?>getUser(@PathVariable("id") String id   ) throws Exception{
        return this.userService.findById(id);
    }

    @PutMapping("")
    public ResponseEntity<?> updateUser(@Valid @RequestBody CreateUserRequest request) throws Exception{
        return this.authService.createNew(request,"update");
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> delete(@PathVariable String userId) {
        return authService.delete(userId.toUpperCase());
    }
}
