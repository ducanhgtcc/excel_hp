package com.example.onekids_project.entity.usermaster;

import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.entity.user.MaUser;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "ma_school_master")
public class SchoolMaster extends BaseEntity<String> {

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, length = 15)
    private String phone;

    @Column(length = 50)
    private String email;

    private LocalDate birthDay;

    private String gender;

    @Column(length = 500)
    private String note;

    @ManyToOne
    @JoinColumn(name = "id_school", nullable =false)
    private School school;

    @OneToOne
    @JoinColumn(name = "id_ma_user", nullable = false, unique = true)
    private MaUser maUser;

}
