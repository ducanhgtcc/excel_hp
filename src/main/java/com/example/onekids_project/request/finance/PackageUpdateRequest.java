package com.example.onekids_project.request.finance;

import com.example.onekids_project.common.FinanceConstant;
import com.example.onekids_project.request.base.IdRequest;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
public class PackageUpdateRequest extends IdRequest {
    @NotBlank
    private String name;

    @NotBlank
    @StringInList(values = {FinanceConstant.CATEGORY_IN, FinanceConstant.CATEGORY_OUT})
    private String category;

    private String description;

    @NotBlank
    private String unit;

    private int number;

    private double price;

    private boolean discount;

    @StringInList(values = {FinanceConstant.DISCOUNT_TYPE_PERCENT, FinanceConstant.DISCOUNT_TYPE_AMOUNT})
    private String discountType;

    private double discountNumber;

    private double discountPrice;

    private boolean active;

    private boolean attendance;

    @StringInList(values = {FinanceConstant.ATTENDANCE_PAID_BEFORE, FinanceConstant.ATTENDANCE_PAID_AFTER})
    private String attendancePaid;

    @StringInList(values = {FinanceConstant.ATTENDANCE_GO_SCHOOL, FinanceConstant.ATTENDANCE_ABSENT_YES, FinanceConstant.ATTENDANCE_ABSENT_NO, FinanceConstant.ATTENDANCE_EAT, FinanceConstant.ATTENDANCE_PICKUP_LATE, FinanceConstant.EAT_TYPE_NEW, FinanceConstant.ARRIVE_TYPE_NEW})
    private String attendanceType;

    @StringInList(values = {FinanceConstant.DAY_MORNING, FinanceConstant.DAY_AFTERNOON, FinanceConstant.DAY_EVENING,
            FinanceConstant.EAT_BREAKFAST, FinanceConstant.EAT_BREAKFAST_SECOND, FinanceConstant.EAT_LUNCH, FinanceConstant.EAT_AFTERNOON, FinanceConstant.EAT_AFTERNOON_SECOND, FinanceConstant.EAT_DINNER, FinanceConstant.ALL_DAY,
            FinanceConstant.EAT_BREAKFAST_1, FinanceConstant.EAT_REMAIN_1, FinanceConstant.EAT_BREAKFAST_OUT_1, FinanceConstant.EAT_REMAIN_OUT_1, FinanceConstant.EAT_DAY_2, FinanceConstant.EAT_DAY_OUT_2, FinanceConstant.EAT_BREAKFAST_OUT_2,
            FinanceConstant.ARRIVE_GO_SCHOOL, FinanceConstant.ARRIVE_ABSENT_YES, FinanceConstant.ARRIVE_ABSENT_NO,
            FinanceConstant.ARRIVE_GO_SCHOOL27, FinanceConstant.ARRIVE_GO_SCHOOL26, FinanceConstant.ARRIVE_GO_SCHOOL7,
            FinanceConstant.ARRIVE_ABSENT_YES_NO27, FinanceConstant.ARRIVE_ABSENT_YES26, FinanceConstant.ARRIVE_ABSENT_YES_NO26, FinanceConstant.ARRIVE_ABSENT_YES7, FinanceConstant.ARRIVE_ABSENT_YES_NO7,
            FinanceConstant.ONELY_SARTUDAY, FinanceConstant.ONELY_SARTUDAY_BEFORE, FinanceConstant.EAT_REPAY_DINNER26
    })
    private String attendanceDetail;

    @NotBlank
    @StringInList(values = {FinanceConstant.TYPE_SINGLE, FinanceConstant.TYPE_MULTIPLE})
    private String type;

    private boolean t1;

    private boolean t2;

    private boolean t3;

    private boolean t4;

    private boolean t5;

    private boolean t6;

    private boolean t7;

    private boolean t8;

    private boolean t9;

    private boolean t10;

    private boolean t11;

    private boolean t12;

    private boolean expired;

    @Min(0)
    @Max(28)
    private int febNumberExpired;

    @Min(0)
    @Max(30)
    private int smallNumberExpired;

    @Min(0)
    @Max(31)
    private int largeNumberExpired;

    //0 là tháng hiện tại, 1 là tháng kế tiếp với kiểu nhiều lần
    @Min(0)
    @Max(1)
    private int monthNumber;

    //ngày hết hạn với kiểu 1 lần
    private LocalDate endDateExpired;

    private boolean usingStatus;
}
