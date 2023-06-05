package com.example.onekids_project.entity.classes;

import com.example.onekids_project.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "ma_menu_file")
public class ManuFile extends BaseEntity<String> {
    @Column(nullable = false)
    private Long idSchool;

    @Column(nullable = false)
    private LocalDate fromFileTime;

    @Column(nullable = false)
    private LocalDate toFileTime;

//    private String urlPicture;

//    private String urlFile;

//    @Column(nullable = false)
//    private Long idEmployee;

    @Column(nullable = false)
    private boolean isApproved;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_class", nullable = false)
    private MaClass maClass;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    }, fetch = FetchType.LAZY)

    @JoinTable(name = "ex_menu_file_url",
            joinColumns = @JoinColumn(name = "id_menu_file"),
            inverseJoinColumns = @JoinColumn(name = "id_url_meunu_file")
    )
    @JsonManagedReference
    private Set<UrlMenuFile> urlMenuFileList;
}
