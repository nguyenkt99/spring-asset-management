package com.nashtech.AssetManagement_backend.service;

import com.nashtech.AssetManagement_backend.dto.UserDto;
import com.nashtech.AssetManagement_backend.entity.Location;
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

    public UserDto saveUser(UserDto userDto) throws BadRequestException;

    public List<UserDto> retrieveUsers(Location location);

    public List<UserDto> retrieveUsers(Pageable pageable);

    public UserDto getUserByStaffCode(String staffCode, Location location) throws ResourceNotFoundException;

    public UserDto updateUser(UserDto userDto);

//    public String deleteUser(String staffCode) throws ResourceNotFoundException;

    public Location getLocationByUserName(String userName);

    Boolean canDisableUser(String staffCode);

    Boolean disableUser(String staffCode);

    UserDto getProfile(String username);

}
