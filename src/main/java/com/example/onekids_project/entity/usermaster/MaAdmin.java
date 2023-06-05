package com.example.onekids_project.entity.usermaster;

import com.example.onekids_project.common.EmployeeConstant;
import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.entity.user.MaUser;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "ma_admin")
public class MaAdmin extends BaseEntity<String> {

    @Column(nullable = false, unique = true, length = 45)
    private String code;

    //in class AdminTypeConstant
    @Column(columnDefinition = "varchar(255) default 'Nhân viên'", nullable = false)
    private String accountType = "Nhân viên";

    @Column(length = 45)
    private String firstName;

    @Column(length = 45)
    private String lastName;

    @Column(nullable = false, length = 60)
    private String fullName;

    private String avatar;

    private LocalDate birthday;

    @Column(length = 1000, nullable = false)
    private String address;

    @Column(length = 45, nullable = false)
    private String gender;

    private String email;

    @Column(length = 15, nullable = false)
    private String phone;

    @Column(length = 20)
    private String cmnd;

    private LocalDate cmndDate;

    @Column(length = 1000)
    private String permanentAddress;

    @Column(length = 1000)
    private String currentAddress;

    private String marriedStatus;

    private Integer numberChildren;

    private String educationLevel;

    private String professional;

    @Column(nullable = false)
    private LocalDate startDate;

    private LocalDate contractDate;

    private LocalDateTime endDate;

    private String note;

    @Column(columnDefinition = "varchar(255) default 'Đang làm'", nullable = false)
    private String adminStatus= EmployeeConstant.STATUS_WORKING;

    private LocalDate dateRetain;

    private LocalDate dateLeave;

    private Long idDepartment;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    }, fetch = FetchType.LAZY)

    @JoinTable(name = "ex_admin_school",
            joinColumns = @JoinColumn(name = "id_admin"),
            inverseJoinColumns = @JoinColumn(name = "id_school")
    )
    @JsonManagedReference
    private List<School> schoolList;

    @OneToOne
    @JoinColumn(name = "id_ma_user", nullable = false, unique = true)
    private MaUser maUser;
}
