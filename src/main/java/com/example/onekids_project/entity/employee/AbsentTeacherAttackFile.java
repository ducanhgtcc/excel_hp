package com.example.onekids_project.entity.employee;

import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.kids.AbsentLetter;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "absent_teacher_attack_file")
public class AbsentTeacherAttackFile extends BaseEntity<String> {
    @Column(nullable = false, length = 500)
    private String url;

    @Column(nullable = false, length = 500)
    private String urlLocal;

    private String name;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_absent_teacher", nullable = false)
    private AbsentTeacher absentTeacher;
}
