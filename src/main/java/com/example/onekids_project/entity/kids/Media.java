package com.example.onekids_project.entity.kids;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.classes.MaClass;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "ma_media")
public class Media extends BaseEntity<String> {

    @Column(nullable = false)
    private Long idSchool;

    @Column(nullable = false)
    private String mediaName;

    @Column(nullable = false)
    private String linkMedia;

    //Video, Facebook, Khác
    @Column(nullable = false)
    private String mediaType;

    private String note;

    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean mediaActive = AppConstant.APP_TRUE;

    //Trường, Lớp
    @Column(nullable = false)
    private String scopeType;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    }, fetch = FetchType.LAZY)
    @JoinTable(name = "ex_class_media",
            joinColumns = @JoinColumn(name = "id_media"),
            inverseJoinColumns = @JoinColumn(name = "id_class")
    )
    @JsonBackReference
    private List<MaClass> maClassList;

}
