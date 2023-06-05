package com.example.onekids_project.entity.kids;

import com.example.onekids_project.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "ma_absent_date")
public class AbsentDate extends BaseEntity<String> {

    @Column(nullable = false)
    private LocalDate absentDate;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean absentMorning;

//    private boolean morningStatus;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean absentAfternoon;

//    private boolean afternoonStatus;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean absentEvening;

//    private boolean eveningStatus;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_absent_letter", nullable = false)
    private AbsentLetter absentLetter;

}
