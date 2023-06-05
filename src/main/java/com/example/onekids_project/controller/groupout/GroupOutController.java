package com.example.onekids_project.controller.groupout;

import com.example.onekids_project.request.base.IdListRequest;
import com.example.onekids_project.request.employee.SearchEmployeeGroupOutRequest;
import com.example.onekids_project.request.groupout.GroupOutRequest;
import com.example.onekids_project.request.kids.ExcelGroupOutRequest;
import com.example.onekids_project.request.kids.SearchKidsGroupOutRequest;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.response.employee.EmployeeDetailGroupOutResponse;
import com.example.onekids_project.response.employee.ListEmployeeGroupOutResponse;
import com.example.onekids_project.response.excel.ExcelResponse;
import com.example.onekids_project.response.groupout.GroupOutNameResponse;
import com.example.onekids_project.response.groupout.ListGroupOutResponse;
import com.example.onekids_project.response.kids.ListStudentGroupOutResponse;
import com.example.onekids_project.response.kids.StudentGroupOutDetailResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.EmployeeService;
import com.example.onekids_project.service.servicecustom.KidsService;
import com.example.onekids_project.service.servicecustom.excelexport.ExcelExportService;
import com.example.onekids_project.service.servicecustom.groupout.GroupOutEmployeeService;
import com.example.onekids_project.service.servicecustom.groupout.GroupOutKidsService;
import com.example.onekids_project.util.RequestUtils;
import com.example.onekids_project.validate.CommonValidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * date 2021-07-12 2:21 PM
 *
 * @author nguyễn văn thụ
 */
@RestController
@RequestMapping("/web/group-out")
public class GroupOutController {

    @Autowired
    private GroupOutKidsService groupOutKidsService;
    @Autowired
    private GroupOutEmployeeService groupOutEmployeeService;
    @Autowired
    private KidsService kidsService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private ExcelExportService excelExportService;

    /**
     * tìm kiếm thông tin thư mục học sinh
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "group/kids")
    public ResponseEntity searchGroupOutCategoryKids(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        ListGroupOutResponse data = groupOutKidsService.searchGroupOutKids(principal);
        return NewDataResponse.setDataSearch(data);
    }

    /**
     * Tạo mới thư mục học sinh
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "group/kids")
    public ResponseEntity createGroupOutKids(@CurrentUser UserPrincipal principal, @Valid @RequestBody GroupOutRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        boolean data = groupOutKidsService.createGroupOutKids(principal, request);
        return NewDataResponse.setDataCreate(data);
    }

    /**
     * Sửa thư mục học sinh
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = {"group/kids/{id}"})
    public ResponseEntity updateGroupOutKids(@CurrentUser UserPrincipal principal, @PathVariable Long id, @Valid @RequestBody GroupOutRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        boolean data = groupOutKidsService.updateGroupOutKids(principal, id, request);
        return NewDataResponse.setDataUpdate(data);
    }

    /**
     * Xóa thư mục học sinh
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE, path = {"group/kids/{id}"})
    public ResponseEntity deleteGroupOutKids(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataPlus(principal);
        boolean data = groupOutKidsService.deleteGroupOutKids(principal, id);
        return NewDataResponse.setDataDelete(data);
    }

    /**
     * tìm kiếm danh sách thư mục trong trường học sinh
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "kids/group-name")
    public ResponseEntity searchGroupOutNameKids(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        List<GroupOutNameResponse> data = groupOutKidsService.findAllGroupName(principal);
        return NewDataResponse.setDataSearch(data);
    }

    /**
     * Tìm kiếm học sinh đã ra truòng
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "kids/search")
    public ResponseEntity searchGroupOut(@CurrentUser UserPrincipal principal, @Valid SearchKidsGroupOutRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        CommonValidate.checkPlusOrTeacher(principal);
        ListStudentGroupOutResponse data = kidsService.searchKidsGroupOut(principal, request);
        return NewDataResponse.setDataSearch(data);
    }

    /**
     * Tìm kiếm học sinh đã ra truòng theo id
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "kids/{id}")
    public ResponseEntity searchGroupOut(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequestPlus(principal);
        CommonValidate.checkPlusOrTeacher(principal);
        StudentGroupOutDetailResponse data = kidsService.searchKidsGroupOutById(principal, id);
        return NewDataResponse.setDataSearch(data);
    }

    /**
     * Khôi phục trạng thái ra trường của học sinh
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "kids/restore/{id}")
    public ResponseEntity restoreKidsGroupOut(@CurrentUser UserPrincipal principal, @PathVariable Long id, @RequestParam Long idClass) throws IOException {
        RequestUtils.getFirstRequestExtend(principal, id, idClass);
        boolean check = kidsService.restoreKidsGroupOut(principal, id, idClass);
        return NewDataResponse.setDataUpdate(check);
    }

    /**
     * Xuất excel ds học sinh
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "kids/excel")
    public ResponseEntity excelKidsGroupOut(@CurrentUser UserPrincipal principal, @Valid IdListRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkPlusOrTeacher(principal);
        List<ExcelResponse> responseList = excelExportService.getExcelGroupOutKids(principal, request);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * Xuất excel ds học sinh chọn lọc
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "kids/excel-proviso")
    public ResponseEntity excelKidsGroupOutProviso(@CurrentUser UserPrincipal principal, @Valid ExcelGroupOutRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkPlusOrTeacher(principal);
        List<ExcelResponse> responseList = excelExportService.getExcelGroupOutKidsProviso(principal, request);
        return NewDataResponse.setDataSearch(responseList);
    }

    //Nhân sự
    /**
     * tìm kiếm thông tin thư mục nhân sự
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "group/employee")
    public ResponseEntity searchGroupOutCategoryEmployee(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        ListGroupOutResponse data = groupOutEmployeeService.searchGroupOutEmployee(principal);
        return NewDataResponse.setDataSearch(data);
    }

    /**
     * Tạo mới thư mục nhân sự
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "group/employee")
    public ResponseEntity createGroupOutEmployee(@CurrentUser UserPrincipal principal, @Valid @RequestBody GroupOutRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        boolean data = groupOutEmployeeService.createGroupOutEmployee(principal, request);
        return NewDataResponse.setDataCreate(data);
    }

    /**
     * Sửa thư mục nhân sự
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = {"group/employee/{id}"})
    public ResponseEntity updateGroupOutEmployee(@CurrentUser UserPrincipal principal, @PathVariable Long id, @Valid @RequestBody GroupOutRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        boolean data = groupOutEmployeeService.updateGroupOutEmployee(principal, id, request);
        return NewDataResponse.setDataUpdate(data);
    }

    /**
     * Xóa thư mục nhân sự
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE, path = {"group/employee/{id}"})
    public ResponseEntity deleteGroupOutEmployee(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataPlus(principal);
        boolean data = groupOutEmployeeService.deleteGroupOutEmployee(principal, id);
        return NewDataResponse.setDataDelete(data);
    }

    /**
     * tìm kiếm danh sách thư mục trong trường nhân sự
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "employee/group-name")
    public ResponseEntity searchGroupOutNameEmployee(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        List<GroupOutNameResponse> data = groupOutEmployeeService.findAllGroupNameEmployee(principal);
        return NewDataResponse.setDataSearch(data);
    }

    /**
     * Tìm kiếm nhân sự đã ra truòng
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "employee/search")
    public ResponseEntity searchGroupOutEmployee(@CurrentUser UserPrincipal principal, @Valid SearchEmployeeGroupOutRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        CommonValidate.checkPlusOrTeacher(principal);
        ListEmployeeGroupOutResponse data = employeeService.searchEmployeeGroupOut(principal, request);
        return NewDataResponse.setDataSearch(data);
    }

    /**
     * Tìm kiếm nhân sự đã ra truòng theo id
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "employee/{id}")
    public ResponseEntity searchGroupOutEmployee(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequestPlus(principal);
        CommonValidate.checkPlusOrTeacher(principal);
        EmployeeDetailGroupOutResponse data = employeeService.findByIdEmployeeGroupOut(principal, id);
        return NewDataResponse.setDataSearch(data);
    }

    /**
     * Khôi phục trạng thái ra trường của nhân sự
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "employee/restore/{id}")
    public ResponseEntity restoreEmployeeGroupOut(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequest(principal);
        boolean check = employeeService.restoreEmployeeGroupOut(principal, id);
        return NewDataResponse.setDataUpdate(check);
    }

    /**
     * Xuất excel ds nhân sự
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "employee/excel")
    public ResponseEntity excelEmployeeGroupOut(@CurrentUser UserPrincipal principal, @Valid IdListRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkPlusOrTeacher(principal);
        List<ExcelResponse> responseList = excelExportService.getExcelGroupOutEmployee(principal, request);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * Xuất excel ds học sinh chọn lọc
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "employee/excel-proviso")
    public ResponseEntity excelEmployeeGroupOutProviso(@CurrentUser UserPrincipal principal, @Valid ExcelGroupOutRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkPlusOrTeacher(principal);
        List<ExcelResponse> responseList = excelExportService.getExcelGroupOutEmployeeProviso(principal, request);
        return NewDataResponse.setDataSearch(responseList);
    }

}
