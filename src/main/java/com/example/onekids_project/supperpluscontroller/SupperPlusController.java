package com.example.onekids_project.supperpluscontroller;

import com.example.onekids_project.common.MessageWebConstant;
import com.example.onekids_project.dto.InfoEmployeeSchoolDTO;
import com.example.onekids_project.request.employee.*;
import com.example.onekids_project.response.common.DataResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.response.employee.ListEmployeePlusNewResponse;
import com.example.onekids_project.response.school.ListAppIconPlusResponse;
import com.example.onekids_project.response.supperplus.SchoolInfoConfigResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.AppIconPlusAddSerivce;
import com.example.onekids_project.service.servicecustom.EmployeeService;
import com.example.onekids_project.service.servicecustom.InfoEmployeeSchoolService;
import com.example.onekids_project.service.servicecustom.SchoolInfoService;
import com.example.onekids_project.util.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

/**
 * date 2021-05-31 09:38
 *
 * @author lavanviet
 */
@RestController
@RequestMapping("/web/supperPlus")
public class SupperPlusController {
    @Autowired
    private InfoEmployeeSchoolService infoEmployeeSchoolService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private AppIconPlusAddSerivce appIconPlusAddSerivce;
    @Autowired
    private SchoolInfoService schoolInfoService;

    @RequestMapping(method = RequestMethod.GET, value = "/plus/search")
    public ResponseEntity searchEmployeePlus(@CurrentUser UserPrincipal principal, @Valid EmployeePlusSearchNew search) {
        RequestUtils.getFirstRequestPlus(principal, search);
        ListEmployeePlusNewResponse data = infoEmployeeSchoolService.searchEmployeePlus(principal, search);
        return NewDataResponse.setDataSearch(data);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/plus/create")
    public ResponseEntity createEmployee(@CurrentUser UserPrincipal principal, @Valid @RequestBody CreateEmployeeRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        boolean checkCreate = employeeService.createEmployee(request, principal);
        return NewDataResponse.setDataCustom(checkCreate, MessageWebConstant.CREATE_EMPLOYEE);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/plus/update/{id}")
    public ResponseEntity updateEmployee(@PathVariable(name = "id") Long id, @CurrentUser UserPrincipal principal, @Valid @RequestBody UpdateEmployeeRequest updateEmployeeRequest) {
        RequestUtils.getFirstRequestPlus(principal, updateEmployeeRequest);
        boolean data = employeeService.updateEmployee(principal, id, updateEmployeeRequest);
        return NewDataResponse.setDataCustom(data, MessageWebConstant.UPDATE_EMPLOYEE);
    }

    @DeleteMapping("/plus/{id}")
    public ResponseEntity deleteEmployee(@PathVariable(name = "id") Long id, @CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequestPlus(principal, id);
        boolean check = employeeService.deleteEmployee(principal, id);
        return NewDataResponse.setDataDelete(check);
    }

    @GetMapping("/plus/{id}")
    public ResponseEntity findById(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequestPlus(principal, id);
        InfoEmployeeSchoolDTO data = employeeService.findByIdEmployee(id);
        return NewDataResponse.setDataSearch(data);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/plus/icon-plus-create")
    public ResponseEntity getIconPlusCreate(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequestPlus(principal);
        ListAppIconPlusResponse listAppIconPlusResponse = appIconPlusAddSerivce.findAppIconPlusAddCreate(principal.getIdSchoolLogin());
        return DataResponse.getData(listAppIconPlusResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/plus/icon-plus-update/{id}")
    public ResponseEntity getIconPlusUpdate(@CurrentUser UserPrincipal principal, @PathVariable(name = "id") Long id) {
        RequestUtils.getFirstRequestPlus(principal, id);
        ListAppIconPlusResponse listAppIconPlusResponse = appIconPlusAddSerivce.findAppIconPlusAddUpdate(principal.getIdSchoolLogin(), id);
        return DataResponse.getData(listAppIconPlusResponse, HttpStatus.OK);
    }

    @PostMapping("/plus/edit-avatar")
    public ResponseEntity uploadEditAvatar(@CurrentUser UserPrincipal principal,
                                           @RequestParam("multipartFile") MultipartFile multipartFile,
                                           @RequestParam("idEmployee") Long idEmployee,
                                           @RequestParam("fileName") String fileName) throws IOException {
        RequestUtils.getFirstRequestPlus(principal, idEmployee);
        employeeService.uploadAvatarEditEmployee(principal.getIdSchoolLogin(), multipartFile, idEmployee, fileName);
        return DataResponse.getData("Ghi file thành công", HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.PUT, value = "/plus/update/active-one")
    public ResponseEntity saveActiveOne(@CurrentUser UserPrincipal principal, @Valid @RequestBody EmployeeUpdateActiveOneRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        boolean data = infoEmployeeSchoolService.updateActiveOne(principal, request);
        String message = request.isActivated() ? MessageWebConstant.UPDATE_ACTIVE_ACCOUNT : MessageWebConstant.UPDATE_UNACTIVE_ACCOUNT;
        return NewDataResponse.setDataCustom(data, message);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/plus/update/active-one/sms-receive")
    public ResponseEntity saveActiveOneSms(@CurrentUser UserPrincipal principal, @Valid @RequestBody EmployeeUpdateActiveSMSOneRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        boolean data = infoEmployeeSchoolService.updateActiveSMSOne(principal, request);
        String message = request.isSmsReceive() ? MessageWebConstant.UPDATE_ACTIVE_SMS : MessageWebConstant.UPDATE_UNACTIVE_SMS;
        return NewDataResponse.setDataCustom(data, message);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/plus/update/active-one/sms-send")
    public ResponseEntity saveActiveOneSmsSend(@CurrentUser UserPrincipal principal, @Valid @RequestBody EmployeeUpdateActiveSMSSendOneRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        boolean data = infoEmployeeSchoolService.updateActiveSMSSendOne(principal, request);
        String message = request.isSmsSend() ? MessageWebConstant.UPDATE_ACTIVE_SEND_SMS : MessageWebConstant.UPDATE_UNACTIVE_SEND_SMS;
        return NewDataResponse.setDataCustom(data, message);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/config/function")
    public ResponseEntity getConfigFunction(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequestPlus(principal);
        SchoolInfoConfigResponse response = schoolInfoService.findSchoolInfoConfig();
        return NewDataResponse.setDataSearch(response);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/config/function")
    public ResponseEntity getConfigFunction(@CurrentUser UserPrincipal principal, @RequestBody SchoolInfoConfigResponse request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        schoolInfoService.updateSchoolInfoConfig(request);
        return NewDataResponse.setDataSave(true);
    }

}
