package com.example.onekids_project.entity.employee;

import com.example.onekids_project.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "ma_account_type")
public class AccountType extends BaseEntity<String> {

    @Column(nullable = false)
    private String name;

    @Column(length = 5000)
    private String description;

    @JsonBackReference
    @ManyToMany(mappedBy = "accountTypeList", fetch = FetchType.LAZY)
    List<InfoEmployeeSchool> infoEmployeeSchoolList;

    @Column(nullable = false)
    private Long idSchool;
}
