package com.example.onekids_project.mobile.teacher.controller;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.MessageConstant;
import com.example.onekids_project.mobile.parent.request.finance.OrderKidsParentRequest;
import com.example.onekids_project.mobile.parent.response.finance.order.OrderKidsParentResponse;
import com.example.onekids_project.mobile.request.DateNotNullRequest;
import com.example.onekids_project.mobile.teacher.response.absentletter.NumberSalaryTeacherResponse;
import com.example.onekids_project.mobile.teacher.response.salary.ListAttendanceTeacherResponse;
import com.example.onekids_project.mobile.teacher.service.servicecustom.SalaryTeacherService;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.RequestUtils;
import com.example.onekids_project.validate.CommonValidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * date 2021-04-20 09:06
 *
 * @author lavanviet
 */
@RestController
@RequestMapping("/mob/teacher/salary")
public class SalaryTeacherController {
    @Autowired
    private SalaryTeacherService salaryTeacherService;

    /**
     * tổng hợp điểm danh của nhân sự
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/attendance")
    public ResponseEntity findQualityKidOfClass(@CurrentUser UserPrincipal principal, @Valid DateNotNullRequest request) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataTeacherNoClass(principal);
        ListAttendanceTeacherResponse listQualityKidTeacherResponse = salaryTeacherService.getAttendanceTeacher(principal, request.getDate(), null);
        return NewDataResponse.setDataSearch(listQualityKidTeacherResponse);
    }

    /**
     * lấy hóa đơn theo năm của nhân sự
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/order")
    public ResponseEntity searchOrderKids(@CurrentUser UserPrincipal principal, @Valid OrderKidsParentRequest request) {
        RequestUtils.getFirstRequestParent(principal, request);
        CommonValidate.checkDataTeacherNoClass(principal);
        List<OrderKidsParentResponse> responseList = salaryTeacherService.searchOrderTeacher(principal, request, null);
        String message = CollectionUtils.isEmpty(responseList) ? MessageConstant.ORDER_EMPTY : AppConstant.SUCCESS_SEARCH;
        return NewDataResponse.setDataCustom(responseList, message);
    }


    /**
     * lấy số thông báo ở mục công lương
     * - số đơn xin nghỉ giáo viên chưa đọc
     * - số hóa đơn chưa hoàn thành trong năm hiện tại
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/show/number")
    public ResponseEntity getSalaryNumber(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataTeacher(principal);
        NumberSalaryTeacherResponse response = salaryTeacherService.showNumberSalary(principal);
        return NewDataResponse.setDataSearch(response);
    }

}
