package com.example.onekids_project.entity.school;

import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.kids.Kids;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "kids_school_date")
public class KidsSchoolDate extends BaseEntity<String> {

    private LocalDate fromDate;

    private LocalDate toDate;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_kid", nullable = false)
    private Kids kids;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_school", nullable = false)
    private School school;
}
