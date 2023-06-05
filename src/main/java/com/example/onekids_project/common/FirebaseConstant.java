package com.example.onekids_project.common;

public interface FirebaseConstant {
    String ACTION_KEY = "click_action";
    String ACTION_VALUE = "FLUTTER_NOTIFICATION_CLICK";

    String ROUTER_KEY = "router";

    String ID_KID = "idKid";
    String ID_CLASS = "idClass";
    String ID_SCHOOL = "idSchool";

    String ID_KEY = "id";

    String FULL_NAME = "{Full Name}";
    String KID_NAME = "{Kid_Name}";
    String DATE_NAME = "{dd/mm/yyyy}";
    String REPLACE_KID_NAME = "{Kid_Name}";
    String REPLACE_FULL_NAME = "{Full Name}";

    String CONTEN = "Thông báo từ nhà trường.";
    String TITLE = "Onekid";

    int CONTEN_LENGHT = 50;


    String MESSAGE = "message";

    String MEDICINE = "medicine";

    String ABSENT = "absent";

    String FEEDBACK = "feedback";

    String SYSTEM = "system";

    int SEND_AGAIN = 2;

    long TIME_EXPIRE_MINI_ANDROID = 2 * 60 * 60 * 1000;//hết hạn trong 2 giờ
    long TIME_EXPIRE_MEDIUM_ANDROID = 24 * 60 * 60 * 1000;//hết hạn trong 1 ngày
    long TIME_EXPIRE_LARGE_ANDROID = 48 * 60 * 60 * 1000;//hết hạn trong 2 ngày
    long TIME_EXPIRE_MINI_IOS = 120;//thêm số phút hết hạn: 60p
    long TIME_EXPIRE_MEDIUM_IOS = 1440;//1 ngày
    long TIME_EXPIRE_LARGE_IOS = 2880;//2 ngày

    String APNS_EXPIRATION = "apns-expiration";
    String DEFAULT = "default";

    /*
    category
     */
    String CATEGORY_NOTIFY_HISTORY = "notifyHistory";
    //thông báo
    String CATEGORY_NOTIFY = "notify";
    //lời nhắn
    String CATEGORY_MESSAGE = "message";
    //dặn thuốc
    String CATEGORY_MEDICINE = "medicine";
    //xin nghỉ
    String CATEGORY_ABSENT = "absent";
    //album
    String CATEGORY_ALBUM = "album";
    //nhận xét
    String CATEGORY_EVALUATE = "evaluate";
    //điểm danh
    String CATEGORY_ATTENDANCE = "attendance";
    //góp ý
    String CATEGORY_FEEDBACK = "feedback";
    //sinh nhật
    String CATEGORY_BIRTHDAY = "birthday";
    //danh bạ
    String CATEGORY_PHONEBOOK = "phonebook";
    //nạp rút ví
    String CATEGORY_WALLET = "wallet";
    //hiện thị hóa đơn
    String CATEGORY_ORDER_SHOW = "orderShow";
    //thanh toán
    String CATEGORY_ORDER_PAYMENT = "orderPayment";
    String CATEGORY_ORDER_NOTIFY = "orderNotify";
    String CATEGORY_ABSENT_TEACHER = "absentTeacher";
    String CATEGORY_NEWS = "news";
    String CATEGORY_CASH_INTERNAL = "cashInternal";

    /*
    router
     */
    String ROUTER_NOTIFY_HISTORY_PLUS = "/history_notification";
    String ROUTER_NOTIFY_PLUS = "/notification";
    String ROUTER_HOME_TEACHER = "/page_home";
    String ROUTER_HOME_PARENT = "/home_page";
    String ROUTER_MESSAGE_COMMON = "/message";
    String ROUTER_MEDICINE_COMMON = "/medicine";
    String ROUTER_ABSENT_TEACHER_PLUS = "/truant_kid";
    String ROUTER_ABSENT_PARENT = "/truant";
    String ROUTER_ALBUM_COMMON = "/album";
    String ROUTER_EVALUATE_PARENT_PLUS = "/comment";
    String ROUTER_EVALUATE_TEACHER = "/comment_v2";
    String ROUTER_ATTENDANCE = "/roll_call";
    String ROUTER_FEEDBACK_PLUS = "/list_feedback";
    String ROUTER_FEEDBACK_PARENT_TEACHER = "/feedback";
    String ROUTER_FEES_PARENT = "/tuition";
    String ROUTER_FEES_TEACHER = "/salary";
    String ROUTER_NEWS="/news";
    String ROUTER_CASH_INTERNAL="/cash_internal";

    String NAME_PARENT = "phụ huynh";
    String NAME_TEACHER = "giáo viên";
    String NAME_PLUS = "Nhân sự";

    //thu nv
    String REPLACE_KID_NAME_DOW = "{kid_name}";
    String REPLACE_NUMBER = "{number}";
    String REPLACE_CLASS_NAME = "{class_name}";
    String REPLACE_MONTH = "{month}";
    String REPLACE_DATE = "{date}";
    String REPLACE_MONEY_TOTAL = "{moneyTotal}";
    String REPLACE_MONEY_PAID = "{moneyPaid}";
    String REPLACE_MONEY_NO_PAID = "{moneyNoPaid}";
    String REPLACE_NUMBER_SUCCESS = "{numberSuccess}";
    String REPLACE_NUMBER_NO_SUCCESS = "{numberNoSuccess}";
    String REPLACE_MONEY_IN = "{moneyIn}";
    String REPLACE_MONEY_OUT = "{moneyOut}";

}
