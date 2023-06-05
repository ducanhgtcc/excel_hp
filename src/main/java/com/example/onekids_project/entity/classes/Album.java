package com.example.onekids_project.entity.classes;

import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.kids.ExAlbumKids;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "ma_album")
public class Album extends BaseEntity<String> {

    @Column(nullable = false)
    private String albumName;

    @Column(length = 8000)
    private String albumDescription;

    @Column(nullable = false)
    private String albumType;

    private String urlPictureFirst;

    @Column(nullable = false)
    private Long idSchool;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_class")
    private MaClass maClass;

    // 1-n album-listAlbum
    @JsonManagedReference
    @OneToMany(mappedBy = "album", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<ListPicture> alistPictureList;

    // tuan to do
    @JsonManagedReference
    @OneToMany(mappedBy = "album", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<ExAlbumKids> exAlbumKidsListA;


}
