package com.example.onekids_project.entity.sample;

import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.base.BaseEntityId;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(name = "wishes_sample")
public class WishesSample extends BaseEntity<String> {

    @Column(nullable = false, length = 500)
    private String wishesContent;

    //học sinh, phụ huynh, nhân viên: nằm trong class SampleContant
    @Column(nullable = false)
    private String receiverType;

    @Column(length = 500)
    private String urlPicture;

    @Column(length = 500)
    private String urlPictureLocal;

    @NotNull
    private Long idSchool;

}
