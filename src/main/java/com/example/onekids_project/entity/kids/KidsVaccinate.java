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
@Table(name = "ex_kids_vaccinate")
public class KidsVaccinate extends BaseEntity<String> {

    private LocalDate date;

    @Column(columnDefinition = "bit default 0")
    private boolean done;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_kids", nullable = false)
    private Kids kid;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vaccinate", nullable = false)
    private Vaccinate vaccinate;
}
