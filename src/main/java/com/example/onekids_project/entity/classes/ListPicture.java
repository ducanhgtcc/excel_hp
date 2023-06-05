package com.example.onekids_project.entity.classes;

import com.example.onekids_project.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "list_picture")
public class ListPicture extends BaseEntity<String> {

    @Column(nullable = false)
    private String urlPicture;

    @Column(nullable = false)
    private String urlLocal;

    @Column(nullable = false)
    private Boolean isApproved;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_album", nullable = false)
    private Album album;
}
