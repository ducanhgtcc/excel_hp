package com.example.onekids_project.entity.employee;

import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.user.MaUser;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "ma_employee")
public class Employee extends BaseEntity<String> {

    @Column(nullable = false, length = 45)
    private String code;

    @Column(nullable = false, columnDefinition = "int default 0")
    private int schoolCount;

    @Column(length = 500)
    private String avatar;

    @Column(length = 500)
    private String avatarLocal;

    private String email;

    @Column(nullable = false)
    private LocalDate birthday;

    //khi appType là teacher thì khi chuyển lớp cập nhật idKidLogin theo
    @Column(nullable = false)
    private Long idSchoolLogin;

    //khi appType là plus thì ko quan tam đến idSchoolLoin
    //khi chưa được add vào lớp nào thì idClassLogin=0
    @Column(nullable = false)
    private Long idClassLogin = 0l;

//    @JsonBackReference
//    @OneToOne(mappedBy = "employee", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
//    private MaUser maUser;

    @JsonManagedReference
    @OneToOne
    @JoinColumn(name = "id_ma_user", nullable = false, unique = true)
    private MaUser maUser;

    @JsonManagedReference
    @OneToMany(mappedBy = "employee", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<InfoEmployeeSchool> infoEmployeeSchoolList;
}
