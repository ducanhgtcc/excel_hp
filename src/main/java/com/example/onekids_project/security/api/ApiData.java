package com.example.onekids_project.security.api;

import com.example.onekids_project.common.ErrorsConstant;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

/**
 * date 2021-04-24 16:14
 * api/*: đường dẫn tuyệt đối và bắt buộc dấu * phải là 1 đường dẫn, vd: api/abs, api/def
 * api/** đường dẫn tương đối và dấu ** có thể ko có gì, vd: api/des, api/df/fhe, api
 *
 * @author lavanviet
 */
@Component
public class ApiData {

    //1.  học phí và dịch vụ

    /**
     * menu học phí và dịch vụ
     *
     * @param number
     * @return
     */
    public String getFeesMenu(int number) {
        switch (number) {
            case 1:
                return "/web/fn/fees/**";
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NOT_FOUND_NUMBER_PERMISSION);
        }
    }

    /**
     * Quản lý ví
     * 1 quản lý ví
     * 2 nạp,rút ví
     * 3 thống kê ví
     *
     * @param number
     * @return
     */
    public String getFeesWallet(int number) {
        switch (number) {
            case 1:
                return "/web/fn/fees/wallet/**";
            case 2:
                return "/web/fn/fees/wallet/history/create";
            case 3:
                return "/web/fn/fees/wallet/statistical/search";
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NOT_FOUND_NUMBER_PERMISSION);
        }
    }

    /**
     * danh sách khoản
     *
     * @param number
     * @return
     */
    public String getFeesPackage(int number) {
        switch (number) {
            case 1:
                return "/web/fn/fees/package/**";
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NOT_FOUND_NUMBER_PERMISSION);
        }
    }

    /**
     * đăng ký khoản
     *
     * @param number
     * @return
     */
    public String getFeesKidsPackage(int number) {
        switch (number) {
            case 1:
                return "/web/fn/fees/kids-package/**";
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NOT_FOUND_NUMBER_PERMISSION);
        }
    }

    /**
     * Duyệt học phí
     * 1. duyệt học phí
     * 2. thay đổi số sử dụng
     * 3. duyệt khoản
     * 4. khóa khoản
     *
     * @param number
     * @return
     */
    public String getFeesApproved(int number) {
        switch (number) {
            case 1:
                return "/web/fn/fees/approved/**";
            case 2:
                return "/web/fn/fees/approved/use-number/**";
            case 3:
                return "/web/fn/fees/approved/kids-package/approved/*";
            case 4:
                return "/web/fn/fees/approved/kids-package/locked/*";
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NOT_FOUND_NUMBER_PERMISSION);
        }
    }

    /**
     * thu học phí
     * 1. thu học phí
     * 2. khóa hóa đơn
     * 3. thanh toán
     * 4. thống kê
     *
     * @param number
     * @return
     */
    public String getFeesOrder(int number) {
        switch (number) {
            case 1:
                return "/web/fn/fees/order/**";
            case 2:
                return "/web/fn/fees/order/locked/*";
            case 3:
                return "/web/fn/fees/order/payment/*";
            case 4:
                return "/web/fn/fees/order/statistical/*";
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NOT_FOUND_NUMBER_PERMISSION);
        }
    }
    // end học phí và dịch vụ

    //2. start công lương

    /**
     * menu công lương
     * 1 menu công lương
     * 2 mẫu công lương
     * 3,4,5 thiết lập tiền lương
     *
     * @param number
     * @return
     */
    public String getSalaryMenu(int number) {
        switch (number) {
            case 1:
                return "/web/fn/salary/**";
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NOT_FOUND_NUMBER_PERMISSION);
        }
    }

    /**
     * mẫu công lương
     *
     * @param number
     * @return
     */
    public String getSalarySample(int number) {
        switch (number) {
            case 1:
                return "/web/fn/salary/salary-sample/**";
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NOT_FOUND_NUMBER_PERMISSION);
        }
    }

    /**
     * thiết lập tiền lương
     * 1,2,3 thiết lập tiền lương
     *
     * @param number
     * @return
     */
    public String getSalarySetting(int number) {
        switch (number) {
            case 1:
                return "/web/fn/salary/salary-default/**";
            case 2:
                return "/web/fn/salary/salary-apply/**";
            case 3:
                return "/web/fn/salary/generate/**";
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NOT_FOUND_NUMBER_PERMISSION);
        }
    }

    /**
     * duyệt bảng lương
     * 1. menu duyệt bảng lương
     * 2. thay đổi số sử dụng
     * 3. duyệt khoản
     * 4 khóa khoản
     *
     * @param number
     * @return
     */
    public String getSalaryApproved(int number) {
        switch (number) {
            case 1:
                return "/web/fn/salary/approved/**";
            case 2:
                return "/web/fn/salary/approved/use-number/**";
            case 3:
                return "/web/fn/salary/approved/approved/**";
            case 4:
                return "/web/fn/salary/approved/locked/**";
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NOT_FOUND_NUMBER_PERMISSION);
        }
    }

    /**
     * thanh toán lương
     * 1. menu thanh toán lương
     * 2. khóa khoản lương
     * 3. thanh toán lương
     * 4. thống kê
     *
     * @param number
     * @return
     */
    public String getSalaryOrder(int number) {
        switch (number) {
            case 1:
                return "/web/fn/salary/order/**";
            case 2:
                return "/web/fn/salary/order/locked";
            case 3:
                return "/web/fn/salary/order/payment/*";
            case 4:
                return "/web/fn/salary/order/statistical/*";
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NOT_FOUND_NUMBER_PERMISSION);
        }
    }
    //end công lương

    //3. start chấm công

    /**
     * menu to chấm công
     *
     * @param number
     * @return
     */
    public String getAttendanceEmployeeMenu(int number) {
        switch (number) {
            case 1:
                return "/web/attendance-employees/**";
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NOT_FOUND_NUMBER_PERMISSION);
        }
    }

    /**
     * thiết lập chấm công
     *
     * @param number
     * @return
     */
    public String getAttendanceEmployeeConfig(int number) {
        switch (number) {
            case 1:
                return "/web/attendance-employees/config/**";
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NOT_FOUND_NUMBER_PERMISSION);
        }
    }

    /**
     * chấm công nhân viên
     *
     * @param number
     * @return
     */
    public String getAttendanceEmployeeAttendance(int number) {
        switch (number) {
            case 1:
                return "/web/attendance-employees/detail-day";
            case 2:
                return "/web/attendance-employees/arrive/**";
            case 3:
                return "/web/attendance-employees/leave/**";
            case 4:
                return "/web/attendance-employees/eat/**";
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NOT_FOUND_NUMBER_PERMISSION);
        }
    }
    //end chấm công

    //4. start thu chi nội bộ

    /**
     * menu to thu chi nội bộ
     * @param number
     * @return
     */
    /**
     * menu thu chi nội bộ
     *
     * @param number
     * @return
     */
    public String getCashInternal(int number) {
        switch (number) {
            case 1:
                return "/web/fn/cashinternal/**";
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NOT_FOUND_NUMBER_PERMISSION);
        }
    }

    /**
     * thông tin ngân hàng
     *
     * @param number
     * @return
     */
    public String getCashInternalBank(int number) {
        switch (number) {
            case 1:
                return "/web/fn/cashinternal/bank/**";
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NOT_FOUND_NUMBER_PERMISSION);
        }
    }

    /**
     * danh sách đối tượng
     *
     * @param number
     * @return
     */
    public String getCashInternalPeople(int number) {
        switch (number) {
            case 1:
                return "/web/fn/cashinternal/people/**";
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NOT_FOUND_NUMBER_PERMISSION);
        }
    }

    /**
     * quản lý quỹ
     *
     * @param number
     * @return
     */
    public String getCashInternalCashBook(int number) {
        switch (number) {
            case 1:
                return "/web/fn/cashinternal/cashbook/**";
            case 2:
                return "/web/fn/cashinternal/cashbook/history";
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NOT_FOUND_NUMBER_PERMISSION);
        }
    }

    /**
     * quản lý phiếu thu
     *
     * @param number
     * @return
     */
    public String getCashInternalIn(int number) {
        switch (number) {
            case 1:
                return "/web/fn/cashinternal/in/**";
            case 2:
                return "/web/fn/cashinternal/in/create";
            case 3:
                return "/web/fn/cashinternal/in/approved/*";
            case 4:
                return "/web/fn/cashinternal/in/payment/*";
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NOT_FOUND_NUMBER_PERMISSION);
        }
    }

    /**
     * quản lý phiếu chi
     *
     * @param number
     * @return
     */
    public String getCashInternalOut(int number) {
        switch (number) {
            case 1:
                return "/web/fn/cashinternal/out/**";
            case 2:
                return "/web/fn/cashinternal/out/create";
            case 3:
                return "/web/fn/cashinternal/out/approved/*";
            case 4:
                return "/web/fn/cashinternal/out/payment/*";
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NOT_FOUND_NUMBER_PERMISSION);
        }
    }

    /**
     * cấu hình nhà trường
     *
     * @param number
     * @return
     */
    public String getSchoolConfig(int number) {
        switch (number) {
            case 1:
                return "/web/school-config/**";
            case 2:
                return "/web/school-config/infor";
            case 3:
                return "/web/school-config/class/**";
            case 4:
                return "/web/school-config/attendance-employee/**";
            case 5:
                return "/web/school-config/finance/**";
            case 6:
                return "/web/school-config/subject/**";
            case 7:
                return "/web/school-config/evaluate-sample/**";
            case 8:
                return "/web/school-config/attendance-sample/**";
            case 9:
                return "/web/school-config/wishes-sample/**";
            case 10:
                return "/web/school-config/account-type/**";
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NOT_FOUND_NUMBER_PERMISSION);
        }
    }

    /**
     * bản tin
     *
     * @param number
     * @return
     */
    public String getNews(int number) {
        switch (number) {
            case 1:
                return "/web/appsend/search";
            case 2:
                return "/web/appsend/update-many-read";
            case 3:
                return "/web/feedbackpr/**";
            case 4:
                return "/web/feedbacktc/**";
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NOT_FOUND_NUMBER_PERMISSION);
        }
    }

    /**
     * nhận ký phụ huynh
     *
     * @param number
     * @return
     */
    public String getParentDiary(int number) {
        switch (number) {
            case 1:
                return "/web/message/**";
            case 2:
                return "/web/medicine/**";
            case 3:
                return "/web/absent-letter/**";
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NOT_FOUND_NUMBER_PERMISSION);
        }
    }

    /**
     * quản lý nhân sự
     *
     * @param number
     * @return
     */
    public String getManageEmployee(int number) {
        switch (number) {
            case 1:
                return "/web/departments/**";
            case 2:
                return "/web/employees/**";
            case 3:
                return "/web/employees/employee-notify";
            case 4:
                return "/web/employees/sms/**";
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NOT_FOUND_NUMBER_PERMISSION);
        }
    }

    /**
     * quản lý học sinh
     *
     * @param number
     * @return
     */
    public String getManageKids(int number) {
        switch (number) {
            case 1:
                return "/web/grade/**";
            case 2:
                return "/web/class/**";
            case 3:
                return "/web/student-group/**";
            case 4:
                return "/web/student/**";
            case 5:
                return "/web/student/student-notify";
            case 6:
                return "/web/student/sms/**";
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NOT_FOUND_NUMBER_PERMISSION);
        }
    }

    /**
     * chất lượng học sinh
     *
     * @param number
     * @return
     */
    public String getStudentQuality(int number) {
        switch (number) {
            case 1:
                return "/web/attendance-kids/**";
            case 2:
                return "/web/evaluate-kids/**";
            case 3:
                return "/web/album/**";
            case 4:
                return "/web/kid-quality/**";
            case 5:
                return "/web/schedules/**";
            case 6:
                return "/web/class-menu/**";
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NOT_FOUND_NUMBER_PERMISSION);
        }
    }

    /**
     * quản lý sinh nhật
     *
     * @param number
     * @return
     */
    public String getBirthday(int number) {
        switch (number) {
            case 1:
                return "/web/kids-birthday/**";
            case 2:
                return "/web/parent-birthday/**";
            case 3:
                return "/web/teacher-birthday/**";
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NOT_FOUND_NUMBER_PERMISSION);
        }
    }

    /**
     * lịch sử thông báo
     *
     * @param number
     * @return
     */
    public String getNotifyHistory(int number) {
        switch (number) {
            case 1:
                return "/web/schedulesms/**";
            case 2:
                return "/web/smscustom/**";
            case 3:
                return "/web/historysmssend/**";
            case 4:
                return "/web/smsapp/**";
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NOT_FOUND_NUMBER_PERMISSION);
        }
    }

    /**
     * lịch sử thông báo
     *
     * @param number
     * @return
     */
    public String getStatisticalFinance(int number) {
        switch (number) {
            case 1:
                return "/web/fn/statistical/**";
            case 2:
                return "/web/fn/statistical/statistical/**";
            case 3:
                return "/web/fn/statistical/money/**";
            case 4:
                return "/web/fn/statistical/kids/group/**";
            case 5:
                return "/web/fn/statistical/employee/group/**";
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NOT_FOUND_NUMBER_PERMISSION);
        }
    }

    /**
     * chấm công nhân sự
     * @param number
     * @return
     */
    public String getAbsentTeacher(int number) {
        switch (number) {
            case 1:
                return "/web/absent-teacher/**";
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NOT_FOUND_NUMBER_PERMISSION);
        }
    }


}
