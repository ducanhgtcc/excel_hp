package com.example.onekids_project.mobile.plus.controller;

import com.example.onekids_project.mobile.plus.request.department.SearchEmployeeRequest;
import com.example.onekids_project.mobile.plus.response.department.EmployeePlusDetailResponse;
import com.example.onekids_project.mobile.plus.response.department.ListDepartmentPlusResponse;
import com.example.onekids_project.mobile.plus.response.department.ListEmployeePlusResponse;
import com.example.onekids_project.mobile.plus.service.servicecustom.DepartmentPlusService;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mob/plus/department")
public class DepartmentListController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DepartmentPlusService departmentPlusService;

    /**
     * Tìm kiếm danh sách phòng ban
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public ResponseEntity findDepartment(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequestPlus(principal);
        ListDepartmentPlusResponse listDepartmentPlusResponse = departmentPlusService.searchDepartment(principal);
        return NewDataResponse.setDataSearch(listDepartmentPlusResponse);
    }

    /**
     * Tìm kiếm danh sách nhân sự
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity searchEmployee(@CurrentUser UserPrincipal principal, SearchEmployeeRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        ListEmployeePlusResponse listEmployeePlusResponse = departmentPlusService.searchEmployeePlus(principal, request);
        return NewDataResponse.setDataSearch(listEmployeePlusResponse);
    }

    /**
     * Xem chi tiết phòng ban
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/detail/{id}")
    public ResponseEntity findEmployeePlusDetail(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequestPlus(principal, id);
        EmployeePlusDetailResponse employeePlusDetailResponse = departmentPlusService.findEmployeeDetail(principal, id);
        return NewDataResponse.setDataSearch(employeePlusDetailResponse);
    }
}
