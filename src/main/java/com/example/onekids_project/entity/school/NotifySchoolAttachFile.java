package com.example.onekids_project.entity.school;

import com.example.onekids_project.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * date 2021-10-21 8:54 AM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
@Entity
@Table(name = "notify_school_attach_file")
public class NotifySchoolAttachFile extends BaseEntity<String> {

    @Column(nullable = false, length = 500)
    private String url;

    @Column(nullable = false, length = 500)
    private String urlLocal;

    private String name;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_notify_school", nullable = false)
    private NotifySchool notifySchool;
}
