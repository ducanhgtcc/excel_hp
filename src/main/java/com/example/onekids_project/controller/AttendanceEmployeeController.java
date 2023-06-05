package com.example.onekids_project.controller;

import com.example.onekids_project.importexport.model.AttendanceEmployeeDate;
import com.example.onekids_project.importexport.model.AttendanceEmployeeMonth;
import com.example.onekids_project.importexport.service.EmployeeExcelService;
import com.example.onekids_project.request.attendanceemployee.*;
import com.example.onekids_project.request.employeeSalary.AttendanceEmployeeConfigRequest;
import com.example.onekids_project.request.employeeSalary.EmployeeConfigSearchRequest;
import com.example.onekids_project.response.attendanceemployee.*;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.response.excel.ExcelNewResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.employeesaraly.AttendanceEmployeeService;
import com.example.onekids_project.util.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

/**
 * date 2021-03-09 2:36 CH
 *
 * @author ADMIN
 */

@RestController
@RequestMapping("/web/attendance-employees")
public class AttendanceEmployeeController {

    @Autowired
    private AttendanceEmployeeService attendanceEmployeeService;

    @Autowired
    private EmployeeExcelService employeeExcelService;

    /**
     * lấy dữ liệu điểm danh của nhân viên
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/config")
    public ResponseEntity searchAttendanceConfigEmployee(@CurrentUser UserPrincipal principal, @Valid EmployeeConfigSearchRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        List<AttendanceConfigEmployeeResponse> responseList = attendanceEmployeeService.searchAttendanceConfigEmployee(principal, request);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * update điểm danh của nhân viên
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/config/{id}")
    public ResponseEntity updateAttendanceConfigEmployee(@CurrentUser UserPrincipal principal, @PathVariable Long id, @RequestBody @Valid AttendanceEmployeeConfigRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = attendanceEmployeeService.updateAttendanceConfigEmployee(principal, id, request);
        return NewDataResponse.setDataUpdate(check);
    }

    /**
     * lấy dữ liệu điểm danh của nhân viên
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/detail-day")
    public ResponseEntity searchAttendanceDetailDay(@CurrentUser UserPrincipal principal, @Valid AttendanceEmployeeSearchRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        List<AttendanceDetailDayEmployeeResponse> responseList = attendanceEmployeeService.searchAttendanceDetailDay(principal, request);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * lấy dữ liệu điểm danh đến cho nhân viên
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/arrive")
    public ResponseEntity searchAttendanceArrive(@CurrentUser UserPrincipal principal, @Valid AttendanceEmployeeSearchRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        List<AttendanceArriveEmployeeResponse> responseList = attendanceEmployeeService.searchAttendanceArrive(principal, request);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * lưu điểm danh đến cho một nhân viên
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/arrive/one")
    public ResponseEntity saveAttendanceArrive(@CurrentUser UserPrincipal principal, @RequestBody @Valid AttendanceEmployeeArriveRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = attendanceEmployeeService.saveAttendanceArrive(principal, request);
        return NewDataResponse.setDataUpdate(check);
    }

    /**
     * lưu điểm danh đến cho nhiều nhân viên
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/arrive/many")
    public ResponseEntity saveMultiAttendanceArrive(@CurrentUser UserPrincipal principal, @RequestBody @Valid List<AttendanceEmployeeArriveRequest> request) {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = attendanceEmployeeService.saveMultiAttendanceArrive(principal, request);
        return NewDataResponse.setDataUpdate(check);
    }

    /**
     * cập nhật nội dung điểm danh đến
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/arrive/content/{id}")
    public ResponseEntity saveContentAttendanceArrive(@CurrentUser UserPrincipal principal, @PathVariable Long id, @ModelAttribute @Valid AttendanceEmployeeContentRequest request) throws IOException {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = attendanceEmployeeService.saveContentAttendanceArrive(principal, request);
        return NewDataResponse.setDataUpdate(check);
    }


    /**
     * lấy dữ liệu điểm danh về cho nhân viên
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/leave")
    public ResponseEntity searchAttendanceLeave(@CurrentUser UserPrincipal principal, @Valid AttendanceEmployeeSearchRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        List<AttendanceLeaveEmployeeResponse> responseList = attendanceEmployeeService.searchAttendanceLeave(principal, request);
        return NewDataResponse.setDataSearch(responseList);
    }


    /**
     * lưu điểm danh về cho 1 nhân viên
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/leave/one")
    public ResponseEntity saveAttendanceLeave(@CurrentUser UserPrincipal principal, @RequestBody @Valid AttendanceEmployeeLeaveRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = attendanceEmployeeService.saveAttendanceLeave(principal, request);
        return NewDataResponse.setDataUpdate(check);
    }

    /**
     * lưu điểm danh về cho nhiều nhân viên
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/leave/many")
    public ResponseEntity saveMultiAttendanceLeave(@CurrentUser UserPrincipal principal, @RequestBody @Valid List<AttendanceEmployeeLeaveRequest> request) {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = attendanceEmployeeService.saveMultiAttendanceLeave(principal, request);
        return NewDataResponse.setDataUpdate(check);
    }

    /**
     * cập nhật nội dung điểm danh về
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/leave/content/{id}")
    public ResponseEntity saveContentAttendanceLeave(@CurrentUser UserPrincipal principal, @PathVariable Long id, @ModelAttribute @Valid AttendanceLeaveEmployeeContentRequest request) throws IOException {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = attendanceEmployeeService.saveContentAttendanceLeave(principal, request);
        return NewDataResponse.setDataUpdate(check);
    }

    /**
     * lấy dữ liệu điểm danh về cho nhân viên
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/eat")
    public ResponseEntity searchAttendanceEat(@CurrentUser UserPrincipal principal, @Valid AttendanceEmployeeSearchRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        List<AttendanceEatEmployeeResponse> responseList = attendanceEmployeeService.searchAttendanceEat(principal, request);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * lưu dữ liệu điểm danh về cho 1 nhân viên
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/eat/one")
    public ResponseEntity saveAttendanceEat(@CurrentUser UserPrincipal principal, @RequestBody @Valid AttendanceEmployeeEatRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = attendanceEmployeeService.saveAttendanceEat(principal, request);
        return NewDataResponse.setDataCreate(check);
    }

    /**
     * lưu điểm danh về cho nhiều nhân viên
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/eat/many")
    public ResponseEntity saveAttendanceEatMulti(@CurrentUser UserPrincipal principal, @RequestBody @Valid List<AttendanceEmployeeEatRequest> request) {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = attendanceEmployeeService.saveAttendanceEatMulti(principal, request);
        return NewDataResponse.setDataCreate(check);
    }

    /**
     * xuất file excel điểm danh nhân viên theo ngày
     *
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/export-attendance-employee")
    public ResponseEntity getAllAttendance(@CurrentUser UserPrincipal principal, @Valid AttendanceEmployeeSearchRequest request) throws IOException {
        RequestUtils.getFirstRequest(principal, request);
        List<AttendanceEmployeeDate> modelList = attendanceEmployeeService.searchAllAttendanceDate(principal, request);
        ByteArrayInputStream in = employeeExcelService.exportAttendanceEmployeeDate(principal, modelList, request.getDate());
        return ResponseEntity.ok().body(new InputStreamResource(in));

    }

    /**
     * xuất file excel điểm danh nhân viên theo ngày NEW
     *
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/export-attendance-employee-new")
    public ResponseEntity getAllAttendanceNew(@CurrentUser UserPrincipal principal, @Valid AttendanceEmployeeSearchRequest request) throws IOException {
        RequestUtils.getFirstRequest(principal, request);
        List<AttendanceEmployeeDate> modelList = attendanceEmployeeService.searchAllAttendanceDate(principal, request);
        List<ExcelNewResponse> list = employeeExcelService.exportAttendanceEmployeeDateNew(principal, modelList, request.getDate());
        return NewDataResponse.setDataSearch(list);

    }

    /**
     * xuất file excel điểm danh của các nhân viên trong một tháng
     *
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/export-attendance-employee-month")
    public ResponseEntity getAllAttendanceMonth(@CurrentUser UserPrincipal principal, @Valid AttendanceEmployeeSearchRequest request) throws IOException {
        RequestUtils.getFirstRequest(principal, request);
        List<AttendanceEmployeeMonth> modelList = attendanceEmployeeService.searchAllAttendanceMonth(principal, request);
        ByteArrayInputStream in = employeeExcelService.exportAttendanceEmployeeMonth(principal, modelList, request.getDate());
        return ResponseEntity.ok().body(new InputStreamResource(in));
    }

    /**
     * xuất file excel điểm danh của các nhân viên trong một tháng NEW
     *
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/export-attendance-employee-month-new")
    public ResponseEntity getAllAttendanceMonthNew(@CurrentUser UserPrincipal principal, @Valid AttendanceEmployeeSearchRequest request) throws IOException {
        RequestUtils.getFirstRequest(principal, request);
        List<AttendanceEmployeeMonth> modelList = attendanceEmployeeService.searchAllAttendanceMonth(principal, request);
        List<ExcelNewResponse> list = employeeExcelService.exportAttendanceEmployeeMonthNew(principal, modelList, request.getDate());
        return NewDataResponse.setDataSearch(list);
    }
}
