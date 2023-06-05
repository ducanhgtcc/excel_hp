package com.example.onekids_project.entity.employee;

import com.example.onekids_project.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "ma_absent_teacher_date")
public class AbsentDateTeacher extends BaseEntity<String> {
    @Column(nullable = false)
    private LocalDate date;

    private boolean morning;

    private boolean afternoon;

    private boolean evening;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_absent_teacher", nullable = false)
    private AbsentTeacher absentTeacher;

}
