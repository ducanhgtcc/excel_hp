package com.example.onekids_project.entity.agent;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.school.School;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "ma_brand")
public class Brand extends BaseEntity<String> {

    @Column(nullable = false)
    private String brandName;

    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean brandActive = AppConstant.APP_TRUE;

    private boolean brandTypeCskh;

    private boolean brandTypeAds;

    @Column(length = 500)
    private String note;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_supplier", nullable = false)
    private Supplier supplier;

    @JsonBackReference
    @ManyToMany(mappedBy = "brandList", fetch = FetchType.LAZY)
    private List<Agent> agentList;

    @JsonManagedReference
    @OneToMany(mappedBy = "brand", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<School> schoolList;
}
