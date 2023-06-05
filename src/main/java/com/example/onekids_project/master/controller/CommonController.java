package com.example.onekids_project.master.controller;


import com.example.onekids_project.common.MessageWebConstant;
import com.example.onekids_project.cronjobs.AttendanceKidsCronjobs;
import com.example.onekids_project.cronjobs.EvaluateKidsCronjobs;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.kids.KidsClassDate;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.mobile.request.DateNotNullRequest;
import com.example.onekids_project.repository.KidsClassDateRepository;
import com.example.onekids_project.repository.KidsRepository;
import com.example.onekids_project.repository.SchoolRepository;
import com.example.onekids_project.response.common.DataResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.security.payload.AdminDataRequest;
import com.example.onekids_project.security.service.servicecustom.MaUserService;
import com.example.onekids_project.service.servicecustom.SupperAdminService;
import com.example.onekids_project.service.servicecustom.SysInforService;
import com.example.onekids_project.service.servicecustom.common.CommonService;
import com.example.onekids_project.util.CommonUtil;
import com.example.onekids_project.validate.CommonValidate;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/web/common")
public class CommonController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MaUserService maUserService;

    @Autowired
    private SupperAdminService supperAdminService;

    @Autowired
    private SysInforService sysInforService;

    @Autowired
    private EvaluateKidsCronjobs evaluateKidsCronjobs;

    @Autowired
    private AttendanceKidsCronjobs attendanceKidsCronjobs;

    @Autowired
    private CommonService commonService;

    @Autowired
    private KidsRepository kidsRepository;

    @Autowired
    private KidsClassDateRepository kidsClassDateRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    /**
     * tạo tài khoản supperadmin
     *
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/create/account")
    public ResponseEntity createAccountSupperAdmin(@Valid @RequestBody AdminDataRequest request) {
        logger.info("create account supper system {}", request);
        boolean checkCreate = supperAdminService.createSupperAdmin(request);
        return NewDataResponse.setDataCustom(checkCreate, MessageWebConstant.CREATE_ACCOUNT_SYSTEM);
    }

    /**
     * tạo các dữ liệu chung cho hệ thống
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/create/common")
    public ResponseEntity createSysInfo() {
        logger.info("create data common for system");
        boolean check = sysInforService.createCommonALl();
        return NewDataResponse.setDataCustom(check, MessageWebConstant.CREATE_DATA_SYSTEM);
    }

    /**
     * create folder system
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/create/folder-system")
    public ResponseEntity createFolderForSystem() {
        logger.info("create data common for system");
        CommonUtil.createFolderSystem();
        return NewDataResponse.setDataCustom(true, MessageWebConstant.CREATE_FOLDER_SYSTEM);
    }

    /**
     * create folder system
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/create/folder-allschool")
    public ResponseEntity createFolderForSchool() {
        logger.info("start creat folder for all school");
        List<School> schoolList = schoolRepository.findAllByDelActiveTrue();
        schoolList.forEach(x -> CommonUtil.createFolderSchool(x.getId()));
        return DataResponse.getData("create folder for all school success", HttpStatus.OK);
    }

    /**
     * create folder system
     *
     * @return
     */
//    @RequestMapping(method = RequestMethod.POST, value = "/create/evaluate-attendance")
//    public ResponseEntity createEvaluateAndAttendanceAuto() {
//        try {
//            evaluateKidsCronjobs.generateEvaluateDateKids();
//            evaluateKidsCronjobs.generateEvaluateWeekKids();
//            evaluateKidsCronjobs.generateEvaluateMonthKids();
//            attendanceKidsCronjobs.generateAttendanceKids();
//            logger.info("Tạo evaluate-attendance thành công");
//            return DataResponse.getData("Tạo evaluate-attendance thành công", HttpStatus.OK);
//        } catch (Exception e) {
//            logger.error("Exception Lỗi tạo evaluate-attendance" + e.getMessage());
//            return ErrorResponse.errorData(e.getMessage(), "Exception Lỗi evaluate-attendance system", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
    @RequestMapping(method = RequestMethod.POST, value = "/create/change-class")
    public ResponseEntity createChaneClass() {
        List<Kids> kidsList = kidsRepository.findAll();
        kidsList.forEach(x -> {
            KidsClassDate kidsClassDate = new KidsClassDate();
            kidsClassDate.setKids(x);
            kidsClassDate.setMaClass(x.getMaClass());
            kidsClassDate.setFromDate(x.getDateStart());
            kidsClassDateRepository.save(kidsClassDate);
        });
        logger.info("Tạo changeClass thanh cong");
        return DataResponse.getData("Tạo changeClass thanh cong", HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/create/attendance-evaluate")
    public ResponseEntity createAttendanceManual(@CurrentUser UserPrincipal principal, @RequestParam(required = false) Long idSchool, @Valid @RequestBody DateNotNullRequest request) {
        CommonValidate.checkDataSystem(principal);
        logger.info("create attendance - evaluate date: {} - {} ", idSchool, request.getDate());
        attendanceKidsCronjobs.generateAttendanceManual(request.getDate(), idSchool);
        evaluateKidsCronjobs.generateEvaluateDateManual(request.getDate(), idSchool);
        return NewDataResponse.setDataCustom(true, "Tạo điểm danh, nhận xét thành công");
    }
}
