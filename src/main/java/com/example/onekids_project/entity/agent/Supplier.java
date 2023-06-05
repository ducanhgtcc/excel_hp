package com.example.onekids_project.entity.agent;

import com.example.onekids_project.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "ma_supplier")
public class Supplier extends BaseEntity<String> {

    @Column(nullable = false)
    private String supplierName;

    @Column(nullable = false)
    private String supplierLink;

    @Column(nullable = false)
    private String usernameLink;

    @Column(nullable = false)
    private String passwordLink;

    @Column(length = 500)
    private String supplierNote;

    @JsonBackReference
    @OneToMany(mappedBy = "supplier", fetch = FetchType.LAZY)
    private List<Brand> brandList;


}
