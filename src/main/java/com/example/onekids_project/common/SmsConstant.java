package com.example.onekids_project.common;

public interface SmsConstant {
    String TYPE_SMS = "sms";
    String TYPE_SEND_ACCOUNT = "account";
    String TYPE_SEND_VERIFY_ACCOUNT = "verify_account";
    String SUP_NEO = "NEO";
    String SUP_VNPT = "VNPT";
    String PHONE_HEAD = "84";

    Long SMS_CODE_NEW = 17L;

    int BRAND_ADS = 0;
    int BRAND_ADS_NO = 1;
    int ONE = 1;
    int ZERO = 0;

    String USER_NAME = "{username}";
    String PASSWORD_NAME = "{password}";

    String USERNAME_CUT = "#";


    //MESSEGER THÔNG BÁO LỖI
    String NO_KID = "Học sinh chưa được kích hoạt nhận Sms hoạc thiếu thông tin phụ huynh ";
    String NO_SUPPLIER = "Cấu hình nhà cung cấp sms không tồn tại.";
    String NO_EMPLOYEE = "Giáo viên được chọn không đủ điều kiện nhận sms";
    String PASS_BUGET = "Không thể gửi vượt hạn mức Sms";
    String NO_BRAND = "Nhà trường chưa được thêm Brand";

    String SMS_KID = "kid";
    String SMS_GRAGE = "grade";
    String SMS_CLASS = "class";
    String SMS_GROUP = "group";

    String SMS_EMPLOYEE = "employee";
    String SMS_DEPARTMENT = "department";


}
