package com.nashtech.rookies.AssetManagement.model.dto;

import com.nashtech.rookies.AssetManagement.constant.Gender;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserDetailDTO {
    private String staffCode;
    private String fullName;
    private String userName;
    private Date dob;
    private Gender gender;
    private Date joinDate;
    private String role;
    private String location;
    private Long accountId;

    public UserDetailDTO(String staffCode, String fullName, String userName, Date joinDate, String role,String location) {
        this.staffCode = staffCode;
        this.fullName = fullName;
        this.userName = userName;
        this.joinDate = joinDate;
        this.role = role;
        this.location = location;
    }

    public UserDetailDTO(String staffCode, String fullName, String userName, Date dob, Gender gender, Date joinDate, String role, String location, Long accountId) {
        this.staffCode = staffCode;
        this.fullName = fullName;
        this.userName = userName;
        this.dob = dob;
        this.gender = gender;
        this.joinDate = joinDate;
        this.role = role;
        this.location = location;
        this.accountId = accountId;
    }

    public UserDetailDTO(String staffCode, String fullName, String userName, Date dob, Gender gender, Date joinDate, String role, String location) {
        this.staffCode = staffCode;
        this.fullName = fullName;
        this.userName = userName;
        this.dob = dob;
        this.gender = gender;
        this.joinDate = joinDate;
        this.role = role;
        this.location = location;
    }
}
