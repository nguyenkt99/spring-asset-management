package com.nashtech.AssetManagement_backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nashtech.AssetManagement_backend.entity.Gender;
import com.nashtech.AssetManagement_backend.entity.Location;
import com.nashtech.AssetManagement_backend.entity.RoleName;
import com.nashtech.AssetManagement_backend.entity.UserState;
import com.nashtech.AssetManagement_backend.entity.UsersEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String staffCode;

    @Size(max = 50)
    @NotBlank(message = "firstname can't not be blank")
    private String firstName;

    @Size(max = 50)
    @NotBlank(message = "lastname can't not be blank")
    private String lastName;

    @NotNull(message = "gender date is not null")
    private Gender gender;

    private String fullName;

    private String username;

    @NotNull(message = "dateOfBirth date is not null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date dateOfBirth;

    @NotNull(message = "joinded date is not null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date joinedDate;

    // @NotNull(message = "location is not chosen")
    private Location location;

    private boolean isFirstLogin;

    @NotNull(message = "Role name can not be null")
    private RoleName type;

    @NotNull(message = "User State can not be null")
    private UserState state ;

    public UserDto toDto(UsersEntity entity) {
        UserDto dto = new UserDto();
        dto.setLastName(entity.getLastName());
        dto.setFirstName(entity.getFirstName());
        dto.setDateOfBirth(entity.getDateOfBirth());
        dto.setJoinedDate(entity.getJoinedDate());
        dto.setLocation(entity.getLocation());
        dto.setType(entity.getRole().getName());
        dto.setState(entity.getState());
        dto.setGender(entity.getGender());
        dto.setStaffCode(entity.getStaffCode());
        dto.setUsername(entity.getUserName());
        dto.setFirstLogin(entity.isFirstLogin());
        dto.setFullName(entity.getFirstName() + " " + entity.getLastName());
        return dto;
    }

    public UsersEntity toEntity(UserDto dto) {
        UsersEntity entity = new UsersEntity();
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setDateOfBirth(dto.getDateOfBirth());
        entity.setJoinedDate(dto.getJoinedDate());
        entity.setGender(dto.getGender());
        entity.setLocation(dto.getLocation());
        entity.setFirstLogin(dto.isFirstLogin);
        entity.setState(dto.getState());
        return entity;
    }

    public List<UserDto> toListDto(List<UsersEntity> listEntity) {
        List<UserDto> listDto = new ArrayList<>();

        listEntity.forEach(e -> {
            listDto.add(this.toDto(e));
        });

        return listDto;
    }

}
