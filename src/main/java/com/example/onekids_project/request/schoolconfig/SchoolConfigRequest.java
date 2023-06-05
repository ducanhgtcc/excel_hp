package com.example.onekids_project.request.schoolconfig;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalTime;

@Getter
@Setter
@ToString
public class SchoolConfigRequest extends IdRequest {
    //1
    private boolean album;

    //2
    private boolean evaluate;

    //3
    private boolean isEditAproved;

    //4
    private boolean evaluateDate;

    //5
    private boolean evaluateWeek;

    //6
    private boolean evaluateMonth;

    //7
    private boolean evaluatePeriod;

    //8
    private boolean approvedAttendanceArrive;

    //9
    private LocalTime timeAttendanceArrive;

    //10
    private boolean approvedAttendanceLeave;

    //11
    private LocalTime timeAttendanceLeave;

    //12
    private Integer againAttendance;

    //13
    private int dateAbsent;

    //14
    private LocalTime timeAbsent;

    //15
    private boolean morningSaturday;

    //16
    private boolean afternoonSaturday;

    //17
    private boolean eveningSaturday;

    //18
    private boolean sunday;

    //19
    private boolean morningAttendanceArrive;

    //20
    private boolean afternoonAttendanceArrive;

    //21
    private boolean eveningAttendanceArrive;

    //22
    private boolean morningEat;

    //23
    private boolean secondMorningEat;

    //24
    private boolean lunchEat;

    //25
    private boolean afternoonEat;

    //26
    private boolean secondAfternoonEat;

    //27
    private boolean eveningEat;

    //28
    private boolean appSendApproved;

    //29
    private boolean isApprovedSchedule;

    //30
    private boolean isApprovedMenu;

    //31
    private boolean parentPhone;

    //32
    private boolean parentInfo;

    //33
    private boolean employeePhone;

    //34
    private boolean employeeInfo;

    //35
    private boolean historyViewParent;

    //36
    private boolean historyViewTeacher;

    //37
    private boolean feesInfo;

    //38
    private boolean feesNumber;

    //39
    private LocalTime timeArriveKid;

    //40
    private LocalTime timePickupKid;

    //41
    private boolean showEvaluateSys;

    //42
    private boolean showAttentendanceSys;

    //43
    private boolean showWishSys;

    //44
    private LocalTime timeArriveEmployee;

    //45
    private LocalTime timeLeaveEmployee;

    //46
    private boolean receiptNote;

    //47
    private boolean paymentNote;

    //48
    private boolean autoBillSalaryEmployee;

    //49
    private boolean autoSignSalaryEmployee;

    //50
    @NotNull
    private Integer autoNextmonthSalaryDate;

    //51
    private boolean autoApprovedSalary;

    //52
    private boolean autoLockSalarySuccess;

    //53
    private boolean autoShowBillOfMonth;

    //54
    private boolean autoFeesApprovedKids;

    //55
    private boolean autoGenerateFeesKids;

    //56
    @NotNull
    private Integer autoNexMonthFeesDate;

    //57
    private boolean autoApprovedFeesMonthKids;

    //58
    private boolean autoLockFeesSuccessKids;

    //59
    private boolean autoShowFeesMonth;

    //60
    private boolean autoShowFeesFutureKids;

    //61
    private boolean showWalletParent;

    //62
    private boolean autoShowFeesFutureKidsForTeacher;

    //63
    private boolean deleteStatus;

    //64
    @Size(min = 6, max = 6)
    private String verifyCode;

    //65
    private boolean checkCompleteFeesStatus;

    //66
    private boolean notifyWalletOutStatus;

    //67
    private boolean showOnecamPlus;

    //68
    private Integer albumMaxNumber;

    //69
    private boolean approvedLogoutStatus;

    //70
    private boolean breakfastAuto;

    //71
    private boolean secondBreakfastAuto;

    //72
    private boolean lunchAuto;

    //73
    private boolean afternoonAuto;

    //74
    private boolean secondAfternoonAuto;

    //75
    private boolean dinnerAuto;

}
