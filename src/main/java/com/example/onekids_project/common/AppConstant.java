package com.example.onekids_project.common;

public interface AppConstant {

    boolean APP_TRUE = true;
    boolean APP_FALSE = false;
    String YES = "yes";
    String NO = "no";
    int PAGE_NUMBER = 0;
    int MAX_PAGE_ITEM = 20;
    long NUMBER_ZERO = 0L;
    int ZERO = 0;
    int ONE = 1;

    String DIRECTORY_DEFAULT = "C:\\\\xampp\\\\htdocs\\\\upload\\\\";
    String URL_DEFAULT = "https://upload.onekids.edu.vn/";
    String URL_DEFAULTTEST = "https://upload1.onekids.edu.vn/";
    String UPLOAD_IN_ALBUM = "C:\\xampp\\htdocs\\upload\\";
    String URL_FONT = "C:\\xampp\\htdocs\\Unicode\\arial.ttf";
    String JSON_FOLDER = "diemdanhjson";
    String SAVE_FILE = "savefile";
    String ID_SCHOOL_LOGIN_ERROR = "Không tồn tại id_school tương ứng với người dùng đang đăng nhập";
    String ID_SCHOOL_LOGIN_CONTENT = "Không tồn tại trường để thực hiện thao tác";
    String ID_URL_OBJECT_DIFFERENT_ERROR = "id in url not equal to id in object";
    String INVALID_PAGE_NUMBER = "Số trang không hợp lệ";

    String EMPLOYEE_STATUS_LEAVE = "Nghỉ làm";
    String EMPLOYEE_STATUS_WORKING = "Đang làm";
    String EMPLOYEE_STATUS_RETAIN = "Tạm nghỉ";
    String EMPLOYEE_STATUS_MARRIED = "Đã kết hôn";
    String EMPLOYEE_STATUS_SINGLE = "Độc thân";
    String EMPLOYEE_WORK = "work";
    String EMPLOYEE_RETAIN = "retain";
    String EMPLOYEE_LEAVE = "leave";
    Integer NUMBER_PICTURE_IN_ALBUM = 30;
    String FATHER = "Bố";
    String MOTHER = "Mẹ";
    String FATHER_KEY = "father";
    String MOTHER_KEY = "mother";
    String HIDDEN = "Ẩn danh";
    String NO_HIDDEN = "Không ẩn danh";
    String MALE = "Nam";
    String FEMALE = "Nữ";
    String ALL = "Tất cả";
    String ACCOUNT_TYPE_PARENT = "Phụ huynh";
    String EVALUATE_TYPE_WEEK = "week";
    String EVALUATE_TYPE_MONTH = "month";
    String SCHOOL = "Nhà trường";
    String TEACHER = "Giáo viên";
    String SYSTEM = "Hệ thống";

    String CLASS = "Lớp";
    String ALBUMSCHOOL = "Trường";
    String SEND_MULTI = "Gửi nhiều người";

    //các loại của danh bạ: phu huynh, giáo viên, nhà trường, hỗ trợ của onekids
    String PHONE_TYPE_PARENT = "phoneParent";
    String PHONE_TYPE_TEACHER = "phoneTeacher";
    String PHONE_TYPE_SCHOOL = "phoneSchool";
    String PHONE_TYPE_SUPPORT = "phoneSupport";

    String PARENT_NAME = "Phụ huynh";
    String KID_NAME = "Học sinh";
    String TEACHER_NAME = "Giáo viên";
    String SCHOOL_NAME = "Nhà trường";

    String GUIDE_LINK = "http://google.com";

    String AUTO_SEND = "autoSend";

    String USER_SEND = "userSend";

    String CODE_SUCCESS = "200";

    String ACCOUNT_TYPE_FEEDBACK_PARENT = "Phụ huynh";
    String ACCOUNT_TYPE_FEEDBACK_TEACHER = "Giáo viên";
    String ACCOUNT_TYPE_FEEDBACK_ONEKIDS = "Hệ thống Onekids";


    String DEFAULT_CONTENT_SCHOOL = "Nhà trường đã xác nhận góp ý.Trân trọng!";
    String DEFAULT_CONTENT_DEL = "Nội dung đã được thu hồi!";

    String APP_PLUS = "APP_PLUS";
    String APP_TEACHER = "APP_TEACHER";
    String APP_ONEKIDS = "APP_ONEKIDS";

    String USERNAME_PLUS = "#NT";
    String USERNAME_TEACHER = "#GV";
    String USERNAME_PARENT = "#PH";

    String TYPE_SCHOOL = "Trường";
    String TYPE_ONEKIDS = "one";
    String TYPE_VIDEO_SCHOOL = "school";
    String TYPE_CLASS = "Lớp";
    String TYPE_VIDEO = "Video";
    String TYPE_FACEBOOK = "Facebook";
    String TYPE_OTHER = "Khác";

    String ANONYMOUS = "Ẩn danh";

    String VIDEO = "Video";

    String SUCCESS_SAVE = "Lưu thành công";
    String SUCCESS_CREATE = "Tạo thành công";
    String SUCCESS_UPDATE = "Cập nhật thành công";
    String SUCCESS_DELETE = "Xóa thành công";
    String SUCCESS_RESTORE = "Khôi phục thành công";
    String SUCCESS_SEARCH = "Lấy dữ liệu thành công";
    String SUCCESS_ACTIVE = "Kích hoạt thành công";
    String SUCCESS_UN_ACTIVE = "Bỏ kích hoạt thành công";
    String SUCCESS_APPROVED = "Duyệt thành công";
    String SUCCESS_UN_APPROVED = "Bỏ duyệt thành công";
    String SUCCESS_LOCKED = "Khóa thành công";
    String SUCCESS_UN_LOCKED = "Bỏ khóa thành công";
    String GENERATE_SUCCESS = "Khởi tạo thành công";
    String SUCCESS_CONFIRM = "Xác nhận thành công";
    String REVOKE_SUCCESS = "Thu hồi thành công";
    String NOTIFY_SMS = "Gửi Sms thành công";
    String NOTIFY_APP = "Gửi thông báo thành công";

    int USERNAME_MIN_LENGHT = 6;
    int USERNAME_EXTEND_MIN_LENGHT = 9;
    int PASSWORD_MIN_LENGHT = 6;

    String STUDYING = "STUDYING";
    String STUDY_WAIT = "STUDY_WAIT";
    String RESERVE = "RESERVE";
    String LEAVE_SCHOOL = "LEAVE_SCHOOL";


    String HANDLE_ACCOUNT = "Xử lý";
    String NO_INFOR_REPPRESENTIION = "Chưa có";

    String OLD_ACCOUNT = "oldAccount";
    String NEW_ACCOUNT = "newAccount";

    String BOY = "boy";
    String GIRL = "girl";


    String TYPE_TEACHER = "teacher";
    String TYPE_CONFIRM = "confirm";

    //chưa đăng nhập lần nào
    String LOGIN_YET = "loginYet";
    //đã từng đăng nhập nhưng hiện tại ko đăng nhập
    String LOGIN_NO = "loginNo";
    //hiện tại đang đăng nhập
    String LOGIN_YES = "loginYes";

    String TYPE_COMMON = "common";
    String APP_SEND = "appsend";
    String TYPE_CELEBRATE = "Ngày lễ";
    String TYPE_LEAVE = "Điểm danh về";
    String TYPE_ARRIVE = "Điểm danh đến";

    String DELETE_TRUE = "deleteTrue";
    String DELETE_FALSE = "deleteFalse";
    String DELETE_COMPLETE = "deleteComplete";

    String SMS = "sms";
    String CUSTOM = "custom";
    String SMS_ALL = "all";

    String FULL = "full";
    String PART = "part";
    String EMPTY = "empty";

    String SORT_VIETNAMESE_UTF8 = "collate utf8_vietnamese_ci ";
    String SORT_VIETNAMESE_UTF8MB4 = "collate utf8mb4_vietnamese_ci ";
    String ISAPPROVE = "IsApprove";
    String SENT = "Sent";
    String REVOKE = "Revoke";
    String READ = "Read";
    String OTHER_CLASS = "OTHER_CLASS";

    String ALBUM_ZIP = "albumzip";
    String CASHINTERNALOUT = "PC";
    String CASHINTERNALIN = "PT";
    String TYPE_ACCOUNT = "account";
    String SMS_ACCOUNT = "Tin nhắn cấp tài khoản";
    String SMS_VERY = "Tin nhắn cấp mã xác minh";
    String SEND_TYPE_COMMON = "common";
    String URL_DEFAULT_FILE = "https://upload.onekids.edu.vn/sysfiles/other/DEFAULT_ATTENDANCE_NO_DATA.xlsx";
    String SPACE_EXPORT_ID = " - ";
    String DAY_OFF = "Ngày nghỉ";
    String CONFIRM_YES = "Đã xác nhận";
    String CONFIRM_NO = "Chưa xác nhận";
    String ROLE_CONFIG = "Vai trò cấu hình";
    String ROLE_COMMON = "Vai trò chung";
    String ROLE_FINANCE = "Vai trò tài chính";
    String ROLE_TEACHER = "Vai trò giáo viên";
    //thunv
    String APPROVE_TRUE = "approveTrue";
    String APPROVE_FALSE = "approveFalse";
    String APPROVE_DELL = "approveDel";
    String PAY_TRUE = "payTrue";
    String PAY_FALSE = "payFalse";

    String GROUP_OUT_ABSENT = "Nghỉ hẳn";
    String GROUP_OUT_LEAVE = "Ra trường";
    String GROUP_OUT_OTHER = "Khác";

    String EXCEL_SCHOOL = "Trường: ";
    String EXCEL_CLASS = "Lớp: ";
    String EXCEL_DATE = "Ngày: ";
    String EXCEL_TIME = "Thời gian: ";
    String EXCEL_KIDS_NAME = "Học sinh: ";

    String CHART_DATE = "date";
    String CHART_WEEK = "week";
    String CHART_MONTH = "month";
    String CHART_YEAR = "year";
    String CHART_ALL = "all";
    String CHART_DETAIL = "detail";
    String CHART_STUDDING = "studding";
    String CHART_GRADE = "grade";
    String CHART_CLASS = "class";
    String CHART_DEPARTMENT = "department";
    String KID_SEARCH_START_DATE = "startDate";
    String KID_SEARCH_BIRTHDAY = "birthday";
}
