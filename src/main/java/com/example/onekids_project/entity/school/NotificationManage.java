package com.example.onekids_project.entity.school;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * date 2021-07-28 1:55 PM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
@Entity
@Table(name = "notification_manage")
public class NotificationManage extends BaseEntity<String> {

    private String title;

    @Column(length = 1000)
    private String content;
    //Loại thông báo: NotificationConstant
    private String type;
    //plus-teacher-parent
    private String typeReceive;

    private String note;

    private boolean status = AppConstant.APP_FALSE;
    //-1, 0, 1-> tháng trước - tháng này - tháng sau
    private int month;

    @Column(nullable = false)
    private Long idSchool;
    //parent-1, teacher-2, plus-3
    private int sortNumber;

    @JsonManagedReference
    @OneToMany(mappedBy = "notificationManage", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<NotificationManageDate> notificationManageDateList;
}
