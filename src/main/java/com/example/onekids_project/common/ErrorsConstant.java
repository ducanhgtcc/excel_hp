package com.example.onekids_project.common;

public interface ErrorsConstant {
    String ERROR_SYSTEM = "Không thành công. Bạn vui lòng thử lại sau!";
    String ERROR_COMMON_FILE = "Không thành công. Bạn vui lòng thử lại sau!!!";
    String ERROR_COMMON_SQL = "Không thành công. Bạn vui lòng thử lại sau!!";
    String ERROR_COMMON_NULL_POINT = "Không thành công. Bạn vui lòng thử lại sau.";
    String ERROR_TOKEN = "Lỗi xác thực người dùng";
    String BAD_CREDENTIALS = "Thông tin đăng nhập không đúng";
    String ERROR_INPUT = "Lỗi dữ liệu truyền vào";
    String ERROR_MAX_FILE = "Kích thước vượt quá 5MB";
    String ERROR_INPUT_PARAMETER = "Lỗi dữ liệu truyền vào ở tham số";
    String ERROR_INPUT_SQL = "Lỗi thao tác với dữ liệu";
    String NOT_FOUND_DATABASE_FIND = "Không tìm thấy dữ liệu trong hệ thống";
    String NOT_FOUND_DATABASE_DELETE = "Không tồn tại dữ liệu trong hệ thống";
    String NON_UNIQUE_RESULT = "Có nhiều hơn một giá trị trong hệ thống";
    String NOT_SUPPORT_METHOD = "Không hỗ trợ cách truy vấn này";
    String SQL_SYNTAX = "Lỗi cú pháp SQL";
    String NOT_FOUND_FILE_DELETE = "Không tìm thấy file cần xóa trong hệ thống";
    String NO_DATE = "Ngày không được để trống";
    String DATE_AFTER = "Ngày không được lớn hơn ngày hiện tại";
    String DATE_VALID = "Ngày không hợp lệ";
    String NO_TIME = "Không có thời gian";
    String NO_DATA = "Thông tin không được để trống";
    String BIRTHDAY_EMPTY = "Ngày sinh không được để trống";
    String NO_PICTURE = "Ảnh không được để trống";
    String DATA_NULL = "Không có dữ liệu";
    String NO_DATA_ACCOUNT = "Thông tin tài khoản không được để trống";
    String NO_SUPPORT_ACCOUNT = "Chức năng chưa hỗ trợ trên tài khoản này";
    String PAGE_NUMBER_INVALID = "Số trang phải lớn hơn hoặc bằng 1";
    String MAX_PAGE_ITEM_INVALID = "Số bản ghi trong một trang phải lớn hơn hoặc bằng 1";
    String USERNAME_EXIST = "Tên đăng nhập đã tồn tại";
    String USERNAME_BLANK = "Tên đăng nhập không được để trống";
    String USERNAME_LENGHT = "Tên đăng nhập phải lớn hơn hoặc bằng " + AppConstant.USERNAME_MIN_LENGHT + " ký tự";
    String USERNAME_INVALID = "Tên đăng nhập không hợp lệ";
    String USERNAME_INVALID_PLUS = "Tên đăng nhập nhà trường không hợp lệ";
    String USERNAME_INVALID_TEACHER = "Tên đăng nhập giáo viên không hợp lệ";
    String USERNAME_INVALID_PARENT = "Tên đăng nhập phụ huynh không hợp lệ";
    String PASSWORD_BLANK = "Mật khẩu không được để trống";
    String PASSWORD_LENGHT = "Mật khẩu phải lớn hơn hoặc bằng " + AppConstant.PASSWORD_MIN_LENGHT + " ký tự";
    String PASSWORD_INVALID = "Mật khẩu không hợp lệ";
    String NO_SCHOOL_IN_AGENT = "Đại lý không có trường nào";
    String VERIFY_CODE_LENGTH = "Mã xác thực bao gồm 6 ký tự";
    String VERIFY_CODE_ADMIN = "Mã xác thực chỉ bao gồm các ký tự A-Z";
    String NO_APP_TYPE = "AppType không được để trống";
    String NO_INFOR_ACCOUNT = "Thông tin tạo tài khoản không được để trống";
    String INVALID_APPTYPE = "AppType không đúng định dạng";
    String PHONE_BLANK = "Số điện thoại không được để trống";
    String PHONE_LENGH = "Số điện thoại bao gồm 10 số";
    String NO_BRAND = "Trường chưa được thêm brand name";
    String PHONE_INVALID = "Số điện thoại không hợp lệ";
    String APPTYPE_BLANK = "Loại người dùng không được để trống";
    String APPTYPE_INVALID = "Loại người dùng không hợp lệ";
    String CODE_BLANK = "Mã xác thực không được để trống";
    String CODE_LENGTH = "Mã xác thực bao gồm 6 ký tự";
    String CODE_WRONG = "Mã xác thực không chính xác";
    String ERROR_FIREBASE = "Lỗi gửi thông báo qua ứng dụng";
    String ERROR_BUTTON = "Thao tác không được để trống";
    String USER_EMPTY = "Người dùng không được để trống";
    String NOT_FOUND_USER = "Không tìm thấy người thông tin người dùng";
    String EMPTY_USER_LOGIN = "Tài khoản đã hết hiệu lực";

    String ACCOUNT_EMPTY = "Không tìm thấy nhân sự trong trường";
    String ACCOUNT_EMPLOYEE_EMPTY= "Không tìm thấy nhân sự nào";
    String ACCOUNT_KID_EMPTY = "Không có học sinh nào";
    String ACCOUNT_KID_ACTIVE = "Không có học sinh nào được kích hoạt";
    String ACCOUNT_KID_UNACTIVE = "Học sinh đang chọn không được kích hoạt";
    String ACCOUNT_DELETE = "Tài khoản đã bị xóa";
    String ACCOUNT_INACTIVATE = "Tài khoản không được kích hoạt";
    String ACCOUNT_DELETE_SCHOOL = "Tài khoản này đã bị xóa";
    String SCHOOL_NOT_FOUND = "Không có trường nào";
    String CONTENT_BLANK = "Nội dung không được để trống";
    String ACCOUNT_INACTIVATE_SCHOOL = "Tài khoản này không được kích hoạt";
    String ACCOUNT_NOT_FOUND = "Tài khoản đã bị xóa hoặc không được kích hoạt";
    String ACCOUNT_MANY_INSCHOOL = "Có nhiều hơn một tài khoản trong một trường đang hợp lệ";
    String NOT_FOUND_EMPLOYEE = "Không tìm thấy nhân sự";
    String NOT_FOUND_KIDS = "Không tìm thấy học sinh";
    String NOT_FOUND_AVATAR_EMPLOYEE = "Không tìm thấy ảnh đại diện nhân viên";
    String NOT_FOUND_AVATAR_PARENT = "Không tìm thấy ảnh đại diện phụ huynh";
    String MAX_SIZE_FILE = "Kích thước file quá " + UploadDownloadConstant.MAX_SIZE + " MB";

    //finance
    String FN_DISCOUNT = "Thông tin giảm giá không được để trống";
    String EXIST_PACKAGE_KIDS = "Đã tồn tại khoản này";
    String EXIST_PACKAGE_DEFAULT = "Đã tồn tại khoản này trong mặc định";
    String EXIST_PACKAGE = "Đã tồn tại khoản này trong đăng ký";
    String EXPIRED_PACKAGE = "Khoản này đã hết hạn";
    String DELETE = "Không thể xóa khoản của lớp";
    String DELETE_KIDS_PACKAGE = "Khoản đã trả";
    String ERROR_UPDATE_SALARY = "Không thể sửa khoản cách hiện tại " + (FinanceConstant.UPDATE_ACCEPT + 1) + " tháng trở về trước";
    String APPROVED_KIDS_PACKAGE = "Khoản đã duyệt";
    String LOCKED_KIDS_PACKAGE = "Khoản đã khóa";
    String ZERO_MONEY = "Khoản 0 đồng";
    String PAYMENT_KIDS_PACKAGE = "Khoản đã được thanh toán";
    String APPROVED_SALARY = "Khoản công lương đã duyệt";
    String LOCKED_SALARY = "Khoản công lương đã khóa";
    String WALLET_BALANCE = "Số dư trong ví không đủ";
    String PAYMENT_ZERO = "Số tiền thanh toán phải lớn hơn 0";
    String NOT_ENOUGH_PACKAGE = "Không tìm thấy đủ khoản thu";
    String CASH_BOOK_LOCKED = "Số tiền mặt đã bị khóa";
    String CASH_BOOK_DATE = "Ngày thao tác không thế nằm ngoài khoảng chu kì";
    String CATEGORY_NOT_FOUND = "Thông tin không thuộc loại nào";
    String CASH_BOOK_TYPE = "Thông tin không thuộc kiểu nào";
    String CASH_BOOK_NOT_ENOUGHT = "Số tiền trong quỹ không đủ";
    String ORDER_LOCKED = "Hóa đơn đã bị khóa";
    String WALLET_NOT_ENOUGH = "Số tiền nhập từ ví lớn hơn số dư ví";
    String ORDER_MODIFIED = "Thông tin hóa đơn dã bị thay đổi";
    String BANK_CHECKED = "Không thể xóa tài khoản chính";
    String MONEY_INPUT_INVALID = "Số tiền nhập lớn hơn số tiền phải thanh toán";
    String KIDS_NAME = "Nhập họ tên học sinh";
    String ATTENDANCE_TYPE = "Không tìm thấy loại điểm danh";
    String CONFIRM_FAIL = "Xác nhận thất bại";
    String DELETE_OBJECT = "Không cho phép xóa";
    String LIST_EMPTY = "Không có giá trị nào";
    String FIREBASE_EMPTY_TITILE = "Tiêu đề không được để trống";
    String FIREBASE_EMPTY_BODY = "Nội dung không được để trống";
    String FIREBASE_EMPTY_ROUTER = "Router không được để trống";
    String DATE_NOT_FOUND = "Không tìm thấy ngày tương ứng";
    String EMPTY_OBJECT = "Không có đối tượng nào được gửi";
    String WEB_SYSTEM_TITLE = "not found websystem title by id";
    String FIREBASE_TYPE_INVALID = "Loại gửi thông báo không hợp lệ";
    String FIREBASE_APP_TYPE = "Đối tượng nhận thông báo không hợp lệ";
    String FIREBASE_TOKEN_EMPTY = "Không có TokenFirebase";
    String FIREBASE_ROUTER_EMPTY = "Không tìm thấy router";
    String FIREBASE_CATEGORY_EMPTY = "Không tìm thấy loại gửi thông báo";
    String FIREBASE_CATEGORY_INACTIVE = "receiver notify not active: {category} of {apptype}";
    String NOT_FOUND_NUMBER_PERMISSION = "Không tìm thấy số phù hợp";
    String PAYMENT_NEGATIVE = "Tổng tiền các khoản đã chọn là số âm";

}
