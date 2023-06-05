package com.example.onekids_project.mobile.plus.controller;

import com.example.onekids_project.mobile.plus.request.student.GroupPlusRequest;
import com.example.onekids_project.mobile.plus.response.student.*;
import com.example.onekids_project.mobile.plus.service.servicecustom.StudentPlusService;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/mob/plus/student")
public class StudentPlusController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    StudentPlusService studentPlusService;

    /**
     * thống kê dữ liệu học sinh ở năm hiện tại
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/statistic-year")
    public ResponseEntity searchStudentYear(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequestPlus(principal);
        StudentYearPlusResponse response = studentPlusService.searchStudentYear(principal);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * thống kê dữ liệu trong các khối
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/statistic-grade")
    public ResponseEntity searchGrade(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequestPlus(principal);
        List<GradePlusResponse> responseList = studentPlusService.searchGrade(principal);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * thống kê dữ liệu trong các lớp
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/statistic-class")
    public ResponseEntity searchClass(@CurrentUser UserPrincipal principal, @RequestParam Long idGrade) {
        RequestUtils.getFirstRequestPlus(principal, idGrade);
        List<ClassPlusResponse> responseList = studentPlusService.searchClass(principal, idGrade);
        return NewDataResponse.setDataSearch(responseList);

    }

    /**
     * thống kê dữ liệu trong các lớp
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/statistic-kid")
    public ResponseEntity searchKid(@CurrentUser UserPrincipal principal, @RequestParam Long idClass) {
        RequestUtils.getFirstRequestPlus(principal, idClass);
        List<KidPlusResponse> responseList = studentPlusService.searchKid(principal, idClass);
        return NewDataResponse.setDataSearch(responseList);

    }

    /**
     * thống kê dữ liệu trong các lớp
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/detail-kid")
    public ResponseEntity searchDeatailKid(@CurrentUser UserPrincipal principal, @RequestParam Long idKid) {
        RequestUtils.getFirstRequestPlus(principal, idKid);
        InfoKidResponse responseList = studentPlusService.searchDeatailKid(principal, idKid);
        return NewDataResponse.setDataSearch(responseList);

    }

    /**
     * thống kê nhóm
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/statistic-group")
    public ResponseEntity searchGroup(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequestPlus(principal);
        List<GroupPlusResponse> responseList = studentPlusService.searchGroup(principal);
        return NewDataResponse.setDataSearch(responseList);

    }

    /**
     * thống kê dữ liệu học sinh trong nhóm: đang đi học
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/statistic-kid-group")
    public ResponseEntity searchKidGroup(@CurrentUser UserPrincipal principal, @Valid GroupPlusRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        List<FeatureKidPlusResponse> responseList = studentPlusService.searchKidGroup(principal, request);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * thống kê dữ liệu học sinh chờ đi học
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/statistic-kid-wait")
    public ResponseEntity searchKidWait(@CurrentUser UserPrincipal principal, Long idClass) {
        RequestUtils.getFirstRequestPlus(principal, idClass);
        List<FeatureKidPlusResponse> responseList = studentPlusService.searchKidWait(principal, idClass);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * thống kê dữ liệu học sinh bảo lưu
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/statistic-kid-reserve")
    public ResponseEntity searchKidReserve(@CurrentUser UserPrincipal principal, @Valid Long idClass) {
        RequestUtils.getFirstRequestPlus(principal, idClass);
        List<FeatureKidPlusResponse> responseList = studentPlusService.searchKidReserve(principal, idClass);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * thống kê dữ liệu học sinh nghỉ học
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/statistic-kid-off")
    public ResponseEntity searchKidOff(@CurrentUser UserPrincipal principal, @Valid Long idClass) {
        RequestUtils.getFirstRequestPlus(principal, idClass);
        List<FeatureKidPlusResponse> responseList = studentPlusService.searchKidOff(principal, idClass);
        return NewDataResponse.setDataSearch(responseList);
    }

}
