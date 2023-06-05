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
@Table(name = "kids_status_timeline")
public class KidsStatusTimeline extends BaseEntity<String> {
    @Column(nullable = false)
    private LocalDate startDate;

    private LocalDate endDate;

    //trạng thái của học sinh: KidsStatusConstant
    @Column(nullable = false)
    private String status;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_kid", nullable = false)
    private Kids kids;

}
