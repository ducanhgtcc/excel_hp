package com.example.onekids_project.entity.school;

import com.example.onekids_project.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * date 2021-10-21 8:32 AM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
@Entity
@Table(name = "ma_notify_school")
public class NotifySchool extends BaseEntity<String> {

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String link;

    private boolean active = true;

    private Long idSchool;

    @JsonManagedReference
    @OneToMany(mappedBy = "notifySchool", fetch = FetchType.LAZY)
    private List<NotifySchoolAttachFile> notifySchoolAttachFileList;
}
