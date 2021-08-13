package com.nashtech.AssetManagement_backend.service.Impl;

import com.nashtech.AssetManagement_backend.dto.UserDto;
import com.nashtech.AssetManagement_backend.entity.Location;
import com.nashtech.AssetManagement_backend.entity.RolesEntity;
import com.nashtech.AssetManagement_backend.entity.UserState;
import com.nashtech.AssetManagement_backend.entity.UsersEntity;
import com.nashtech.AssetManagement_backend.exception.BadRequestException;
import com.nashtech.AssetManagement_backend.exception.ConflictException;
import com.nashtech.AssetManagement_backend.exception.InvalidInputException;
import com.nashtech.AssetManagement_backend.exception.ResourceNotFoundException;
import com.nashtech.AssetManagement_backend.handleException.NotFoundExecptionHandle;
import com.nashtech.AssetManagement_backend.repository.RoleRepository;
import com.nashtech.AssetManagement_backend.repository.UserRepository;
import com.nashtech.AssetManagement_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UsersEntity findByUserName(String username) {
        return userRepository.findByUserName(username)
                .orElseThrow(() -> new NotFoundExecptionHandle("Could not found user: " + username));
    }

    @Override
    public UserDto changepassword(String username, String passwordEncode) {
        UsersEntity existUser = findByUserName(username);
        existUser.setPassword(passwordEncode);
        existUser.setFirstLogin(false);

        try {
            UsersEntity user = userRepository.save(existUser);
            return new UserDto().toDto(user);
        } catch (Exception e) {
            throw new BadRequestException("invalid Request");
        }
    }


    @Override
    public UserDto saveUser(UserDto userDto) throws BadRequestException {
        UsersEntity usersEntity = userDto.toEntity(userDto);
        //validate
        if (usersEntity.getJoinedDate().before(usersEntity.getDateOfBirth()))
            throw new InvalidInputException("Joined date is not later than Date of Birth. Please select a different date");
        if (!checkAge(usersEntity.getDateOfBirth(), usersEntity.getJoinedDate()))
            throw new InvalidInputException("User is under 18. Please select a different date");
        int day = getDayNumberOld(usersEntity.getJoinedDate());
        if (day == 7 || day == 1)
            throw new InvalidInputException("Joined date is Saturday or Sunday. Please select a different date");

        usersEntity.setFirstLogin(true);
        RolesEntity rolesEntity = roleRepository.getByName(userDto.getType());
        usersEntity.setRole(rolesEntity);
        usersEntity = userRepository.save(usersEntity);
        return new UserDto().toDto(userRepository.getByStaffCode(usersEntity.getStaffCode()));
    }

    @Override
    public List<UserDto> retrieveUsers(Location location) {
        List<UsersEntity> usersEntities = userRepository.findAllByLocation(location);
        usersEntities = usersEntities.stream().sorted(Comparator.comparing(o -> (o.getFirstName() + ' ' + o.getLastName())))
                .collect(Collectors.toList());
        return new UserDto().toListDto(usersEntities);
    }

    @Override
    public List<UserDto> retrieveUsers(Pageable pageable) {
        List<UsersEntity> usersEntities = new ArrayList<>();
        Page<UsersEntity> page;
        page = userRepository.findAll(pageable);

        usersEntities = page.getContent();
        return new UserDto().toListDto(usersEntities);
    }

    @Override
    public UserDto getUserByStaffCode(String staffCode, Location location) throws ResourceNotFoundException {
        UsersEntity user = userRepository.findByStaffCodeAndLocation(staffCode, location)
                .orElseThrow(() -> new ResourceNotFoundException("user not found for this staff code: " + staffCode));
        return new UserDto().toDto(user);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        UsersEntity existUser = userRepository.findByStaffCode(userDto.getStaffCode())
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this staff code: " + userDto.getStaffCode()));

        if (userDto.getJoinedDate().before(userDto.getDateOfBirth()))
            throw new InvalidInputException("Joined date is not later than Date of Birth. Please select a different date");
        if (!checkAge(userDto.getDateOfBirth(), userDto.getJoinedDate()))
            throw new InvalidInputException("User is under 18. Please select a different date");
        int day = getDayNumberOld(userDto.getJoinedDate());
        if (day == 7 || day == 1)
            throw new InvalidInputException("Joined date is Saturday or Sunday. Please select a different date");

        existUser.setDateOfBirth(userDto.getDateOfBirth());
        existUser.setGender(userDto.getGender());
        existUser.setJoinedDate(userDto.getJoinedDate());

        RolesEntity rolesEntity = roleRepository.getByName(userDto.getType());
        existUser.setRole(rolesEntity);

        UsersEntity user = userRepository.save(existUser);
        return new UserDto().toDto(user);
    }

    @Override
    public String deleteUser(String staffCode) throws ResourceNotFoundException {
        userRepository.findByStaffCode(staffCode)
                .orElseThrow(() -> new ResourceNotFoundException("user not found for this staff code: " + staffCode));
        userRepository.deleteByStaffCode(staffCode);
        return "deleted";
    }

    public Location getLocationByUserName(String userName) {
        return userRepository.getByUserName(userName).getLocation();
    }

    private final String USER_NOT_FOUND="user is not found.";
    private final String DISABLE_CONFLICT="There are valid assignments belonging to this user. Please close all assignments before disabling user.";

    @Override
    public Boolean canDisableUser(String staffCode) {
        return !(userRepository.findByStaffCode(staffCode)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND))
                .getAssignmentEntityTos().size()>0);
    }

    @Override
    public Boolean disableUser(String staffCode) {
        UsersEntity usersEntity = userRepository.findByStaffCode(staffCode)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
        if (usersEntity.getAssignmentEntityTos().size()>0){
            throw new ConflictException(DISABLE_CONFLICT);
        }
        usersEntity.setState(UserState.Disabled);
        userRepository.save(usersEntity);
        return true;
    }

    // number ranges from 1 (Sunday) to 7 (Saturday)
    private int getDayNumberOld(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    // check age >=18
    private boolean checkAge(Date dOB, Date joinDate) {
        LocalDate date1 = dOB.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate date2 = joinDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate now = LocalDate.now();
        Period period = Period.between(date1, date2);
        int temp = period.getYears();
        return period.getYears() >= 18 ? true : false;
    }
}
