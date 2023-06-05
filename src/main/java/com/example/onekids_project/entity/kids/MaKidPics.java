package com.example.onekids_project.entity.kids;

import com.example.onekids_project.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name="ma_kid_pics")
public class MaKidPics extends BaseEntity<String> {

    @Column(length = 500)
    private String urlWeb;

    @Column(length = 500)
    private String urlLocal;

    private boolean mainStatus;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_kid", nullable = false)
    private Kids kid;

}
