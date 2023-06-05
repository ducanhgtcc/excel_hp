package com.example.onekids_project.entity.kids;

import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.parent.ParentTemp;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "ma_kids_temp")
public class KidsTemp extends BaseEntity<String> {

    @Column(length = 45, nullable = false)
    private String kidCode;

    private String avatarKid;

    @Column(length = 45)
    private String firstName;

    @Column(length = 45)
    private String lastName;

    @Column(length = 45, nullable = false)
    private String fullName;

    private LocalDate birthday;

    @Column(length = 45)
    private String nickName;

    @Column(length = 45)
    private String gender;

    @Column(length = 1000, nullable = false)
    private String address;

    private String note;

    @Column(length = 45, nullable = false)
    private String kidStatus;

    private LocalDate dateRetain;

    private LocalDate dateLeave;

    private boolean isActivated;

    @Column(nullable = false)
    private Long idClass;

    private String listIdClass;

    @Column(nullable = false)
    private Long idSchool;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_parent", nullable = false)
    private ParentTemp parentTemp;

}
