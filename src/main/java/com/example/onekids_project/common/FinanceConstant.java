package com.example.onekids_project.common;

public interface FinanceConstant {
    String ATTENDANCE_GO_SCHOOL = "goSchool";
    String ATTENDANCE_GO_WORK = "goWork";

    String ATTENDANCE_LEAVE = "leave";
    String ATTENDANCE_ABSENT_YES = "absentYes";
    String ATTENDANCE_ABSENT_NO = "absentNo";

    String ATTENDANCE_EAT = "eat";
    String ATTENDANCE_PICKUP_LATE = "pickupLate";
    String ATTENDANCE_PICKUP_SOON = "pickupSoon";

    String DAY_MORNING = "morning";
    String DAY_AFTERNOON = "afternoon";
    String DAY_EVENING = "evening";

    String EAT_BREAKFAST = "eatBreakfast";
    String EAT_BREAKFAST_SECOND = "eatBreakfastSecond";
    String EAT_LUNCH = "eatLunch";
    String EAT_AFTERNOON = "eatAfternoon";
    String EAT_AFTERNOON_SECOND = "eatAfternoonSecond";
    String EAT_DINNER = "eatDinner";

    /**
     * điểm danh ăn kiểu mới
     */
    String EAT_TYPE_NEW = "eatNew";
    //loại 1
    //ăn bữa sáng
    String EAT_BREAKFAST_1 = "eatBreakfast1";
    //ăn ít nhất 1 bữa ngoài bữa sáng
    String EAT_REMAIN_1 = "eatRemain1";
    //trả lại tiền ăn sáng tháng trước
    String EAT_BREAKFAST_OUT_1 = "eatBreakfastOut1";
    //trả lại tiền ăn các bữa còn lại tháng trước
    String EAT_REMAIN_OUT_1 = "eatRemainOut1";

    //loại 2
    //ăn ít nhất 1 bữa trong ngày(kể cả bữa sáng)
    String EAT_DAY_2 = "eatDay2";
    //trả lại tiền ăn tháng trước trong ngày(1 ngày chỉ rơi và 1 trong 2 cái trả lại này)
    String EAT_BREAKFAST_OUT_2 = "eatBreakfastOut2";
    //trả lại tiền ăn sáng tháng trước(có ăn ít nhất 1 bữa trừ sáng)
    String EAT_DAY_OUT_2 = "eatDayOut2";


    /**
     * điểm danh đi học kiểu mới
     */
    String ARRIVE_TYPE_NEW = "arriveNew";
    //đi học ít nhất 1 buổi trong ngày(sáng, chiều, tối)
    String ARRIVE_GO_SCHOOL = "arriveGoSchool";
    String ARRIVE_GO_SCHOOL27 = "arriveGoSchool27";
    String ARRIVE_GO_SCHOOL26 = "arriveGoSchool26";
    String ARRIVE_GO_SCHOOL7 = "arriveGoSchool7";
    //ko đi học buổi nào và nghỉ ít nhất 1 buổi có phép
    String ARRIVE_ABSENT_YES = "arriveAbsentYes";
    //không đi học buổi nào và nghỉ ít nhất 1 buổi không phép
    String ARRIVE_ABSENT_NO = "arriveAbsentNo";
    String ARRIVE_ABSENT_YES_NO27 = "arriveAbsentYesNo27";
    String ARRIVE_ABSENT_YES26 = "arriveAbsentYes26";
    String ARRIVE_ABSENT_YES_NO26 = "arriveAbsentYesNo26";
    String ARRIVE_ABSENT_YES7 = "arriveAbsentYes7";
    String ARRIVE_ABSENT_YES_NO7 = "arriveAbsentYesNo7";
    String ONELY_SARTUDAY = "onlySartuday";
    String ONELY_SARTUDAY_BEFORE = "onlySartudayBefore";


    String ATTENDANCE_PAID_BEFORE = "before";
    String ATTENDANCE_PAID_AFTER = "after";

    String ALL_DAY = "allDay";

    String DISCOUNT_TYPE_PERCENT = "percent";
    String DISCOUNT_TYPE_AMOUNT = "money";

    String INTERNAL = "internal";
    String EXTERNAL = "external";
    String TYPE_SINGLE = "single";
    String TYPE_MULTIPLE = "multiple";

    String CATEGORY_IN = "in";
    String CATEGORY_OUT = "out";
    //phụ vụ cho thanh toán cả thu và chi
    String CATEGOTY_BOTH = "both";

    String PAST_MONTH = "pastMonth";
    String NOW_MONTH = "nowMonth";
    String NEXT_MONTH = "nextMonth";

    int UPDATE_ACCEPT = 1;

    String CASH_BOOK_KID = "KID";
    String CASH_BOOK_EMP = "EMP";
    String CASH_BOOK_SCH = "SCH";
    String CASH_BOOK_OTHER = "OTHER";

    String CASH_BOOK_ORIGIN_UNAPPROVED = "UNAPPROVED";
    String CASH_BOOK_ORIGIN_DELETE = "DELETE";
    String CASH_BOOK_ORIGIN_SCHOOL_OUT = "SCHOOL_OUT";
    String CASH_BOOK_ORIGIN_SCHOOL_IN = "SCHOOL_IN";
    String CASH_BOOK_ORIGIN_SCHOOL_PAYMENT = "SCHOOL_PAYMENT";
    String CASH_BOOK_ORIGIN_EMPLOYEE_PAYMENT = "EMPLOYEE_PAYMENT";
    String CASH_BOOK_ORIGIN_KID_PAYMENT = "KID_PAYMENT";


    String WALLET_TYPE_SCHOOL = "school";
    String WALLET_TYPE_PARENT = "parent";

    String TYPE_POSITIVE = "positive";
    String TYPE_ZERO = "zero";

    String TYPE_MONTH = "month";
    String TYPE_DATE = "date";

    String GENERATE_AUTO = "auto";
    String GENERATE_PART = "part";
    String GENERATE_MANUAL = "manual";

    int ORDER_MONTH_PAST = 1;
    int ORDER_MONTH_FUTURE = 2;

    String EXPORT_ORDER = "exOrder";
    String EXPORT_IN = "exIn";
    String EXPORT_OUT = "exOut";
    String EXPORT_INOUT = "exInOut";
    String EXPORT_INOUT_TRUE = "exInOutTrue";
    String EXPORT_INOUT_TOTAL = "exInOutTotal";

    String WALLET_UNCONFIRM_PARENT = "parentUnConfirm";
    String WALLET_UNCONFIRM_SCHOOL = "schoolUnConfirm";
    String WALLET_UNCONFIRM = "unConfirm";
    String WALLET_CONFIRM = "confirm";

    String GO_SCHOOL_TIME = "goSchoolTime";
    String ABSENT_COMMON = "absentCommon";
    String ABSENT_TIME = "absentTime";

    String ABSENT_DATE_TO_DATE = "absentDateToDate";
    String ABSENT_DATE_TO_DATE_NO26 = "absentDateToDateNo26";
    String ABSENT_DATE_TO_DATE_YES_NO26 = "absentDateToDateYesNo26";
    String ABSENT_DATE_TO_DATE_NO78 = "absentDateToDateNo78";
    String ABSENT_DATE_TO_DATE_YES_NO78 = "absentDateToDateYesNo78";

    String GO_WORK_DATE_TO_DATE = "goWorkDateToDate";
    String GO_WORK_DATE_TO_DATE26 = "goWorkDataToDate26";
    String GO_WORK_DATE_TO_DATE78 = "goWorkDataToDate78";

    String PAYMENT_CASH = "CASH";
    String PAYMENT_TRANSFER = "TRANSFER";
    String PAYMENT_BOTH = "BOTH";

    String EAT_REPAY_DINNER26 = "eatRepayDinner26";

    String PACKAGE_UNIT = "Lần";
    String PACKAGE_DESCRIPTION = "Khoản hệ thống";

}
