package com.example.onekids_project.entity.kids;

import com.example.onekids_project.entity.base.BaseEntityId;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "ma_kids_log")
public class KidsLog extends BaseEntityId<String> {

    @Column(nullable = false)
    private Long idEmployee;

    @Column(nullable = false)
    private String action;

    private String area;

    private String oldValue;

    private String newValue;

    @Column(nullable = false)
    private LocalDateTime dateAction;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_kids", nullable = false)
    private Kids kid;

}
