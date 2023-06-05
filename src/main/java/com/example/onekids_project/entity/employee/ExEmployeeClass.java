package com.example.onekids_project.entity.employee;

import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.classes.MaClass;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "ex_employee_class")
public class ExEmployeeClass extends BaseEntity<String> {

    private boolean isMaster;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_info_employee", nullable = false)
    private InfoEmployeeSchool infoEmployeeSchool;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_class", nullable = false)
    private MaClass maClass;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    }, fetch = FetchType.LAZY)
    @JoinTable(name = "ex_employee_subject",
            joinColumns = @JoinColumn(name = "id_exemployee_class"),
            inverseJoinColumns = @JoinColumn(name = "id_subject")
    )
    @JsonManagedReference
    private Set<Subject> subjectSet;

}
