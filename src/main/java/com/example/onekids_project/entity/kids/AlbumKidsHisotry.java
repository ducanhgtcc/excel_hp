package com.example.onekids_project.entity.kids;

import com.example.onekids_project.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Year;

@Getter
@Setter
@Entity
@Table(name = "album_kids_hisotry")
public class AlbumKidsHisotry extends BaseEntity<String> {

    @Column(nullable = false, length = 5000)
    private String listIdAlbum;

    private int year;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_kids", nullable = false)
    private Kids kid;

}
