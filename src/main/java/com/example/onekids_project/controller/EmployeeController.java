package com.example.onekids_project.controller;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.MessageConstant;
import com.example.onekids_project.common.MessageWebConstant;
import com.example.onekids_project.dto.InfoEmployeeSchoolDTO;
import com.example.onekids_project.importexport.model.EmployeeModelImport;
import com.example.onekids_project.importexport.model.EmployeeModelImportFail;
import com.example.onekids_project.importexport.model.ListEmployeeModelImport;
import com.example.onekids_project.importexport.service.EmployeeExcelService;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.request.common.StatusCommonRequest;
import com.example.onekids_project.request.createnotifyschool.CreateEmployeeNotify;
import com.example.onekids_project.request.employee.*;
import com.example.onekids_project.request.kids.MultipartFileRequest;
import com.example.onekids_project.request.kids.SmsStudentRequest;
import com.example.onekids_project.request.smsNotify.SMSRequest;
import com.example.onekids_project.response.common.DataResponse;
import com.example.onekids_project.response.common.ErrorResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.response.department.TabDepartmentResponse;
import com.example.onekids_project.response.employee.ListEmployeeNewResponse;
import com.example.onekids_project.response.employee.ListInfoEmployeeSchoolResponse;
import com.example.onekids_project.response.employee.SubjectInClassResponse;
import com.example.onekids_project.response.employee.TabProfessionalResponse;
import com.example.onekids_project.response.excel.ExcelNewResponse;
import com.example.onekids_project.response.groupout.GroupOutNameResponse;
import com.example.onekids_project.response.school.ListAppIconPlusNotifyResponse;
import com.example.onekids_project.response.school.ListAppIconTeacherResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.*;
import com.example.onekids_project.service.servicecustom.groupout.GroupOutEmployeeService;
import com.example.onekids_project.util.RequestUtils;
import com.example.onekids_project.validate.CommonValidate;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/web/employees")
public class EmployeeController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private InfoEmployeeSchoolService infoEmployeeSchoolService;

    @Autowired
    private AppIconTeacherService appIconTeacherService;

    @Autowired
    private AppIconTeacherAddSerivce appIconTeacherAddSerivce;

    @Autowired
    private AppIconPlusService appIconPlusService;

    @Autowired
    private AppIconPlusAddSerivce appIconPlusAddSerivce;

    @Autowired
    private EmployeeExcelService employeeExcelService;

    @Autowired
    private AppIconNotifyPlusService appIconNotifyPlusService;

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private GroupOutEmployeeService groupOutEmployeeService;

    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public ResponseEntity searchEmployee(@CurrentUser UserPrincipal principal, @Valid EmployeeSearchNew search) {
        RequestUtils.getFirstRequestPlus(principal, search);
        ListEmployeeNewResponse data = infoEmployeeSchoolService.searchEmployee(principal, search);
        return NewDataResponse.setDataSearch(data);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/update/active-one")
    public ResponseEntity saveActiveOne(@CurrentUser UserPrincipal principal, @Valid @RequestBody EmployeeUpdateActiveOneRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        boolean data = infoEmployeeSchoolService.updateActiveOne(principal, request);
        String message = request.isActivated() ? MessageWebConstant.UPDATE_ACTIVE_ACCOUNT : MessageWebConstant.UPDATE_UNACTIVE_ACCOUNT;
        return NewDataResponse.setDataCustom(data, message);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/update/active-one/sms-receive")
    public ResponseEntity saveActiveOneSms(@CurrentUser UserPrincipal principal, @Valid @RequestBody EmployeeUpdateActiveSMSOneRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        boolean data = infoEmployeeSchoolService.updateActiveSMSOne(principal, request);
        String message = request.isSmsReceive() ? MessageWebConstant.UPDATE_ACTIVE_SMS : MessageWebConstant.UPDATE_UNACTIVE_SMS;
        return NewDataResponse.setDataCustom(data, message);
    }


    @RequestMapping(method = RequestMethod.PUT, value = "/update/active-many")
    public ResponseEntity saveActiveMany(@CurrentUser UserPrincipal principal, @Valid @RequestBody EmployeeUpdateActiveManyRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        boolean data = infoEmployeeSchoolService.updateActiveMany(principal, request);
        String message = request.isStatus() ? MessageWebConstant.UPDATE_ACTIVE_ACCOUNT : MessageWebConstant.UPDATE_UNACTIVE_ACCOUNT;
        return NewDataResponse.setDataCustom(data, message);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/update/active-many/sms-receive")
    public ResponseEntity saveActiveSMSMany(@CurrentUser UserPrincipal principal, @Valid @RequestBody EmployeeUpdateActiveManyRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        boolean data = infoEmployeeSchoolService.updateActiveSMSMany(principal, request);
        String message = request.isStatus() ? MessageWebConstant.UPDATE_ACTIVE_SMS : MessageWebConstant.UPDATE_UNACTIVE_SMS;
        return NewDataResponse.setDataCustom(data, message);
    }


    /**
     * Tìm kiếm nhân viên theo Id
     *
     * @param principal
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity findById(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequestPlus(principal, id);
        InfoEmployeeSchoolDTO data = employeeService.findByIdEmployee(id);
        return NewDataResponse.setDataSearch(data);
    }

    /**
     * Tìm kiếm nhân viên theo Id và idSchool
     *
     * @param principal
     * @param id
     * @return
     */
    @GetMapping("/school/{id}")
    public ResponseEntity findByIdAndIdSchool(@CurrentUser UserPrincipal principal, @PathVariable Long id, @RequestParam(name = "idSchool") Long idSchool) {
        RequestUtils.getFirstRequestPlus(principal, id);
        InfoEmployeeSchoolDTO infoEmployeeSchoolDTOOptional = employeeService.findByIdEmployee(id);
        return NewDataResponse.setDataSearch(infoEmployeeSchoolDTOOptional);
    }

    /**
     * Thêm mới nhân viên
     *
     * @param request
     * @return
     */
    @PostMapping
    public ResponseEntity createEmployee(@CurrentUser UserPrincipal principal, @Valid @RequestBody CreateEmployeeRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        boolean checkCreate = employeeService.createEmployee(request, principal);
        return NewDataResponse.setDataCustom(checkCreate, MessageWebConstant.CREATE_EMPLOYEE);
    }

    @PostMapping("/insert-avatar")
    public ResponseEntity uploadAvatar(@CurrentUser UserPrincipal principal, @RequestParam("multipartFile") MultipartFile multipartFile) throws IOException {
        RequestUtils.getFirstRequestPlus(principal, multipartFile);
        employeeService.uploadAvatarEmployee(principal.getIdSchoolLogin(), multipartFile);
        return NewDataResponse.setDataCreate(null);

    }

    @PostMapping("/edit-avatar")
    public ResponseEntity uploadEditAvatar(@CurrentUser UserPrincipal principal,
                                           @RequestParam("multipartFile") MultipartFile multipartFile,
                                           @RequestParam("idEmployee") Long idEmployee,
                                           @RequestParam("fileName") String fileName) throws IOException {
        RequestUtils.getFirstRequestPlus(principal, idEmployee);
        employeeService.uploadAvatarEditEmployee(principal.getIdSchoolLogin(), multipartFile, idEmployee, fileName);
        return NewDataResponse.setDataCreate(null);
    }

    @PostMapping("/test")
    public ResponseEntity createEmployeeTest(@RequestParam("multipartFile") MultipartFile multipartFile) {
        try {
            return DataResponse.getData("createEmployeeResponse", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception thêm mới nhân viên: " + e.getMessage());
            return ErrorResponse.errorData(e.getMessage(), "Lỗi thêm mới nhân viên", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Sửa nhân viên
     *
     * @param updateEmployeeRequest
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity updateEmployee(@PathVariable(name = "id") Long id, @CurrentUser UserPrincipal principal, @Valid @RequestBody UpdateEmployeeRequest updateEmployeeRequest) {
        RequestUtils.getFirstRequestPlus(principal, updateEmployeeRequest);
        boolean data = employeeService.updateEmployee(principal, id, updateEmployeeRequest);
        return NewDataResponse.setDataCustom(data, MessageWebConstant.UPDATE_EMPLOYEE);
    }

    /**
     * Xóa nhân viên theo id
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity deleteEmployee(@PathVariable(name = "id") Long id, @CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequestPlus(principal, id);
        boolean check = employeeService.deleteEmployee(principal, id);
        return NewDataResponse.setDataDelete(check);
    }

    /**
     * bỏ xóa nhiều nhân sự
     *
     * @param ids
     * @param principal
     * @return
     */
    @PutMapping("/delete-multi")
    public ResponseEntity deleteMultiEmployee(@RequestBody Long[] ids, @CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequestPlus(principal, ids);
        boolean checkDelete = employeeService.deleteMultiEmployee(principal, ids);
        return NewDataResponse.setDataCustom(checkDelete, MessageWebConstant.DELETE_EMPLOYEE);

    }


    @GetMapping("/tab-department/{idEmployee}")
    public ResponseEntity findTabDepartmentInEmployee(@CurrentUser UserPrincipal principal, @PathVariable("idEmployee") Long idEmployee) {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        List<TabDepartmentResponse> tabDepartmentResponseList = employeeService.getTabDepartmentInEmployee(idSchoolLogin, idEmployee);
        return NewDataResponse.setDataSearch(tabDepartmentResponseList);
    }


    @GetMapping("/tab-professional/{idEmployee}")
    public ResponseEntity findTabProfressionalInEmployee(@CurrentUser UserPrincipal principal, @PathVariable("idEmployee") Long idEmployee) {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        List<TabProfessionalResponse> tabProfessionalResponseList = employeeService.getTabProfessionalInEmployee(idSchoolLogin, idEmployee);
        return NewDataResponse.setDataSearch(tabProfessionalResponseList);
    }

    /**
     * Lấy môn học trong class
     *
     * @return
     */
    @GetMapping("/subject-in-class/{idClass}")
    public ResponseEntity findSubjectInClass(@CurrentUser UserPrincipal principal, @PathVariable("idClass") Long idClass) {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        List<SubjectInClassResponse> subjectInClassResponseList = employeeService.getSubjectInClass(idClass, idSchoolLogin);
        return NewDataResponse.setDataSearch(subjectInClassResponseList);
    }

    /**
     * tìm kiếm icon Teacher cho trường học tạo mới
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/icon-teacher-create")
    public ResponseEntity getIconTeacherCreate(@CurrentUser UserPrincipal principal) {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        ListAppIconTeacherResponse listAppIconTeacherResponse = appIconTeacherAddSerivce.findAppIconTeacherAddCreate(idSchoolLogin);
        return NewDataResponse.setDataSearch(listAppIconTeacherResponse);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/icon-teacher-update/{id}")
    public ResponseEntity getIconTeacherUpdate(@CurrentUser UserPrincipal principal, @PathVariable(name = "id") Long id) {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        ListAppIconTeacherResponse listAppIconTeacherResponse = appIconTeacherAddSerivce.findAppIconTeacherAddUpdate(idSchoolLogin, id);
        return NewDataResponse.setDataSearch(listAppIconTeacherResponse);
    }

    /**
     * get tab phòng ban
     */
    @GetMapping("/tab-department")
    public ResponseEntity findTabDepartmentInEmployee(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequestPlus(principal);
        List<TabDepartmentResponse> tabDepartmentResponseList = employeeService.getTabDepartmentInEmployee(principal.getIdSchoolLogin(), null);
        return NewDataResponse.setDataSearch(tabDepartmentResponseList);
    }

    /**
     * tab chuyển môn
     *
     * @param principal
     * @return
     */
    @GetMapping("/tab-professional")
    public ResponseEntity findTabProfressionalInEmployee(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequestPlus(principal);
        List<TabProfessionalResponse> tabProfessionalResponseList = employeeService.getTabProfessionalInEmployee(principal.getIdSchoolLogin(), null);
        return NewDataResponse.setDataSearch(tabProfessionalResponseList);
    }

    /**
     * tìm kiếm icon Notify cho Plus khi trường học tạo mới
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/icon-plus-notify-create")
    public ResponseEntity getIconPlusNotifyCreate(@CurrentUser UserPrincipal principal) {
        logger.info("username: {}, fullName: {}", principal.getUsername(), principal.getFullName());
        ListAppIconPlusNotifyResponse listAppIconPlusNotifyResponse = appIconPlusService.getAppIconPlusNotify(principal.getIdSchoolLogin());
        return NewDataResponse.setDataCreate(listAppIconPlusNotifyResponse);

    }

    @RequestMapping(method = RequestMethod.GET, value = "/icon-plus-notify-update/{id}")
    public ResponseEntity getIconNotifyPlusUpdate(@CurrentUser UserPrincipal principal, @PathVariable(name = "id") Long id) {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        ListAppIconPlusNotifyResponse listAppIconPlusNotifyResponse = appIconNotifyPlusService.findAppIconNotifyPlusAdd(idSchoolLogin, id);
        return NewDataResponse.setDataSearch(listAppIconPlusNotifyResponse);
    }

//    @GetMapping("/export-excel/employee")
//    public ResponseEntity excelCustomrsReport(@CurrentUser UserPrincipal principal, SearchExportEmployeeRequest searchExportEmployeeRequest) {
//        try {
//            Long idSchoolLogin = principal.getIdSchoolLogin();
//            if (idSchoolLogin == null || idSchoolLogin <= 0) {
//                logger.error(AppConstant.ID_SCHOOL_LOGIN_ERROR);
//                return ErrorResponse.errorData(AppConstant.ID_SCHOOL_LOGIN_ERROR, AppConstant.ID_SCHOOL_LOGIN_CONTENT, HttpStatus.NOT_ACCEPTABLE);
//            }
//            if (searchExportEmployeeRequest.getIdDepartment() != null || searchExportEmployeeRequest.getList() != null) {
//                logger.info("Thành công");
//                ListInfoEmployeeSchoolResponse listInfoEmployeeSchoolResponse = infoEmployeeSchoolService.findAllInfoEmployee(idSchoolLogin, searchExportEmployeeRequest);
//                //getnameschool
//                SchoolResponse schoolResponse = schoolService.findByIdSchool(principal.getIdSchoolLogin()).stream().findFirst().orElse(null);
//                String nameSchool = schoolResponse.getSchoolName();
//                if (listInfoEmployeeSchoolResponse == null) {
//                    logger.error("lỗi tìm kiếm nhân viên");
//                    return ErrorResponse.errorData("Không thể tìm kiếm nhân viên", "Không thể tìm kiếm nhân viên", HttpStatus.INTERNAL_SERVER_ERROR);
//                }
//                List<EmployeeModel> listVM = infoEmployeeSchoolService.getFileAllEmployeeByDepartment(listInfoEmployeeSchoolResponse, nameSchool);
//                ByteArrayInputStream in = null;
//                try {
//                    in = employeeExcelService.customersToExcel(listVM, nameSchool);
////                    in = ExcelHelper.employeeToExcel(listVM);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                logger.info("Tìm kiếm nhân viên thành công");
//                return ResponseEntity.ok().body(new InputStreamResource(in));  // chưa trả được về kiểu DataReponse Custom
//            } else {
//                logger.info("Thất bại");
//                return ErrorResponse.errorData("không có dữ liệu truyền vào", "không có dữ liệu truyền vào", HttpStatus.INTERNAL_SERVER_ERROR);
//            }
//
//        } catch (Exception e) {
//            logger.error("Exception Tìm kiếm nhân viên không thành công: " + e.getMessage());
//            return ErrorResponse.errorData(e.getMessage(), "Tìm kiếm nhân viên không thành công", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @GetMapping("/export-excel/employee-new")
    public ResponseEntity excelCustomsReportNew(@CurrentUser UserPrincipal principal, SearchExportEmployeeRequest searchExportEmployeeRequest) {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        ListInfoEmployeeSchoolResponse listInfoEmployeeSchoolResponse = infoEmployeeSchoolService.findAllInfoEmployee(idSchoolLogin, searchExportEmployeeRequest);
        List<ExcelNewResponse> listVM = infoEmployeeSchoolService.getFileAllEmployeeByDepartmentNew(listInfoEmployeeSchoolResponse, principal);
        return NewDataResponse.setDataSearch(listVM);
    }


    /**
     * create employee notify
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/employee-notify")
    public ResponseEntity createNotifyEmployee(@CurrentUser UserPrincipal principal, CreateEmployeeNotify request) throws IOException, FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal, request);
        boolean checkCreate = employeeService.createEmployeeNotify(principal, request);
        return NewDataResponse.setDataCreate(checkCreate);
    }

    /**
     * gửi sms nhân sự
     *
     * @param principal
     * @param request
     * @return
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @RequestMapping(method = RequestMethod.POST, value = "/employee-sms")
    public ResponseEntity createNotifyEmployeeSmsNew(@CurrentUser UserPrincipal principal, @Valid @RequestBody SMSRequest request) throws IOException, ExecutionException, InterruptedException {
        RequestUtils.getFirstRequest(principal, request);
        boolean checkCreate = employeeService.sendSmsInfoEmployee(principal, request);
        return NewDataResponse.setDataCreate(checkCreate);
    }

    /**
     * gửi sms kiểu mới
     *
     * @param principal
     * @param request
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @RequestMapping(method = RequestMethod.POST, value = "/sms")
    public ResponseEntity createEmployeeSms(@CurrentUser UserPrincipal principal, @Valid @RequestBody SmsStudentRequest request) throws ExecutionException, InterruptedException {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = employeeService.createEmployeeSms(principal, request);
        return NewDataResponse.setDataCustom(check, MessageConstant.SMS_SEND);
    }

    /**
     * send account employee Sms
     *
     * @param principal
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/sms/account")
    public ResponseEntity sendAccountEmployeeSms(@CurrentUser UserPrincipal principal, @NotEmpty @RequestBody List<Long> idEmployeeList) throws ExecutionException, InterruptedException {
        RequestUtils.getFirstRequest(principal, idEmployeeList);
        CommonValidate.checkDataPlus(principal);
        boolean checkCreate = employeeService.sendAccountEmployeeSms(principal, idEmployeeList);
        return NewDataResponse.setDataCustom(checkCreate, MessageConstant.SMS_ACCOUNT);
    }


    /**
     * importExcelEmployee
     *
     * @param principal
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/import-excel-employee")
    public ResponseEntity importExcelKids(@CurrentUser UserPrincipal principal, @NotNull @ModelAttribute MultipartFileRequest multipartFileRequest) throws IOException {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        if (idSchoolLogin == null || idSchoolLogin <= 0) {
            logger.error(AppConstant.ID_SCHOOL_LOGIN_ERROR);
            return ErrorResponse.errorData(AppConstant.ID_SCHOOL_LOGIN_ERROR, AppConstant.ID_SCHOOL_LOGIN_CONTENT, HttpStatus.NOT_ACCEPTABLE);
        }
        ListEmployeeModelImport employeeModels = employeeExcelService.importExcelEmployee(principal, multipartFileRequest.getMultipartFile());

        List<EmployeeModelImport> employeeModelImportList = employeeService.saveDataEmployeeExcel(employeeModels.getEmployeeModelImportList(), principal);
        List<EmployeeModelImportFail> employeeModelImportFails = listMapper.mapList(employeeModelImportList, EmployeeModelImportFail.class);
        employeeModelImportFails.addAll(employeeModels.getEmployeeModelImportFailList().stream().filter(x -> x.getBirthday() != null).collect(Collectors.toList()));
        if (CollectionUtils.isNotEmpty(employeeModelImportFails)) {
            ByteArrayInputStream in = null;
            in = employeeExcelService.customEmployeeImportFailExcel(employeeModels.getEmployeeModelImportFailList(), principal.getSchool().getSchoolName());
            logger.info("Lỗi tạo dữ liệu nhân viên từ file excel");
            return ResponseEntity.ok().body(new InputStreamResource(in));
        }

        logger.info("Tạo dữ liệu nhân viên từ file excel thành công");
        return NewDataResponse.setDataCreate(MessageConstant.CREATE_EMPLOYEE_EXCEL);
    }

    /**
     * importExcelEmployee new
     *
     * @param principal
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/import-excel-employee-new")
    public ResponseEntity importExcelNewKids(@CurrentUser UserPrincipal principal, @Valid @RequestBody CreateEmployeeExcelRequest request){
        RequestUtils.getFirstRequest(principal, request);
        employeeExcelService.importExcelNewEmployee(principal, request);
        return NewDataResponse.setDataCreate(MessageConstant.CREATE_EMPLOYEE_EXCEL);
    }

    /**
     * cập nhật trạng thái nhân sự
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/employee-status")
    public ResponseEntity updateStatusEmployee(@CurrentUser UserPrincipal principal, @Valid @RequestBody StatusCommonRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = employeeService.updateStatusForEmployee(principal, request);
        return NewDataResponse.setDataCustom(check, MessageWebConstant.UPDATE_STATUS);
    }

    /**
     * tìm kiếm danh sách thư mục trong trường nhân sự
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/employee-group-out/group-name")
    public ResponseEntity searchGroupOutNameEmployee(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        List<GroupOutNameResponse> data = groupOutEmployeeService.findAllGroupNameEmployee(principal);
        return NewDataResponse.setDataSearch(data);
    }

    /**
     * Cập nhật trạng thái ra trường của nhân sự
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/employee-group-out")
    public ResponseEntity updateKidsGroupOut(@CurrentUser UserPrincipal principal, @Valid @RequestBody EmployeeGroupOutRequest request) {
        RequestUtils.getFirstRequest(principal);
        boolean check = employeeService.updateEmployeeGroupOut(principal, request);
        return NewDataResponse.setDataUpdate(check);
    }
}
