package com.nashtech.rookies.AssetManagement.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateUserRequest {

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String gender;
    private String joinDate;
    private String dayOfBirth;
    @NotBlank
    private String role;
    @NotBlank
    private String accountId;
}
