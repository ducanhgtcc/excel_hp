package com.example.onekids_project.common;

/**
 * message cho mobile
 * cách để tên biến
 * - tìm kiếm thì để tiền tố FIND_....
 * - tạo thì để tiền tố CREATE_...
 * - cập nhật thì để UPDATE_....
 * - xóa thì để DELETE_...
 */
public interface MessageConstant {
    String MESSAGE_CONFIRM = " Xác nhận lời nhắn thành công";
    String ABSENT_CONFIRM = " Xác nhận xin nghỉ thành công";
    String REVOKE = "Thu hồi phản hồi thành công";
    String MESSAGE_REVOKE = "Thu hồi lời nhắn thành công";
    String MESSAGE_SENDREPLY = "Gửi phản hồi thành công";
    String MESSAGE_UPDATEREPLY = "Cập nhật phản hồi thành công";
    String CHANGE_CLASS = "Thay đổi lớp thành công";
    String CHANGE_SCHOOL = "Thay đổi trường thành công";
    String FIND_NEWS = "Lấy tin tức thành công";
    String FIND_ACCOUNT = "Lấy thông tin tài khoản thành công";
    String UPDATE_AVATAR = "Cập nhật ảnh đại diện thành công";
    String DELETE_PICTURES = "Xóa ảnh thành công!";
    String DELETE_ALBUM = "Xóa album ảnh thành công";
    String CREATE_ALBUM = "Tạo album thành công";
    String UPDATE_PASSWORD = "Thay đổi mật khẩu thành công";
    String FIND_CONTACT_TEACHER = "Lấy danh bạ giáo viên thành công";
    String SEARCH_MEDICINE = "Lấy dữ liệu dặn thuốc thành công";
    String SEARCH_ABSENT = "Lấy dữ liệu xin nghỉ thành công";
    String SEARCH_ABSENT_DETAIL = "Xem chi tiết xin nghỉ thành công";
    String SEARCH_DETAIL_MEDICINE = "Xem chi tiết dặn thuốc thành công!";
    String FIND_CONTACT_PARENT = "Lấy danh bạ phụ huynh thành công";
    String FIND_CONTACT_KID = "Lấy thông tin học sinh thành công";
    //thành ghi-----------------
    String UPDATE_PICTURE = "Cập nhật Album thành công";
    String SEARCH_HISTORY_NOTIFI = "Tìm kiếm lịch sử thông báo thành công";
    String HISTORY_NOTIFI_REVOKE = "Thu hồi thông báo thành công";
    String VIEW_DETAIL_HISTORYNOTIFI = "Xem chi tiết thông báo thành công";
    String REVOKE_NOTIFI = "Thông báo đã được thu hồi";
    String FIND_JSON_URL = "Lấy dữ liệu thành công";
    String MEDICINE_CONFIRM = " Xác nhận dặn thuốc thành công";
    String FEEDBACK_CONFIRM = " Xác nhận góp ý thành công";
    String APPROVE_ALBUM = "Duyệt album thành công";
    String APPROVE_PICTURE = "Duyệt ảnh đã chọn thành công";
    String DELETE_NOTIFI = "Xóa thông báo thành công";
    String APPOVE_NOTIFI = "Duyệt thông báo thành công";
    String SENDEL_NOTIFI = "Thu hồi thông báo thành công";
    String DELETE = "Xóa đối tượng thành công";
    String CREATE_CASHINTERNAL = "Tạo phiếu chi thành công";
    String CREATE_CASHCOLLECT = "Tạo phiếu thu thành công";
    String CREATE_PEOPLETYPE = "Tạo đối tượng thành công";

    //a cường-----------------------
    String CREATE_IDENTIFY = "Tạo ảnh định dạng thành công";
    String FIND_KID_LIST = "Lấy danh sách học sinh thành công";
    String FIND_KID = "Lấy ảnh điểm danh học sinh thành công";

    //thắng ghi--------------------------------
    String CREATE_HEIGHT_WEIGHT = "Tạo chiều cao, cân nặng thành công";
    String CREATE_NOTIFY = "Tạo thông báo thành công";
    String CREATE_NOTIFY_KID = "Tạo thông báo học cho sinh thành công";
    String CREATE_NOTIFY_EMPLOYEE = "Tạo thông báo cho giáo viên thành công";
    String CREATE_NOTIFY_FAIL = "Tạo thông báo không thành công";
    String DELETE_HEIGHT_WEIGHT = "Xóa chiều cao, cân nặng thành công";
    String CREATE_BIRTHDAY = "Tạo lời chúc sinh nhật thành công";
    String CREATE_KIDS_EXCEL = "Tạo học sinh từ excel thành công";
    String CREATE_EMPLOYEE_EXCEL = "Tạo nhân sự từ excel thành công";
    String SEND_ACCOUNT_EMPLOYEE = "Gửi tài khoản cho nhân viên thành công";
    String SEND_ACCOUNT_STUDENT = "Gửi tài khoản cho phụ huynh thành công";
    String SMS_SEND = "Gửi SMS thành công";
    String SMS_ACCOUNT= "Gửi tài khoản qua SMS thành công";
    String SEND_SMS_STUDENT = "Gửi tin nhắn cho phụ huynh thành công";
    String SEND_SMS_FAIL = "Gửi lại tin nhắn thành công";
    String SEND_SMS_EMPLOYEE = "Gửi tin nhắn cho nhân viên thành công";
    String SEND_ACCOUNT_STUDENT_FAIL = "Lỗi gửi tài khoản cho phụ huynh";
    String SEND_CODE_VERIFY_ACCOUNT_KID = "Gửi mã xác thực thành công";
    String ATENDANCE_ARRIVE_CREATE = "Tạo điểm danh đến thành công";
    String ATENDANCE_NO = "Không có dữ liệu điểm danh";
    String ATENDANCE_LEAVE_CREATE = "Tạo điểm danh về thành công";
    String ATENDANCE_ABSENT_YES = "Học sinh được nghỉ học";
    String FIND_EVALUATE_STATUS_KID = "Lấy dữ liệu nhận xét học sinh thành công";
    String ACTIVE_SARALY_DEFAULT = "Kích hoạt khoản công lương thành công";
    String CANCEL_SARALY_DEFAULT = "Hủy kích hoạt khoản công lương thành công";
    String APPROVE_KID_DATE = "Duyệt nhận xét ngày thành công";
    String APPROVE_KID_WEEK = "Duyệt nhận xét tuần thành công";
    String APPROVE_KID_MONTH = "Duyệt nhận xét tháng thành công";
    String APPROVE_KID_PERIODIC = "Duyệt nhận xét định kỳ thành công";
    String CREATE_SALARY_DEFAULT_FAIL = "Bạn cần chọn tháng áp dụng cho khoản công lương";
    String SALARY_PAID = "Thanh toán thành công";


    //viêt-----------------------------------
    String MAX_FILE = "Số file tối đa là ";
    String FIND_EVALUATE_DATE = "Lấy nhận xét ngày thành công";
    String FIND_EVALUATE_WEEK = "Lấy nhận xét tuần thành công";
    String FIND_EVALUATE_MONTH = "Lấy nhận xét tháng thành công";
    String FIND_EVALUATE_PERIODIC = "Lấy nhận xét định kỳ thành công";
    String FIND_HOME = "Lấy dữ liệu home thành công";
    String CLASS_DELETE = "Lớp bạn đang chọn đã bị xóa";
    String CLASS_NOT_FOUND = "Không có lớp nào được chọn";
    String EVALUATE_APPROVED = "Bạn không được sửa các nhận xét đã duyệt";
    String EVALUATE_DATE_SAVE = "Nhận xét ngày thành công";
    String EVALUATE_DATE_COMMON_SAVE = "Nhận xét ngày chung thành công";
    String EVALUATE_WEEK_SAVE = "Nhận xét tuần thành công";
    String EVALUATE_WEEK_COMMON_SAVE = "Nhận xét tuần chung thành công";
    String EVALUATE_MONTH_SAVE = "Nhận xét tháng thành công";
    String EVALUATE_MONTH_COMMON_SAVE = "Nhận xét tháng chung thành công";
    String EVALUATE_PERIODIC_SAVE = "Nhận xét định kỳ thành công";
    String EVALUATE_PERIODIC_COMMON_SAVE = "Nhận xét định kỳ chung thành công";
    String EVALUATE_EDIT = "Dữ liệu đã được chỉnh sửa";
    String EVALUATE_MAX_FILE = "Không được quá 3 file";
    String EVALUATE_DATE_STATISTICAL_MONTH = "Lấy ngày có nhận xét thành công";
    String STATISTICAL_EVALUATE_MONTH = "Lấy danh sách nhận xét lớp thành công";
    String STATISTICAL_EVALUATE_PERIODIC = "Lấy thống kê nhận xét định kỳ trong tháng thành công";
    String EVALUATE_DELETE = "Không được xóa nhận xét";
    String EVALUATE_EDIT_APPROVED = "Bạn không được sửa nhận xét đã duyệt";
    String FIND_EVALUATE_SAMPLE = "Tìm kiếm mẫu điểm danh thành công";
    String FIND_EVALUATE_TOTAL_KID = "Lấy số thông báo thành công";
    String FIND_KID_EVALUATE_DATE = "Lấy nhận xét ngày của một học sinh thành công";
    String COUNT_KID_DATE_UNREAD_MONTH = "Lấy tổng số ngày chưa đọc trong tháng thành công";
    String COUNT_KID_DATE_HAVE_MONTH = "Lấy danh sách ngày có nhận xét trong tháng thành công";
    String CREATE_KID_DATE = "Gửi phản hồi nhận xét ngày thành công";
    String CREATE_KID_WEEK = "Gửi phản hồi nhận xét tuần thành công";
    String CREATE_KID_MONTH = "Gửi phản hồi nhận xét tháng thành công";
    String CREATE_KID_PERIODIC = "Gửi phản hồi nhận xét định kỳ thành công";
    String REVOKE_KID_DATE = "Thu hồi nhận xét ngày thành công";
    String REVOKE_KID_WEEK = "Thu hồi nhận xét tuần thành công";
    String REVOKE_KID_MONTH = "Thu hồi nhận xét tháng thành công";
    String REVOKE_KID_PERIODIC = "Thu hồi nhận xét định kỳ thành công";
    String FIND_KID_EVALUATE_WEEK = "Lấy nhận xét tuần của một học sinh thành công";
    String FIND_KID_EVALUATE_MONTH = "Lấy nhận xét tháng của một học sinh thành công";
    String FIND_KID_EVALUATE_PERIODIC = "Lấy nhận xét định kỳ của một học sinh thành công";
    String VIEW_KID_EVALUATE_WEEK = "Xem nhận xét tuần của một học sinh thành công";
    String VIEW_KID_EVALUATE_MONTH = "Xem nhận xét tháng của một học sinh thành công";
    String VIEW_KID_EVALUATE_PERIODIC = "Xem nhận xét định kỳ của một học sinh thành công";
    String CREATE_SCHOOL = "Tạo trường thành công";
    String FIND_PROFILE = "Lấy trang cá nhân thành công";
    String UPDATE_PROFILE = "Cập nhật trang cá nhân thành công";

    //finance
    String ORDER_EMPTY = "Không có hóa đơn nào";
    String WALLET_PARENT_IN = "Nạp tiền thành công";

    //Thunv
    String ABSENT_SUCCESS = "Tạo xin nghỉ thành công";
    String ABSENT_READ = "Đánh dấu đã đọc thành công";
    //thunv
    String REVOKE_SUCCESS = "Thu hồi thành công";
}
