package com.nashtech.AssetManagement_backend.service;

import com.nashtech.AssetManagement_backend.dto.UserDto;
import com.nashtech.AssetManagement_backend.entity.Location;
import com.nashtech.AssetManagement_backend.entity.LocationEntity;
import com.nashtech.AssetManagement_backend.entity.UsersEntity;
import com.nashtech.AssetManagement_backend.exception.BadRequestException;
import com.nashtech.AssetManagement_backend.exception.ResourceNotFoundException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    UsersEntity findByUserName(String username);

    UsersEntity findByEmail(String email);

    UserDto changePasswordAfterfirstLogin(String username, String passwordEncode);

    UserDto changePassword(String username, String passwordEncode);

    UserDto saveUser(UserDto userDto, String username) throws BadRequestException;

    List<UserDto> retrieveUsers(LocationEntity location);

    List<UserDto> retrieveUsers(Pageable pageable);

    UserDto getUserById(String id, LocationEntity location) throws ResourceNotFoundException;

    UserDto updateUser(UserDto userDto);

//    public String deleteUser(String id) throws ResourceNotFoundException;

    LocationEntity getLocationByUserName(String userName);

    Boolean canDisableUser(String id);

    Boolean disableUser(String id);

    UserDto getProfile(String username);

}
