package com.example.onekids_project.entity.kids;

import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.base.BaseEntityId;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "ma_vaccinate")
public class Vaccinate extends BaseEntity<String> {

    @Column(nullable = false)
    private String yearsOld;

    @Column(nullable = false)
    private String vaccinate;

    @Column(length = 10, nullable = false)
    private String injectNumber;

    @JsonManagedReference
    @OneToMany(mappedBy = "vaccinate", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<KidsVaccinate> kidsVaccinateList;
}
