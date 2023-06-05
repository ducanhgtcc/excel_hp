package com.example.onekids_project.entity.school;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "config_plus")
public class ConfigPlus extends BaseEntity<String> {
    //    Có cần duyệt album ảnh hay không?
    //true không cần duyệt, false cần duyệt
    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean album = AppConstant.APP_TRUE;

    //    Có cần duyệt đánh giá nhận xét hay không?
    //true không cần duyệt, false cần duyệt
    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean evaluate = AppConstant.APP_TRUE;

    //    Có cho phép sửa đánh giá khi đã duyệt hay không?
    // true là cho phép sửa
    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean editAproved = AppConstant.APP_TRUE;

    //    Có hiển thị chức năng đánh giá ngày hay không?
    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean evaluateDate = AppConstant.APP_TRUE;

    //    Có hiển thị chức năng đánh giá tuần hay không?
    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean evaluateWeek = AppConstant.APP_TRUE;

    //    Có hiển thị chức năng đánh giá tháng hay không?
    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean evaluateMonth = AppConstant.APP_TRUE;

    //    Có hiển thị chức năng đánh giá định kỳ hay không?
    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean evaluatePeriod = AppConstant.APP_TRUE;

    //    Có cần duyệt các thông báo gửi đi hay không?
    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean appSendApproved = AppConstant.APP_TRUE;

    //    Có cần duyệt nội dung thời khóa biểu hay không?
    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean approvedSchedule = AppConstant.APP_TRUE;

    //    Có cần duyệt nội dung thực đơn hay không?
    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean approvedMenu = AppConstant.APP_TRUE;

    //    Cho phép hiển thị số điện thoại của phụ huynh với App plus không?
    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean parentPhone = AppConstant.APP_TRUE;

    //Cho phép plus xem thông tin chi tiết của phụ huynh hay không?
    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean parentInfo = AppConstant.APP_TRUE;

    //    Cho phép plus xem thông tin chi tiết của giáo viên không?
    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean teacherInfo = AppConstant.APP_TRUE;

    @JsonBackReference
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_school", nullable = false, unique = true)
    private School school;
}
