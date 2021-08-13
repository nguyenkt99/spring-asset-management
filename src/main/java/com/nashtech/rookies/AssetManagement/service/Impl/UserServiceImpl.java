package com.nashtech.rookies.AssetManagement.service.Impl;

import com.nashtech.rookies.AssetManagement.exception.EmptyException;
import com.nashtech.rookies.AssetManagement.exception.NotFoundException;
import com.nashtech.rookies.AssetManagement.model.dto.CreateUserRequest;
import com.nashtech.rookies.AssetManagement.model.dto.UserDetailDTO;
import com.nashtech.rookies.AssetManagement.model.entity.UserDetail;
import com.nashtech.rookies.AssetManagement.repository.UserDetailRepository;
import com.nashtech.rookies.AssetManagement.service.AuthService;
import com.nashtech.rookies.AssetManagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;



import java.util.*;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDetailRepository userDetailRepository;


    @Override
    public ResponseEntity<?> getAll() throws Exception {
        List<UserDetail> userList = userDetailRepository.findAll();
        if(userList==null){
            throw new NotFoundException("Don't have any user!");
        }
        return returnListUser(userList);
    }

    @Override
    public ResponseEntity<?> findById(String id) throws Exception {
        if(id==null || id.trim().isEmpty()){
            throw new EmptyException("Id must not be empty");
        }
        Optional<UserDetail> entity = Optional.ofNullable(userDetailRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(UserDetail.class, id)));
        UserDetailDTO userDetail = new UserDetailDTO(entity.get().getId(),entity.get().getLastName()+" " +
                entity.get().getFirstName(),entity.get().getAccount().getUsername(),
                entity.get().getDateOfBirth(),entity.get().getGender(),
                entity.get().getJoinDate(),entity.get().getAccount().getRole().getName().toString(),
                entity.get().getLocation(),entity.get().getAccount().getAccountId());
        return ResponseEntity.ok(userDetail);
    }

    @Override
    public ResponseEntity<?> searchByNameOrId(String searchStr) throws Exception {
        List<UserDetail> userList = userDetailRepository
                .findByIdOrFirstNameContainingOrLastNameContaining(searchStr,searchStr,searchStr);
        if(userList==null){
            throw new NotFoundException("Don't have any user!");
        }
        return returnListUser(userList);
    }

    private ResponseEntity<?> returnListUser(List<UserDetail> userList){
        List<UserDetailDTO> detailList = new ArrayList<>();
        for (int i = 0; i<userList.size();i++){
            UserDetail tempUs = userList.get(i);
            detailList.add(new UserDetailDTO(tempUs.getId(),
            tempUs.getLastName() +" "+ tempUs.getFirstName(),
                    tempUs.getAccount().getUsername(),tempUs.getDateOfBirth(),tempUs.getGender(),tempUs.getJoinDate(),tempUs.getAccount().getRole().getName().toString(),
                    tempUs.getLocation()));
        }
        detailList.sort((UserDetailDTO a, UserDetailDTO b)->{
            return a.getStaffCode().compareTo(b.getStaffCode());
        });
        return ResponseEntity.ok(detailList);
    }




}
