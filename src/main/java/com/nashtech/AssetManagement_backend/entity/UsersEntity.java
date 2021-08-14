package com.nashtech.AssetManagement_backend.entity;

import com.nashtech.AssetManagement_backend.generators.AssetCodeGenerator;
import com.nashtech.AssetManagement_backend.generators.PasswordGenerator;
import com.nashtech.AssetManagement_backend.generators.StaffCodeGenerator;
import com.nashtech.AssetManagement_backend.generators.UsernameGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UsersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @GenericGenerator(
            name = "user_seq",
            strategy = "com.nashtech.AssetManagement_backend.generators.StaffCodeGenerator",
            parameters = {
                    @Parameter(name = StaffCodeGenerator.INCREMENT_PARAM, value = "50"),
                    @Parameter(name = StaffCodeGenerator.VALUE_PREFIX_PARAMETER, value = "SD"),
                    @Parameter(name = StaffCodeGenerator.NUMBER_FORMAT_PARAMETER, value = "%04d") })
    private String staffCode;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(length = 10,name = "gender")
    private Gender gender;

    @GeneratorType(type = UsernameGenerator.class, when = GenerationTime.INSERT)
    @Column(name = "user_name")
    private String userName;


    //    @NotBlank
    @Size(min=6, max = 100)
    @GeneratorType(type = PasswordGenerator.class, when = GenerationTime.INSERT)
    private String password;

    @Column(name = "date_of_birth")
//    @CreationTimestamp
    private Date dateOfBirth;

    @Column(name = "joined_date")
//    @CreationTimestamp
    private Date joinedDate;

    @Column(name = "created_date")
    @CreationTimestamp
    private Date createdDate;

    @Enumerated(EnumType.STRING)
    @Column(length = 60,name = "location")
    private Location location;


    @Column(name = "is_first_login")
    private boolean isFirstLogin;

    @ManyToOne
    @JoinColumn(name="role_id")
    private RolesEntity role;

    @OneToMany(mappedBy = "assignTo")
    private List<AssignmentEntity> assignmentTos = new ArrayList<>();

    @OneToMany(mappedBy = "assignBy")
    private List<AssignmentEntity> assignmentsBys = new ArrayList<>();

    @OneToMany(mappedBy = "requestBy")
    private List<RequestEntity> requestBys = new ArrayList<>();

    @OneToMany(mappedBy = "acceptBy")
    private List<RequestEntity> acceptBys = new ArrayList<>();

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 60)
    private UserState state;

    @PrePersist
    protected void onCreate() {
      this.state = UserState.Enable;
    }
}
