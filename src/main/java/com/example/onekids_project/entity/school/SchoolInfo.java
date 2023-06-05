package com.example.onekids_project.entity.school;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * date 2021-03-05 11:28
 *
 * @author lavanviet
 */
@Getter
@Setter
@Entity
@Table(name = "school_info")
public class SchoolInfo extends BaseEntity<String> {
    //Nội dung thanh toán hiển thị trên app phụ huynh
    @Column(columnDefinition = "TEXT")
    private String bankInfo;

    //Ghi chú trên phiếu thu học phí
    @Column(length = 1000)
    private String expired;

    //Ghi chú trên phiếu công lương
    @Column(columnDefinition = "TEXT")
    private String note;

    //đính kèm thông báo học phí qua app phụ huynh(khi click hiện thị ở hóa đơn)
    @Column(columnDefinition = "TEXT")
    private String feesParent;

    //hiện điểm danh ngày của app phụ huynh, true là hiện, false là ẩn
    @Column(columnDefinition = "bit default 1")
    private boolean showAttendanceDateParent = AppConstant.APP_TRUE;

    //hiện điểm danh tháng của app phụ huynh, true là hiện, false là ẩn
    @Column(columnDefinition = "bit default 1")
    private boolean showAttendanceMonthParent = AppConstant.APP_TRUE;

    //hiện điểm danh ăn của app phụ huynh, true là hiện, false là ẩn
    @Column(columnDefinition = "bit default 1")
    private boolean showAttendanceEatParent = AppConstant.APP_TRUE;

    //hiện điểm danh đến của app teacher-plus, true là hiện, false là ẩn
    @Column(columnDefinition = "bit default 1")
    private boolean showAttendanceArriveTeacherPlus = AppConstant.APP_TRUE;

    //hiện điểm danh về của app teacher-plus, true là hiện, false là ẩn
    @Column(columnDefinition = "bit default 1")
    private boolean showAttendanceLeaveTeacherPlus = AppConstant.APP_TRUE;

    //hiện điểm danh ăn của app teacher-plus, true là hiện, false là ẩn
    @Column(columnDefinition = "bit default 1")
    private boolean showAttendanceEatTeacherPlus = AppConstant.APP_TRUE;

    //nx học tập của app teacher-plus, true là hiện, false là ẩn
    @Column(columnDefinition = "bit default 1")
    private boolean showEvaluateLearnTeacherPlus = AppConstant.APP_TRUE;

    //nx ăn uống của app teacher-plus, true là hiện, false là ẩn
    @Column(columnDefinition = "bit default 1")
    private boolean showEvaluateEatTeacherPlus = AppConstant.APP_TRUE;

    //nx ngủ nghỉ của app teacher-plus, true là hiện, false là ẩn
    @Column(columnDefinition = "bit default 1")
    private boolean showEvaluateSleepTeacherPlus = AppConstant.APP_TRUE;

    //nx vệ sinh của app teacher-plus, true là hiện, false là ẩn
    @Column(columnDefinition = "bit default 1")
    private boolean showEvaluateSanitaryTeacherPlus = AppConstant.APP_TRUE;

    //nx sức khỏe của app teacher-plus, true là hiện, false là ẩn
    @Column(columnDefinition = "bit default 1")
    private boolean showEvaluateHealthTeacherPlus = AppConstant.APP_TRUE;

    //nx chung của app teacher-plus, true là hiện, false là ẩn
    @Column(columnDefinition = "bit default 1")
    private boolean showEvaluateCommonTeacherPlus = AppConstant.APP_TRUE;

    @JsonBackReference
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_school", nullable = false, unique = true)
    private School school;

}
