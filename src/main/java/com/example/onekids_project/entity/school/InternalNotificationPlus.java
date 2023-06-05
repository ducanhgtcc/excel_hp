package com.example.onekids_project.entity.school;

import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * date 2021-07-28 3:31 PM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
@Entity
@Table(name = "internal_notification_plus")
public class InternalNotificationPlus extends BaseEntity<String> {

    private String title;

    @Column(length = 1000)
    private String content;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_info_employee", nullable = false)
    private InfoEmployeeSchool infoEmployeeSchool;
}
