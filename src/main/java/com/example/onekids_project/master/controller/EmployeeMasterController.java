package com.example.onekids_project.master.controller;

import com.example.onekids_project.common.MessageConstant;
import com.example.onekids_project.common.MessageWebConstant;
import com.example.onekids_project.controller.EmployeeController;
import com.example.onekids_project.master.request.employee.EmployeeSearchAdminRequest;
import com.example.onekids_project.master.request.employee.UpdateEmployeeAdminRequest;
import com.example.onekids_project.master.response.employee.ListEmployeeAdminResponse;
import com.example.onekids_project.master.service.CommonMasterService;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.EmployeeService;
import com.example.onekids_project.service.servicecustom.InfoEmployeeSchoolService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/web/employees-master")
public class EmployeeMasterController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private InfoEmployeeSchoolService infoEmployeeSchoolService;
    @Autowired
    private CommonMasterService commonMasterService;

    /**
     * Tìm kiếm nhân viên
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public ResponseEntity searchEmployee(@CurrentUser UserPrincipal principal, @Valid EmployeeSearchAdminRequest request) {
        logger.info("username: {}, fullName: {}, {}", principal.getUsername(), principal.getFullName(), request);
        ListEmployeeAdminResponse response = infoEmployeeSchoolService.searchEmployeeAdmin(request);
        return NewDataResponse.setDataSearch(response);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/extra")
    public ResponseEntity update(@CurrentUser UserPrincipal principal, @Valid @RequestBody UpdateEmployeeAdminRequest request) {
        logger.info("username: {}, fullName: {}, {}", principal.getUsername(), principal.getFullName(), request);
        boolean data = employeeService.updateEmployeeAdmin(request);
        return NewDataResponse.setDataCustom(data, MessageWebConstant.UPDATE_EMPLOYEE);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity deleteKids(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        logger.info("username: {}, fullName: {}, {}", principal.getUsername(), principal.getFullName(), id);
        boolean check = employeeService.deleteEmployeeAdmin(id);
        return NewDataResponse.setDataDelete(check);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/restore/{id}")
    public ResponseEntity restoreKids(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        logger.info("username: {}, fullName: {}, {}", principal.getUsername(), principal.getFullName(), id);
        boolean check = employeeService.restoreEmployeeAdmin(id);
        return NewDataResponse.setDataRestore(check);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/update/password")
    public ResponseEntity updatePasswordEmployee(@CurrentUser UserPrincipal principal, @RequestParam List<Long> idList, @RequestParam String newPassword) {
        logger.info("username: {}, fullName: {}, {}, {}", principal.getUsername(), principal.getFullName(), idList, newPassword);
        commonMasterService.updatePasswordManyEmployee(idList, newPassword);
        return NewDataResponse.setDataCustom(true, MessageConstant.UPDATE_PASSWORD);
    }

}
