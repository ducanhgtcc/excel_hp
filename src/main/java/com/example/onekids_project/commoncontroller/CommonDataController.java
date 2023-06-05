package com.example.onekids_project.commoncontroller;

import com.example.onekids_project.master.response.MenuSupportResponse;
import com.example.onekids_project.response.accounttype.AccountTypeOtherResponse;
import com.example.onekids_project.response.classes.MaClassOtherResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.response.department.DepartmentOtherResponse;
import com.example.onekids_project.response.grade.GradeUniqueResponse;
import com.example.onekids_project.response.school.ListAppIconPlusResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.*;
import com.example.onekids_project.util.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * date 2021-05-26 08:57
 *
 * @author lavanviet
 */
@RestController
@RequestMapping("/web/common-data")
public class CommonDataController {
    @Autowired
    private GradeService gradeService;
    @Autowired
    private MaClassService maClassService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private AppIconPlusService appIconPlusService;
    @Autowired
    private MenuSupportService menuSupportService;

    /**
     * lấy danh sách phòng ban
     *
     * @param principal
     * @return
     */
    @GetMapping("/departments")
    public ResponseEntity getDepartment(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        List<DepartmentOtherResponse> responseList = departmentService.findDepartmentCommon(principal);
        return NewDataResponse.setDataSearch(responseList);
    }

    @GetMapping("/account-type")
    public ResponseEntity getAccountType(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequestPlus(principal);
        List<AccountTypeOtherResponse> responseList = employeeService.findAllAccountTypeByIdSchool(principal.getIdSchoolLogin());
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * danh sách khối trong trường
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/grade-in-school")
    public ResponseEntity searchGradeInSchool(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        List<GradeUniqueResponse> responseList = gradeService.findGradeInSchool(principal);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * tìm kiếm danh sách lớp theo trường
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/class-in-school")
    public ResponseEntity findAllOther(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        List<MaClassOtherResponse> responseList = maClassService.findAllMaClassOther(principal.getIdSchoolLogin());
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * tìm kiếm danh dách lớp theo khối
     *
     * @param principal
     * @param idGrade
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/class-in-grade")
    public ResponseEntity searchClassInGrade(@CurrentUser UserPrincipal principal, @RequestParam Long idGrade) {
        RequestUtils.getFirstRequest(principal, idGrade);
        List<MaClassOtherResponse> responseList = maClassService.findClassInGrade(principal, idGrade);
        return NewDataResponse.setDataSearch(responseList);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/support")
    public ResponseEntity showSupport(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        List<MenuSupportResponse> responseList = menuSupportService.showMenuSupport();
        return NewDataResponse.setDataSearch(responseList);
    }

}
