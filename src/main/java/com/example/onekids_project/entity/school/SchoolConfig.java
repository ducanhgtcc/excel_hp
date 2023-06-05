package com.example.onekids_project.entity.school;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "school_config")
public class SchoolConfig extends BaseEntity<String> {

    //true không cần duyệt, false cần duyệt
    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean album;

    //true không cần duyệt, false cần duyệt
    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean evaluate = AppConstant.APP_TRUE;

    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean parentPhone = AppConstant.APP_TRUE;

    //Cho phép giáo viên xem thông tin chi tiết của phụ huynh hay không?
    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean parentInfo = AppConstant.APP_TRUE;

    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean employeePhone = AppConstant.APP_TRUE;

    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean employeeInfo = AppConstant.APP_TRUE;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean historyViewParent;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean historyViewTeacher;

    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean approvedAttendanceArrive = AppConstant.APP_TRUE;

    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean approvedAttendanceLeave = AppConstant.APP_TRUE;

    @Column(nullable = false, columnDefinition = "time default '10:00:00'")
    private LocalTime timeAttendanceArrive = LocalTime.of(10, 0, 0);

    @Column(nullable = false, columnDefinition = "time default '19:00:00'")
    private LocalTime timeAttendanceLeave = LocalTime.of(19, 0, 0);

    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean morningSaturday = AppConstant.APP_TRUE;

    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean afternoonSaturday = AppConstant.APP_TRUE;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean eveningSaturday = AppConstant.APP_FALSE;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean sunday = AppConstant.APP_FALSE;

    //true là học sáng(thứ 2- thứ 6, chủ nhật học thì ăn theo)
    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean morningAttendanceArrive = AppConstant.APP_TRUE;

    //true là học chiều(thứ 2- thứ 6, chủ nhật học thì ăn theo)
    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean afternoonAttendanceArrive = AppConstant.APP_TRUE;

    //true là học tối(thứ 2- thứ 6, chủ nhật học thì ăn theo)
    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean eveningAttendanceArrive;

    //true là ăn sáng(tất cả các ngày)
    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean morningEat = AppConstant.APP_TRUE;

    //true là ăn phụ sáng
    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean secondMorningEat = AppConstant.APP_TRUE;

    //true là ăn trưa
    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean lunchEat = AppConstant.APP_TRUE;

    //true là ăn chiều
    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean afternoonEat = AppConstant.APP_TRUE;

    //true là ăn phụ chiều
    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean secondAfternoonEat = AppConstant.APP_TRUE;

    //true là ăn tối
    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean eveningEat;

    //ngày xin nghỉ phải trước bao nhiêu ngày
    @Column(nullable = false)
    private int dateAbsent;

    //thời gian xin nghỉ phải trước mấy giờ theo ngày xin nghỉ
    @Column(nullable = false, columnDefinition = "time default '08:00:00'")
    private LocalTime timeAbsent = LocalTime.of(8, 0, 0);

    private Integer againAttendance;

    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean evaluateDate = AppConstant.APP_TRUE;

    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean evaluateWeek = AppConstant.APP_TRUE;

    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean evaluateMonth = AppConstant.APP_TRUE;

    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean evaluatePeriod = AppConstant.APP_TRUE;

    //Có cho phép sửa đánh giá khi đã duyệt hay không?, true là cho phép sửa
    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean isEditAproved = AppConstant.APP_TRUE;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean appSendApproved;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean feesInfo;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean feesNumber;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean isApprovedSchedule = AppConstant.APP_FALSE;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean isApprovedMenu = AppConstant.APP_FALSE;

    //quy định giờ đến của học sinh
    @Column(nullable = false, columnDefinition = "time default '07:30:00'")
    private LocalTime timeArriveKid = LocalTime.of(7, 30, 0);

    //quy định giờ về của học sinh
    @Column(nullable = false, columnDefinition = "time default '17:00:00'")
    private LocalTime timePickupKid = LocalTime.of(17, 00, 0);

    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean showEvaluateSys = AppConstant.APP_TRUE;

    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean showAttentendanceSys = AppConstant.APP_TRUE;

    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean showWishSys = AppConstant.APP_TRUE;

    //bổ sung ngày 20-8auto_nex_month_fees_date
    //Thiết lập giờ đi làm buổi sáng của nhân viên nhà trường?
    @Column(nullable = false, columnDefinition = "time default '07:30:00'")
    private LocalTime timeArriveEmployee = LocalTime.of(7, 30, 0);

    //Thiết lập giờ đi làm buổi sáng của nhân viên nhà trường?
    @Column(nullable = false, columnDefinition = "time default '07:30:00'")
    private LocalTime timeMorningEmployee = LocalTime.of(7, 30, 0);

    //Thiết lập giờ đi làm buổi chiều của nhân viên nhà trường?
    @Column(nullable = false, columnDefinition = "time default '13:00:00'")
    private LocalTime timeAfternoonEmployee = LocalTime.of(13, 0, 0);

    //Thiết lập giờ đi làm buổi sáng của nhân viên nhà trường?
    @Column(nullable = false, columnDefinition = "time default '18:00:00'")
    private LocalTime timeEveningEmployee = LocalTime.of(18, 0, 0);

    //Thiết lập giờ về của nhân viên nhà trường?
    @Column(nullable = false, columnDefinition = "time default '12:00:00'")
    private LocalTime timeLeaveMorningEmployee = LocalTime.of(12, 0, 0);

    //Thiết lập giờ về của nhân viên nhà trường?
    @Column(nullable = false, columnDefinition = "time default '18:00:00'")
    private LocalTime timeLeaveAfternoonEmployee = LocalTime.of(18, 0, 0);

    //Thiết lập giờ về của nhân viên nhà trường?
    @Column(nullable = false, columnDefinition = "time default '22:00:00'")
    private LocalTime timeLeaveEveningEmployee = LocalTime.of(22, 0, 0);

    //Có tự động duyệt các phiếu thu không?
    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean receiptNote;

    //Có tự động duyệt các phiếu chi không?
    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean paymentNote;

    //Có sinh tự động các khoản công lương với nhân viên theo các khoản mặc định đã đăng ký?
    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean autoSignSalaryEmployee = AppConstant.APP_TRUE;

    //Có sinh tự động hóa đơn công lương của nhân viên cho tháng tiếp theo không (sinh tự động vào ngày đầu tháng)?
    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean autoBillSalaryEmployee = AppConstant.APP_TRUE;

    //Thiết lập ngày của tháng kế tiếp sẽ sinh tự động hóa đơn lương của tháng?
    @Column(nullable = false)
    private Integer autoNextmonthSalaryDate = 1;

    //Có tự động duyệt các khoản lương hàng tháng không?
    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean autoApprovedSalary;

    //Có tự động khóa các khoản lương khi đã thanh toán đủ không?
    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean autoLockSalarySuccess = AppConstant.APP_TRUE;

    //Có tự động hiển thị hóa đơn lương của cả tháng khi hóa đơn đã được sinh ra không?
    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean autoShowBillOfMonth;

    //Có sinh tự động các khoản thu với học sinh theo các khoản thu đã đăng ký?
    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean autoGenerateFeesKids = AppConstant.APP_TRUE;

    //Có sinh tự động hóa đơn học phí của học sinh cho tháng tiếp theo không(sinh tự động vào ngày đầu tháng)?
    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean autoFeesApprovedKids = AppConstant.APP_TRUE;

    //Thiết lập ngày của tháng kế tiếp sẽ sinh tự động hóa đơn học phí của tháng?
    @Column(nullable = false)
    private Integer autoNexMonthFeesDate = 1;

    //Có tự động duyệt các khoản thu của học sinh hàng tháng không?
    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean autoApprovedFeesMonthKids;

    //Có tự động khóa các khoản thu khi phụ huynh đã thanh toán đủ không?
    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean autoLockFeesSuccessKids = AppConstant.APP_TRUE;

    //Có tự động hiển thị hóa đơn học phí của cả tháng khi hóa đơn đã được sinh ra không?
    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean autoShowFeesMonth;

    //Có hiển thị thông tin các khoản thu dự kiến cho phụ huynh học sinh không?
    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean autoShowFeesFutureKids;

    //Có hiển thị thông tin khoản thu dự kiến của học sinh cho giáo viên không?
    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean autoShowFeesFutureKidsForTeacher;

    //Có cho phép phụ huynh truy cập ví hay không
    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean showWalletParent = AppConstant.APP_TRUE;

    //cho phép xóa học sinh, nhân sự hay không
    //true là cho phép xóa, false là ko cho phép xóa
    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean deleteStatus;

    @Column(nullable = false, length = 6)
    private String verifyCode;

    //true->ko check hoc phí khi ra trường, flase->check học phí
    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean checkCompleteFeesStatus;

    //true->thông báo khi nhà trường rút ví, flase->không thông báo
    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean notifyWalletOutStatus;

    //true->hiện onecam plus
    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean showOnecamPlus = AppConstant.APP_TRUE;

    //Số lượng ảnh tối đa khi đăng album
    @Column(columnDefinition = "int default 30")
    private Integer albumMaxNumber = 30;

    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean approvedLogoutStatus = AppConstant.APP_TRUE;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean breakfastAuto = AppConstant.APP_FALSE;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean secondBreakfastAuto = AppConstant.APP_FALSE;

    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean lunchAuto = AppConstant.APP_TRUE;

    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean afternoonAuto = AppConstant.APP_TRUE;

    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean secondAfternoonAuto = AppConstant.APP_TRUE;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean dinnerAuto = AppConstant.APP_FALSE;

    @JsonBackReference
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_school", nullable = false, unique = true)
    private School school;

}
