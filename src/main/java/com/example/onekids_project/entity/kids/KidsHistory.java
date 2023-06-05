package com.example.onekids_project.entity.kids;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "ma_kids_history")
public class KidsHistory extends BaseEntity<String> {

    @Column(length = 1000)
    private String address;

    private LocalDate dateStart;

    @Column(length = 45)
    private String kidStatus;

    private LocalDate dateRetian;

    private LocalDate dateLeave;

    @Column(length = 1000)
    private String note;

    @Column(columnDefinition = "bit default true", nullable = false)
    private boolean isActivated = AppConstant.APP_TRUE;

    @Column(nullable = false)
    private Boolean historyView;

    private String listIdClass;

    private String listIdAlbum;

    @Column(nullable = false)
    private Long idSchool;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_kids", nullable = false)
    private Kids kid;
}
