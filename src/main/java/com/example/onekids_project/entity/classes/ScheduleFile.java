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
@Table(name = "ma_schedule_file")
public class ScheduleFile extends BaseEntity<String> {

    @Column(nullable = false)
    private Long idSchool;

    @Column(nullable = false)
    private LocalDate fromFileTsime;

    @Column(nullable = false)
    private LocalDate toFileTsime;

//    private String urlPicture;
//    private String urlLocalPicture;
//    private String urlFile;
//    private String urlLocalFile;

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

    @JoinTable(name = "ex_schedule_file_url",
            joinColumns = @JoinColumn(name = "id_schedule_file"),
            inverseJoinColumns = @JoinColumn(name = "id_url_schedule_file")
    )
    @JsonManagedReference
    private Set<UrlScheuldeFile> urlScheuldeFileList;
}
