package com.nashtech.AssetManagement_backend.controller;


import com.nashtech.AssetManagement_backend.dto.UserDto;
import com.nashtech.AssetManagement_backend.entity.Location;
import com.nashtech.AssetManagement_backend.repository.UserRepository;
import com.nashtech.AssetManagement_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;


    @GetMapping("")
    public ResponseEntity<List<UserDto>> getAll (HttpServletRequest request) {




        String userName = (String) request.getAttribute("userName");
        Location location = userService.getLocationByUserName(userName);

        List<UserDto> userDtos = userService.retrieveUsers(location);



        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }

    @GetMapping("/{staffCode}")
    public ResponseEntity<UserDto> getUserById(HttpServletRequest request,

                                               @PathVariable("staffCode") String staffCode) {



        String userName = (String) request.getAttribute("userName");
        Location location = userService.getLocationByUserName(userName);

        UserDto userDto = userService.getUserByStaffCode(staffCode, location);
        return new ResponseEntity<>(userDto, HttpStatus.OK);

    }


//    {
//        "firstName": "Tan",
//            "lastName": "Vo Dinh",
//            "gender": "Male",
//            "location": "HCM",
//            "dateOfBirth": "2020-01-01T12:00:27.87+00:20",
//            "joinedDate": "2020-01-01T12:00:27.87+00:20",
//            "role": "Staff"
//    }

    @PostMapping("")
    public ResponseEntity<UserDto> addUser( HttpServletRequest request, @Valid @RequestBody UserDto userDto) throws ParseException {

        String userName = (String) request.getAttribute("userName");
        //set location staff = location admin create;
        userDto.setLocation(userRepository.findByUserName(userName).get().getLocation());
        return new ResponseEntity<>(userService.saveUser(userDto), HttpStatus.CREATED);

    }
    @PutMapping("/{staffCode}")
    public ResponseEntity<UserDto> editUser(@PathVariable("staffCode") String staffCode, @RequestBody UserDto userDto) {
        userDto.setStaffCode(staffCode);
        UserDto updateUser = userService.updateUser(userDto);
        return new ResponseEntity<>(updateUser, HttpStatus.OK);
    }


    @DeleteMapping("/{staffCode}")
    public ResponseEntity<Map<String, Boolean>> deleteCategory(HttpServletRequest request,
                                                               @PathVariable("staffCode") String staffCode)  {

        userService.deleteUser(staffCode);

        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/disable/{staffCode}")
    public ResponseEntity<Boolean> canDisableUser(@PathVariable("staffCode") String staffCode){
        return ResponseEntity.ok().body(userService.canDisableUser(staffCode));
    }

    @PostMapping("/disable/{staffCode}")
    public ResponseEntity<Boolean> disableUser(@PathVariable("staffCode") String staffCode){
        return ResponseEntity.ok().body(userService.disableUser(staffCode));
    }

}
