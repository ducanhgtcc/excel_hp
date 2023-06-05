package com.example.onekids_project.entity.classes;

import com.example.onekids_project.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * date 2021-05-05 10:03
 *
 * @author lavanviet
 */
@Getter
@Setter
@Entity
@Table(name = "day_off_class")
public class DayOffClass extends BaseEntity<String> {
    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String note;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_class", nullable = false)
    private MaClass maClass;
}
