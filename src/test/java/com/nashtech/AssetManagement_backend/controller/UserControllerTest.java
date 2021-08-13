package com.nashtech.AssetManagement_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nashtech.AssetManagement_backend.dto.AssetDTO;
import com.nashtech.AssetManagement_backend.dto.UserDto;
import com.nashtech.AssetManagement_backend.entity.Gender;
import com.nashtech.AssetManagement_backend.entity.Location;
import com.nashtech.AssetManagement_backend.entity.RoleName;
import com.nashtech.AssetManagement_backend.repository.UserRepository;
import com.nashtech.AssetManagement_backend.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {



    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    private static ObjectMapper mapper = new ObjectMapper();

    UserDto userDto = new UserDto();
    List<UserDto> userDtos = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        userDto.setUsername("name");
        userDto.setStaffCode("staffCode");
        userDto.setGender(Gender.Male);
        userDto.setLocation(Location.HCM);
        userDto.setJoinedDate(new Date());
        userDto.setDateOfBirth(new Date());
        userDto.setLastName("last");
        userDto.setFirstName("first");
        userDto.setType(RoleName.STAFF);


        userDtos.add(userDto);
    }


    @AfterEach
    public void tearDown() {
        userDtos = null;
        userDto = null;
    }



//    @Test
//    public void testGetAllUser () throws Exception {
//
//        Mockito.when(userService.getLocationByUserName("location")).thenReturn(ArgumentMatchers.any());
//        Mockito.when(userService.retrieveUsers(userDto.getLocation())).thenReturn(userDtos);
//
//
//        MvcResult result=  mockMvc.perform(get("/api/users").requestAttr("userName", "username").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
//                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].userName", Matchers.equalTo("name")))
//                .andExpect(jsonPath("$[0].staffCode", Matchers.equalTo("staffCode")))
//                .andDo(print()).andReturn();
//        ;
//
////        System.out.println("Test "+result.getResponse().getContentAsString());
//    }
//
//
//    @Test
//    public void testGetUserByStaffCode() throws Exception {
//
//        Mockito.when(userService.getUserByStaffCode(ArgumentMatchers.anyString(), ArgumentMatchers.any())).thenReturn(userDto);
//
//
//        mockMvc.perform(get("/api/users/" + "1").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
//                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
//                .andExpect(jsonPath("$.userName", Matchers.equalTo("name")))
//                .andExpect(jsonPath("$.staffCode", Matchers.equalTo("staffCode")));
//    }
//
//    @Test
//    public void addUserSuccess() throws Exception {
//
//        ObjectMapper mapper = new ObjectMapper();
//        userDto.setLocation(Location.HN);
//        UsersEntity admin = new UserDto().toEntity(userDto);
//        Mockito.when(userRepository.findByUserName(ArgumentMatchers.anyString())).thenReturn(Optional.ofNullable(admin));
//        Mockito.when(userService.saveUser(ArgumentMatchers.anyObject())).thenReturn(userDto);
//
//        String json = mapper.writeValueAsString(userDto);
//
//        mockMvc.perform(post("/api/users/").contentType(MediaType.APPLICATION_JSON).requestAttr("userName","username")
//                .characterEncoding("utf-8")
//                .content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
//                .andExpect(jsonPath("$.userName", Matchers.equalTo("name")))
//                .andExpect(jsonPath("$.staffCode", Matchers.equalTo("staffCode")))
//                .andExpect(jsonPath("$.location", Matchers.equalTo("HN")));
//
//    }

//    @Test
//    void editUser_ReturnUserDtoJon_WhenSuccessful() throws Exception {
//        userDto.setGender(Gender.Female);
////        userDto.setType(RoleName.STAFF);
////        userDto.setJoinedDate(Date.from(LocalDate.of(2021, 8, 10).atStartOfDay(ZoneId.systemDefault()).toInstant()));
////        userDto.setDateOfBirth(Date.from(LocalDate.of(1999, 2, 12).atStartOfDay(ZoneId.systemDefault()).toInstant()));
//        Mockito.when(userService.updateUser(Mockito.any())).thenReturn(userDto);
//        mockMvc.perform(put("/api/users").contentType(MediaType.APPLICATION_JSON).requestAttr("userName","username")
//                        .characterEncoding("utf-8")
//                        .content(new ObjectMapper().writeValueAsString(userDto)).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
//                .andExpect(jsonPath("$.gender", Matchers.equalTo("Female")));
//
//    }

}
