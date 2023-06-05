package com.example.onekids_project.entity.user;

import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.base.BaseEntityNoAuditing;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "ma_api")
public class Api extends BaseEntity<String> {

    @Column(nullable = false)
    private String apiUrl;

    @Column(nullable = false)
    private String apiName;

    @Column(length = 3000)
    private String apiDescription;

    //AppTypeConstant
    @Column(nullable = false)
    private String type;

    private int level1;

    private int level2;

    private int level3;

    //dùng để sắp xếp vị trí
    private int ranks;

    @JsonBackReference
    @ManyToMany(mappedBy = "apiList", fetch = FetchType.LAZY)
    private List<Role> roleList;
}
