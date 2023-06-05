package com.example.onekids_project.entity.school;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * date 2021-08-09 9:56 AM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
@Entity
@Table(name = "notification_manage_date")
public class NotificationManageDate extends BaseEntity<String> {

    private Integer minute;

    private Integer hour;
    @Min(1)
    @Max(31)
    private Integer day;

    private Integer month;

    private boolean status = AppConstant.APP_TRUE;

    private Integer percent;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_notification_manage", nullable = false)
    private NotificationManage notificationManage;
}
