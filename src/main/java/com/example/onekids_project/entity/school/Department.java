package com.example.onekids_project.entity.school;

import com.example.onekids_project.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "ma_department")
public class Department extends BaseEntity<String> {

    @Column(nullable = false)
    private String departmentName;

    private String departmentDescription;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_school", nullable = false)
    private School school;

    @JsonManagedReference
    @OneToMany(mappedBy = "department", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<ExDepartmentEmployee> departmentEmployeeList;
}
