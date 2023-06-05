package com.example.onekids_project.entity.usermaster;

import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.user.MaUser;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "supper_admin")
public class SupperAdmin extends BaseEntity<String> {
    @Column(nullable = false)
    private String fullName;

    private String gender;

    private String phone;

    private String email;

    private LocalDate birthDay;

    @Column(length = 500)
    private String note;

    @OneToOne
    @JoinColumn(name = "id_ma_user", nullable = false, unique = true)
    private MaUser maUser;

}
