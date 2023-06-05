package com.example.onekids_project.entity.user;

import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.kids.Kids;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "url_file_app_send")
public class UrlFileAppSend extends BaseEntity<String> {

    @Column(length = 1500)
    private String attachFile;

    @Column(length = 1500)
    private String urlLocalFile;

    private String name;

    @Column(length = 1500)
    private String attachPicture;

    @Column(length = 1500)
    private String urlLocalPicture;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name = "id_appsend",nullable = false)
    private AppSend appSend;
}
