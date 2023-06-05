package com.example.onekids_project.entity.parent;

import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.kids.KidsTemp;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "ma_parent_temp")
public class ParentTemp extends BaseEntity<String> {

    @Column(length = 45, nullable = false, unique = true)
    private String parentCode;

    @Column(nullable = false)
    private String accountType;

    private String fatherAvatar;

    private String representation;

    private String fatherName;

    private LocalDate fatherBirthday;

    @Column(length = 15)
    private String fatherPhone;

    private String fatherEmail;

    @Column(length = 10)
    private String fatherGender;

    private String fatherJob;

    private String motherAvatar;

    private String motherName;

    private LocalDate motherBirthday;

    @Column(length = 15)
    private String motherPhone;

    private String motherEmail;

    @Column(length = 10)
    private String motherGender;

    private String motherJob;

    private String appType;

    @JsonManagedReference
    @OneToMany(mappedBy = "parentTemp", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<KidsTemp> kidsTempList;

}
