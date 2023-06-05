package com.example.onekids_project.response.schoolconfig;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfigPlusResponse {
    //    Có cần duyệt album ảnh hay không?
    //true không cần duyệt, false cần duyệt
    private boolean album;

    //    Có cần duyệt đánh giá nhận xét hay không?
    //true không cần duyệt, false cần duyệt
    private boolean evaluate;

    //    Có cho phép sửa đánh giá khi đã duyệt hay không?
    // true là cho phép sửa
    private boolean editAproved;

    //    Có hiển thị chức năng đánh giá ngày hay không?
    private boolean evaluateDate;

    //    Có hiển thị chức năng đánh giá tuần hay không?
    private boolean evaluateWeek;

    //    Có hiển thị chức năng đánh giá tháng hay không?
    private boolean evaluateMonth;

    //    Có hiển thị chức năng đánh giá định kỳ hay không?
    private boolean evaluatePeriod;

    //    Có cần duyệt các thông báo gửi đi hay không?
    private boolean appSendApproved;

    //    Có cần duyệt nội dung thời khóa biểu hay không?
    private boolean approvedSchedule;

    //    Có cần duyệt nội dung thực đơn hay không?
    private boolean approvedMenu;

    //    Cho phép hiển thị số điện thoại của phụ huynh với App plus không?
    private boolean parentPhone;

    //Cho phép plus xem thông tin chi tiết của phụ huynh hay không?
    private boolean parentInfo;

    //    Cho phép plus xem thông tin chi tiết của giáo viên không?
    private boolean teacherInfo;
}
