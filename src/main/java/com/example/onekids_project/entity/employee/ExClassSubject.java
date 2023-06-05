package com.example.onekids_project.entity.employee;

import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.base.BaseEntityId;
import com.example.onekids_project.entity.classes.MaClass;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/*@Getter
@Setter
@Entity
@Table(name = "ex_class_subject")*/
public class ExClassSubject extends BaseEntity<String> {

    /*@JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_subject")
    private Subject subject;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_class")
    private MaClass maClass;*/
}
