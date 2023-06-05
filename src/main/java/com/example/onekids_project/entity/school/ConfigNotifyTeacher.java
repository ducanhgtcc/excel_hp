package com.example.onekids_project.entity.school;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * date 2021-04-10 15:49
 *
 * @author lavanviet
 */
@Getter
@Setter
@Entity
@Table(name = "config_notify_teacher")
public class ConfigNotifyTeacher extends BaseEntity<String> {
    //thông báo
    @Column(columnDefinition = "bit default 1")
    private boolean notify = AppConstant.APP_TRUE;

    //lời nhắn
    @Column(columnDefinition = "bit default 1")
    private boolean message = AppConstant.APP_TRUE;

    //dặn thuốc
    @Column(columnDefinition = "bit default 1")
    private boolean medicine = AppConstant.APP_TRUE;

    //xin nghỉ
    @Column(columnDefinition = "bit default 1")
    private boolean absent = AppConstant.APP_TRUE;

    //nhận xét
    @Column(columnDefinition = "bit default 1")
    private boolean evaluate = AppConstant.APP_TRUE;

    //góp ý
    @Column(columnDefinition = "bit default 1")
    private boolean feedback = AppConstant.APP_TRUE;

    //sinh nhật
    @Column(columnDefinition = "bit default 1")
    private boolean birthday = AppConstant.APP_TRUE;

    //danh bạ
    @Column(columnDefinition = "bit default 1")
    private boolean phonebook = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean news = AppConstant.APP_TRUE;

    //thanh toán hóa đơn
    @Column(columnDefinition = "bit default 1")
    private boolean orderPayment = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean orderNotify = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean orderShow = AppConstant.APP_TRUE;

    @JsonBackReference
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_school", nullable = false, unique = true)
    private School school;

}
