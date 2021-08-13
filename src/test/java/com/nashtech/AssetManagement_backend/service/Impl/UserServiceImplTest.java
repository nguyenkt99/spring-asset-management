package com.nashtech.AssetManagement_backend.service.Impl;

import com.nashtech.AssetManagement_backend.dto.UserDto;
import com.nashtech.AssetManagement_backend.entity.*;
import com.nashtech.AssetManagement_backend.exception.InvalidInputException;
import com.nashtech.AssetManagement_backend.exception.ResourceNotFoundException;
import com.nashtech.AssetManagement_backend.repository.RoleRepository;
import com.nashtech.AssetManagement_backend.repository.UserRepository;
import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

    import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
    import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserServiceImpl userService;


    @Rule
    public final ExpectedException exception = ExpectedException.none();

    RolesEntity rolesEntity = new RolesEntity();
    UsersEntity usersEntity = new UsersEntity();
    List<UsersEntity> usersEntities = new ArrayList<>();
    UserDto userDto = new UserDto();
    List<UserDto> userDtos = new ArrayList<>();




    RolesEntity role = new RolesEntity();

    List<UsersEntity> users = new ArrayList<>();
    @BeforeEach
    void setUp() {
        LocalDate localDate = LocalDate.of( 1999 , 6 , 7 );
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        UsersEntity user = new UsersEntity();
        UsersEntity user1 = new UsersEntity("SD001","Liem","Nguyen", Gender.Male,"liemnv","123456",date,new Date(),
                new Date(), Location.HCM,true,role,null,null,null, UserState.Enable);
        UsersEntity user2 = new UsersEntity("SD002","Nguyen","Kieu", Gender.Male,"liemnv","123456",date,new Date(),
                new Date(), Location.HCM,true,role,null,null,null, UserState.Enable);
        users = Stream.of(user1,user2).collect(Collectors.toList());


        rolesEntity.setId(1L);
        rolesEntity.setName(RoleName.STAFF);
        usersEntity.setUserName("name");
        usersEntity.setUserName("name");
        usersEntity.setStaffCode("staffCode");
        usersEntity.setGender(Gender.Male);
        usersEntity.setLocation(Location.HCM);
        usersEntity.setRole(rolesEntity);
        usersEntity.setLastName("last");
        usersEntity.setFirstName("first");
        usersEntity.setJoinedDate(new Date());
        usersEntity.setDateOfBirth(new Date());
        usersEntity.setState(UserState.Enable);

        usersEntities.add(usersEntity);

        userDto  = new UserDto().toDto(usersEntity);
        userDtos = new UserDto().toListDto(usersEntities);



    }



    @AfterEach
    public void tearDown() {
        userDtos = null;
        userDto = null;
    }



    @Test
    void findByUserName() {
    }

    @Test
    void changepassword() {
    }

    @Test
    void saveUser_Success() {
        Mockito.when(userRepository.save(Mockito.anyObject())).thenReturn(users.get(0));
        Mockito.when(roleRepository.getByName(Mockito.anyObject())).thenReturn(role);
        Mockito.when(userRepository.getByStaffCode(Mockito.anyString())).thenReturn(users.get(0));
        LocalDate localDate = LocalDate.of( 1999 , 6 , 7 );
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        UserDto dto = new UserDto();
        dto.setJoinedDate(new Date());
        dto.setDateOfBirth(date);
        dto.setType(RoleName.STAFF);

        assertEquals(new UserDto().toDto(users.get(0)).getStaffCode(),userService.saveUser(dto).getStaffCode());
    }

    @Test
    void saveUser_Fail_DOB() {
        Mockito.when(userRepository.save(Mockito.anyObject())).thenReturn(users.get(0));
        Mockito.when(roleRepository.getByName(Mockito.anyObject())).thenReturn(role);
        Mockito.when(userRepository.getByStaffCode(Mockito.anyString())).thenReturn(users.get(0));
        LocalDate localDate = LocalDate.of( 2021 , 6 , 7 );
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        UserDto dto = new UserDto();
        dto.setJoinedDate(new Date());
        dto.setDateOfBirth(date);
        dto.setType(RoleName.STAFF);

        Exception exception = assertThrows(InvalidInputException.class,()->{
            userService.saveUser(dto);
        });
        String expectedMessage = "User is under 18.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void saveUser_Fail_SundaySaturday() {
        Mockito.when(userRepository.save(Mockito.anyObject())).thenReturn(users.get(0));
        Mockito.when(roleRepository.getByName(Mockito.anyObject())).thenReturn(role);
        Mockito.when(userRepository.getByStaffCode(Mockito.anyString())).thenReturn(users.get(0));
        LocalDate localDate1 = LocalDate.of( 2021 , 8 , 1 );
        Date joinedDate = Date.from(localDate1.atStartOfDay(ZoneId.systemDefault()).toInstant());
        LocalDate localDate2 = LocalDate.of( 1999 , 04 , 1 );
        Date dOB = Date.from(localDate2.atStartOfDay(ZoneId.systemDefault()).toInstant());

        UserDto dto = new UserDto();
        dto.setJoinedDate(joinedDate);
        dto.setDateOfBirth(dOB);
        dto.setType(RoleName.STAFF);

        Exception exception = assertThrows(InvalidInputException.class,()->{
            userService.saveUser(dto);
        });
        String expectedMessage = "Joined date is Saturday or Sunday";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    void saveUser_Fail_JoinBeforeDOB() {
        Mockito.when(userRepository.save(Mockito.anyObject())).thenReturn(users.get(0));
        Mockito.when(roleRepository.getByName(Mockito.anyObject())).thenReturn(role);
        Mockito.when(userRepository.getByStaffCode(Mockito.anyString())).thenReturn(users.get(0));
        LocalDate localDate1 = LocalDate.of( 1999 , 03 , 1 );
        Date joinedDate = Date.from(localDate1.atStartOfDay(ZoneId.systemDefault()).toInstant());
        LocalDate localDate2 = LocalDate.of( 1999 , 04 , 1 );
        Date dOB = Date.from(localDate2.atStartOfDay(ZoneId.systemDefault()).toInstant());

        UserDto dto = new UserDto();
        dto.setJoinedDate(joinedDate);
        dto.setDateOfBirth(dOB);
        dto.setType(RoleName.STAFF);

        Exception exception = assertThrows(InvalidInputException.class,()->{
            userService.saveUser(dto);
        });
        String expectedMessage = "Joined date is not later than Date of Birth.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void shouldReturnAllUserDto() {

        given(userRepository.findByUserName(userDto.getUsername())).willReturn(Optional.ofNullable(usersEntity));
        when(userRepository.findAllByLocationOrderByFirstNameAscLastNameAsc(usersEntity.getLocation())).thenReturn(usersEntities);

        List<UserDto> userDtos1 = userService.retrieveUsers(userDto.getLocation());
        assertThat(userDtos).usingRecursiveComparison().isEqualTo(userDtos1);
        verify(userRepository, times(1)).findAllByLocationOrderByFirstNameAscLastNameAsc(any());
    }


    @Test
    public void whenGivenstaffCode_shouldReturnUserDto_ifFound() {
        when(userRepository.findByStaffCodeAndLocation(any(), any())).thenReturn(Optional.ofNullable(usersEntity));

        UserDto userDto1 = userService.getUserByStaffCode(any(), any());

        assertThat(userDto1).usingRecursiveComparison().isEqualTo(userDto);
    }

    @Test
    public void whenGivenstaffCode_shouldReturnUserDto_ifNotFound() {
        when(userRepository.findByStaffCodeAndLocation(any(), any())).thenReturn(Optional.ofNullable(null));

        try {
            UserDto userDto1 = userService.getUserByStaffCode(any(), any());
        } catch (ResourceNotFoundException e) {
            exception.expect(ResourceNotFoundException.class);
        }

    }



    @Test
    void updateUser_ThrowError_WhenUnder18() {
        UserDto userDto = new UserDto().toDto(users.get(0));
        userDto.setDateOfBirth(Date.from(LocalDate.of(2005, 2, 12).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        when(userRepository.findByStaffCode(Mockito.anyObject())).thenReturn(Optional.ofNullable(users.get(0)));

        Exception exception = assertThrows(InvalidInputException.class,()->{
            userService.saveUser(userDto);
        });
        String expectedMessage = "User is under 18.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void updateUser_ThrowError_WhenJoinedDateBeforeDob() {
        UserDto userDto = new UserDto().toDto(users.get(0));
        userDto.setJoinedDate(Date.from(LocalDate.of(1999, 1, 12).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        userDto.setDateOfBirth(Date.from(LocalDate.of(1999, 2, 12).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        when(userRepository.findByStaffCode(Mockito.anyObject())).thenReturn(Optional.ofNullable(users.get(0)));

        Exception exception = assertThrows(InvalidInputException.class,()->{
            userService.updateUser(userDto);
        });
        String expectedMessage = "Joined date is not later than Date of Birth";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void updateUser_ThrowError_WhenJoinedDateIsWeekend() {
        UserDto userDto = new UserDto().toDto(users.get(0));
        userDto.setJoinedDate(Date.from(LocalDate.of(2021, 8, 8).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        when(userRepository.findByStaffCode(Mockito.anyObject())).thenReturn(Optional.ofNullable(users.get(0)));

        Exception exception = assertThrows(InvalidInputException.class,()->{
            userService.updateUser(userDto);
        });
        String expectedMessage = "Joined date is Saturday or Sunday";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void updateUser_ThrowError_WhenUserNotFound() {
        UserDto userDto = new UserDto().toDto(users.get(0));
        userDto.setStaffCode("0x0001");
        when(userRepository.findByStaffCode(Mockito.anyObject())).thenThrow(new ResourceNotFoundException("User not found for this staff code: " + userDto.getStaffCode()));
        Exception exception = assertThrows(ResourceNotFoundException.class,()->{
            userService.updateUser(userDto);
        });
        String expectedMessage = "User not found for this staff code";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void updateUser_ReturnUserDto_WhenSuccessful() {
        UserDto userDto = new UserDto().toDto(users.get(0));
        userDto.setGender(Gender.Female);
        userDto.setJoinedDate(Date.from(LocalDate.of(2021, 8, 10).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        userDto.setDateOfBirth(Date.from(LocalDate.of(1999, 2, 12).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        when(userRepository.findByStaffCode(Mockito.anyObject())).thenReturn(Optional.ofNullable(users.get(0)));
        Mockito.when(roleRepository.getByName(Mockito.anyObject())).thenReturn(role);
        Mockito.when(userRepository.save(Mockito.anyObject())).thenReturn(users.get(0));
        assertThat(userDto)
                .usingRecursiveComparison()
                .isEqualTo(userService.updateUser(userDto));
    }

    @Test
    void deleteUser() {
    }
}