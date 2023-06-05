package com.example.onekids_project.entity.kids;

import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.classes.MaClass;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * chuyển lớp trước 12h thì ghi ngày chuyển cho lớp cũ, sau 12h thì ghi ngày chuyển cho lớp mới
 */
@Getter
@Setter
@Entity
@Table(name = "kids_class_date")
public class KidsClassDate extends BaseEntity<String> {

    @Column(nullable = false)
    private LocalDate fromDate;

    private LocalDate toDate;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_kid", nullable = false)
    private Kids kids;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_class", nullable = false)
    private MaClass maClass;

}
