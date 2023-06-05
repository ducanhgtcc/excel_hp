package com.example.onekids_project.common;

/**
 * message cho mobile
 * - tìm kiếm thì để tiền tố FIND_....
 * - tạo thì để tiền tố CREATE_...
 * - cập nhật thì để UPDATE_....
 * - xóa thì để DELETE_...
 */
public interface MessageWebConstant {

    String SUCCESS_CREATE = "Tạo thành công";
    String SUCCESS_SAVE = "Lưu thành công";
    String SUCCESS_SAVE_REPLY = "Lưu phản hồi thành công";
    String SUCCESS_UPDATE = "Cập nhật thành công";
    String SUCCESS_DELETE = "Xóa thành công";
    String SUCCESS_SEARCH = "Lấy dữ liệu thành công";

    String SUCCESS_CONFIRM = "Xác nhận thành công";
    String SUCCESS_REVOKE = "Thu hồi thành công";
    String SUCCESS_UNREVOKE = "Hủy thu hồi thành công";


    //----------------việt-----------------------
    String SUCCESS_ACTIVE = "Kích hoạt thành công";
    String SUCCESS_UNACTIVE = "Hủy kích hoạt thành công";
    String SUCCESS_APPROVED = "Duyệt thành công";
    String SUCCESS_UNAPPROVED = "Bỏ duyệt thành công";
    String SUCCESS_LOCKED = "Khóa thành công";
    String SUCCESS_UNLOCKED = "Bỏ khóa thành công";
    String FIND_PROFILE = "Tìm kiếm trang cá nhân thành công";
    String UPDATE_PROFILE = "Cập nhật trang cá nhân thành công";
    String FIND_ACCOUNT = "Tìm kiếm danh sách tài khoản thành công";
    String UPDATE_ACCOUNT = "Cập nhật tài khoản thành công";
    String CREATE_ACCOUNT = "Tạo tài khoản thành công";
    String CREATE_ACCOUNT_SYSTEM = "Tạo tài khoản hệ thống thành công";
    String LOGIN_SUCCESS = "Đăng nhập thành công";
    String FIND_STUDENT = "Tìm kiếm thành công";
    String UPDATE_ACTIVE_ACCOUNT = "Kích hoạt tài khoản thành công";
    String UPDATE_UNACTIVE_ACCOUNT = "Hủy kích hoạt tài khoản thành công";
    String UPDATE_ACTIVE_SMS = "Kích hoạt SMS thành công";
    String UPDATE_UNACTIVE_SMS = "Hủy kích hoạt SMS thành công";
    String UPDATE_ACTIVE_SEND_SMS = "Kích hoạt gửi SMS thành công";
    String UPDATE_UNACTIVE_SEND_SMS = "Hủy kích hoạt nhận SMS thành công";
    String CREATE_STUDENT = "Thêm mới học sinh thành công";
    String UPDATE_STUDENT = "Cập nhật học sinh thành công";
    String CREATE_EMPLOYEE = "Thêm mới nhân viên thành công";
    String UPDATE_EMPLOYEE = "Cập nhật nhân viên thành công";
    String USERNAME_NOT_EXIST = "Tên đăng nhập chưa tồn tại";
    String UPDATE_NEW_PHONE = "Cấp tài khoản thành công";
    String VALID_VERIFY_CODE = "Mã xác thực hợp lệ";
    String CREATE_DATA_SYSTEM = "Tạo dữ liệu chung cho hệ thống thành công";
    String CREATE_FOLDER_SYSTEM = "Tạo folder '0' cho hệ thống thành công";
    String UPDATE_SCHOOL = "Cập nhật trường thành công";
    String CREATE_NOTIFY = "Tạo thông báo thành công";
    String SAVE_EVALUATE = "Lưu nhận xét thành công";
    String CHANGE_CLASS = "Chuyển lớp thành công";
    String START_DATE_STUDYING = "Ở trạng thái đang học không thể chọn ngày nhập học lớn hơn ngày hiện tại";
    String START_DATE_WAIT = "Ở trạng thái chờ học không thể chọn ngày nhập học nhỏ hơn hoặc bằng ngày hiện tại";
    String DELETE_CLASS_EXIST_EMPLOYEE = "Lớp còn tồn tại nhân sự";
    String DELETE_CLASS_EXIST_KIDS = "Lớp còn tồn tại học sinh";
    String DELETE_CLASS_SUCCESS = "Xóa lớp thành công";
    String DELETE_ACCOUNT_TYPE_FAIL = "Tồn tại người dùng đang áp dụng đối tượng này";
    String UPDATE_PHONE_SMS = "Cập nhật số điện thoại nhận SMS thành công";
    String NO_ACCOUNT = "Không có tài khoản nào được chọn";
    String ACCOUNT_UNIQUE = "Chỉ được chọn một tài khoản";
    String MERGE_KID = "Chuyển học sinh sang tài khoản khác thành công";
    String SORT_ROW = "Sắp xếp thành công";
    String ERROR_DELETE_DEPARTMENT = "Còn tồn tại nhân sự trong phòng ban";
    String GENERATE_KIDS_PACKAGE = "Khởi tạo khoản thu thành công";
    String GENERATE_ORDER_KIDS = "Khởi tạo hóa đơn thành công";
    String VIEW_ACTIVE = "Hiện thị thành công";
    String VIEW_INACTIVE = "Bỏ hiện thị thành công";
    String WALLET_IN = "Nạp tiền vào ví thành công";
    String WALLET_OUT = "Rút tiền từ ví thành công";
    String PAYMENT_SUCCESS = "Thanh toán thành công";
    String BANK_CHECKED = "Chọn tài khoản chính thành công";
    //---------------thành-----------------------
    String DELETE_PEOPLE_TYPE = "Đối tượng đang được lưu trữ, Xóa thất bại!";
    String CONFIRM = "Đã xác nhận";
    String NOT_CONFIRM = "Chưa xác nhận";
    String PARENT = "Phụ huynh";
    String EMPLOYEE = "Nhân viên";
    String BIRTHDAY = "Chúc mừng sinh nhật";
    String SUCCESS = "Thành công";
    String FAIL = "Thất bại";
    String UPDATE_BANK_INFO = "Cập nhật thông tin tài khoản thành công";
    String DELETE_BANK_INFO = "Xóa thông tin tài khoản ngân hàng thành công";
    String CREATE_BANK_INFO = "Thêm thông tin tài khoản ngân hàng thành công";
    String UPDATE_BANK = "Cập nhật thông tin ngân hàng thành công";
    String DELETE_STUDENT = "Xóa học sinh thành công";
    String DELETE_EMPLOYEE = "Xóa nhân sự thành công";
    String UPDATE_STATUS = "Cập nhật trạng thái thành công";
    String NUMBER_TRANSFER = "Cập nhật số tính toán sang số sử dụng thành công";
    String SEND_NOTIFY = "Gửi thông báo thành công";
    String ZERO_SALARY = "Khoản đã có trong mặc định";
    String ZERO_SALARY_CREATE = "Không có khoản nào được tạo";
    //--------------a cường----------------------

    //---------------thắng-----------------------
    String CREATE_FILE_PICTURE = "Thêm mới file, ảnh thành công";

    //thu nv
    String READ_SUCCESS = "Duyệt đã đọc thành công";
}
