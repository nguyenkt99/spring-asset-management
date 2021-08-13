package com.nashtech.rookies.AssetManagement.model.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nashtech.rookies.AssetManagement.constant.Gender;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.boot.context.properties.bind.DefaultValue;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(schema = "public", name = "userdetails")
@Getter
@Setter
public class UserDetail {

    @Id
    @GeneratedValue(generator = "user-generator")
    @GenericGenerator(name = "user-generator",
            parameters = {@org.hibernate.annotations.Parameter(name = "prefix", value = "SD"),
                    @org.hibernate.annotations.Parameter(name = "length", value = "4")},
            strategy = "com.nashtech.rookies.AssetManagement.utils.MyGenerator")
    private String id;
    @Column(nullable = true)
    private String firstName;
    @Column(nullable = true)
    private String lastName;
    private Gender gender;
    private Date dateOfBirth;
    private Date joinDate;
    private String location;
    @Column(columnDefinition = "boolean default false")
    private Boolean isDisable;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;

    public UserDetail(String id, String firstName, String lastName, Gender gender, Date dateOfBirth, Date joinDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.joinDate = joinDate;
    }


    public UserDetail() {
    }

    @Override
    public String toString() {
        return "UserDetail{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender=" + gender +
                ", dateOfBirth=" + dateOfBirth +
                ", joinDate=" + joinDate +
                '}';
    }
}
