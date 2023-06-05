package com.example.onekids_project.entity.classes;

import com.example.onekids_project.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "ma_url_menu_file")
public class UrlMenuFile extends BaseEntity<String> {
    private String urlPicture;
    private String urlLocalPicture;
    private String namePicture;
    private String urlFile;
    private String urlLocalFile;
    private String nameFile;

    @JsonBackReference
    @ManyToMany(mappedBy = "urlMenuFileList", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Set<ManuFile> manuFileList;
}
