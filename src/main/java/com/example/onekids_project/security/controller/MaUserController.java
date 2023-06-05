package com.example.onekids_project.security.controller;

import com.example.onekids_project.common.MessageConstant;
import com.example.onekids_project.cronjobs.AttendanceKidsCronjobs;
import com.example.onekids_project.cronjobs.EvaluateKidsCronjobs;
import com.example.onekids_project.dto.TestDTO;
import com.example.onekids_project.entity.school.Grade;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.enums.DateEnum;
import com.example.onekids_project.repository.GradeRepository;
import com.example.onekids_project.repository.SchoolRepository;
import com.example.onekids_project.response.common.DataResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.security.payload.ChangePasswordRequest;
import com.example.onekids_project.security.service.servicecustom.MaUserService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MaUserController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private MaUserService maUserService;

    @Autowired
    private EvaluateKidsCronjobs evaluateKidsCronjobs;

    @Autowired
    private AttendanceKidsCronjobs attendanceKidsCronjobs;

    /**
     * change password
     *
     * @param principal
     * @param changePasswordRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/update-password")
    public ResponseEntity updatePassword(@CurrentUser UserPrincipal principal, @Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        logger.info("username: {}, fullName: {}, {}", principal.getUsername(), principal.getFullName());
        boolean checkUpdatePassword = maUserService.updatePassword(principal, changePasswordRequest);
        logger.info(MessageConstant.UPDATE_PASSWORD);
        return NewDataResponse.setDataCustom(checkUpdatePassword, MessageConstant.UPDATE_PASSWORD);
    }


    @GetMapping("/mauser")
    public String testMaUser(@CurrentUser UserPrincipal userPrincipal) {


        List<Long> longSet = new ArrayList<>();
        longSet.add(4l);
        longSet.add(3l);
        longSet.add(7l);

        return "this is test ma_user";
    }

    @GetMapping("/sqlmauser")
    public ResponseEntity getMaUser(@CurrentUser UserPrincipal userPrincipal) {

        School school = schoolRepository.findById(userPrincipal.getIdSchoolLogin()).get();
        Grade grade = gradeRepository.findById(5l).get();

//        Grade grade1= gradeRepository.save(grade);


        return DataResponse.getData(grade, HttpStatus.OK);
    }

    /**
     * test không cần đăng nhập
     *
     * @return
     */
    @GetMapping("/ok")
    public ResponseEntity testAccess() {

//        evaluateKidsCronjobs.generateEvaluateDateKids();
//        evaluateKidsCronjobs.generateEvaluateWeekKids();
//        evaluateKidsCronjobs.generateEvaluateMonthKids();

        LocalDate a = LocalDate.parse("2020-08-04");
        a = a.minusDays(1);
        int month = a.getMonthValue();
        int year = a.getYear();
        DayOfWeek dayOfWeek = a.getDayOfWeek();
        if (dayOfWeek.toString().equals(DateEnum.MONDAY.toString())) {
            System.out.println("aaa");
        }

        return DataResponse.getData("fffff", HttpStatus.OK);
    }

//    @GetMapping("/createauto")
//    public ResponseEntity createData() {
//        evaluateKidsCronjobs.generateEvaluateDateKids();
//        evaluateKidsCronjobs.generateEvaluateWeekKids();
//        evaluateKidsCronjobs.generateEvaluateMonthKids();
//
//        attendanceKidsCronjobs.generateAttendanceKids();
//        return DataResponse.getData("thanh cong", HttpStatus.OK);
//    }

    @GetMapping("/test")
    public ResponseEntity testController() {
        List<Integer> l1 = new ArrayList<>();

        l1.add(1);
        l1.add(2);
        l1.add(3);

        List<Integer> l2 = new ArrayList<>();
        l2.add(4);
        l2.add(2);
        l2.add(3);

        List<Integer> intersection = (List<Integer>) CollectionUtils.intersection(l1, l2);
        List<Integer> union = (List<Integer>) CollectionUtils.union(l1, l2);
        List<Integer> substruct = (List<Integer>) CollectionUtils.subtract(l1, l2); // list 2 - list 1

        String urlLocal = "aafd.sdfsdfsju.addf";
        String nameFile = urlLocal.substring(urlLocal.lastIndexOf(".") + 1);

        return DataResponse.getData("my test", HttpStatus.OK);

    }

    public String hhhg(TestDTO testDTO) {
        return "this is tring";
    }

    @GetMapping("/mytest")
    public ResponseEntity testAccess5(@CurrentUser UserPrincipal userPrincipal) {

        School school = schoolRepository.findById(userPrincipal.getIdSchoolLogin()).get();
        Grade grade = new Grade();
        grade.setGradeName("cap 2");
        grade.setGradeDescription("mo ta 2");
        grade.setSchool(school);
        Grade gradesave = gradeRepository.save(grade);
        return DataResponse.getData(gradesave, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/ok1")
    public String testAccess1g(@CurrentUser UserPrincipal userPrinciple) {
        return "this is ok1 get";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/ok1")
    public String testAccess1po() {
        return "this is ok1 post";
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/ok1")
    public String testAccess1pu() {
        return "this is ok1 put";
    }

    @GetMapping("/ok2")
    public String testAccess2() {
        return "this is ok2";
    }

    @GetMapping("/ok31")
    public String testAccess31() {
        return "this is ok31 get";
    }

    @GetMapping("/ok31/into")
    public String testAccess311() {
        return "this is ok31 into";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/ok31")
    public String testAccess32() {
        return "this is ok31 post";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/ok32")
    public String testAccess36() {
        return "this is ok32 post";
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/ok31")
    public String testAccess33() {
        return "this is ok33 delete";
    }

    @GetMapping("/ok4")
    public String testAccess4() {
        return "this is ok4";
    }

    @GetMapping("/nook")
    public String testAccessok() {
        return "this is no ok";
    }
}
