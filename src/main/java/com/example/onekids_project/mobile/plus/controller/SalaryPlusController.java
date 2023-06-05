package com.example.onekids_project.mobile.plus.controller;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.MessageConstant;
import com.example.onekids_project.mobile.parent.request.finance.OrderKidsParentRequest;
import com.example.onekids_project.mobile.parent.response.finance.order.OrderKidsParentResponse;
import com.example.onekids_project.mobile.plus.response.salary.EmployeeSalaryPlusResponse;
import com.example.onekids_project.mobile.plus.response.salary.NumberSalaryPlusResponse;
import com.example.onekids_project.mobile.plus.service.servicecustom.SalaryPlusService;
import com.example.onekids_project.mobile.request.DateNotNullRequest;
import com.example.onekids_project.mobile.teacher.response.salary.ListAttendanceTeacherResponse;
import com.example.onekids_project.mobile.teacher.service.servicecustom.SalaryTeacherService;
import com.example.onekids_project.request.common.StatusRequest;
import com.example.onekids_project.request.finance.statistical.FinanceKidsStatisticalRequest;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.response.finance.statistical.FinanceKidsStatisticalResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.employeesaraly.FinanceStatisticalEmployeeService;
import com.example.onekids_project.util.RequestUtils;
import com.example.onekids_project.validate.CommonValidate;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * date 2021-06-08 14:37
 *
 * @author lavanviet
 */
@RestController
@RequestMapping("/mob/plus/salary")
public class SalaryPlusController {

    @Autowired
    private SalaryPlusService salaryPlusService;
    @Autowired
    private FinanceStatisticalEmployeeService financeStatisticalEmployeeService;
    @Autowired
    private SalaryTeacherService salaryTeacherService;

    @RequestMapping(method = RequestMethod.GET, value = "/employee")
    public ResponseEntity getFeesClass(@CurrentUser UserPrincipal principal, @Valid DateNotNullRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        List<EmployeeSalaryPlusResponse> responseList = salaryPlusService.getSalaryEmployee(principal, request.getDate());
        return NewDataResponse.setDataSearch(responseList);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/order/view")
    public ResponseEntity setOrderShow(@CurrentUser UserPrincipal principal, @RequestBody List<StatusRequest> request) throws ExecutionException, InterruptedException, FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        salaryPlusService.setOrderShow(principal, request);
        return NewDataResponse.setDataCustom(AppConstant.APP_TRUE, "Cập nhật hiện thị hóa đơn thành công");
    }

    @RequestMapping(method = RequestMethod.GET, value = "/statistical")
    public ResponseEntity statisticalSalary(@CurrentUser UserPrincipal principal, @Valid FinanceKidsStatisticalRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        FinanceKidsStatisticalResponse response = financeStatisticalEmployeeService.statisticalFinanceEmployee(principal, request);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * tổng hợp điểm danh của nhân sự
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/attendance/{idEmployee}")
    public ResponseEntity findQualityKidOfClass(@CurrentUser UserPrincipal principal, @PathVariable Long idEmployee, @Valid DateNotNullRequest request) {
        RequestUtils.getFirstRequestExtend(principal, idEmployee, request);
        CommonValidate.checkDataPlus(principal);
        ListAttendanceTeacherResponse listQualityKidTeacherResponse = salaryTeacherService.getAttendanceTeacher(principal, request.getDate(), idEmployee);
        return NewDataResponse.setDataSearch(listQualityKidTeacherResponse);
    }

    /**
     * lấy hóa đơn theo năm của nhân sự
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/order/{idEmployee}")
    public ResponseEntity searchOrderKids(@CurrentUser UserPrincipal principal, @PathVariable Long idEmployee, @Valid OrderKidsParentRequest request) {
        RequestUtils.getFirstRequestExtend(principal, idEmployee, request);
        CommonValidate.checkDataPlus(principal);
        List<OrderKidsParentResponse> responseList = salaryTeacherService.searchOrderTeacher(principal, request, idEmployee);
        String message = CollectionUtils.isEmpty(responseList) ? MessageConstant.ORDER_EMPTY : AppConstant.SUCCESS_SEARCH;
        return NewDataResponse.setDataCustom(responseList, message);
    }

    /**
     * lấy số thông báo ở mục công lương
     * - số đơn giáo viên xin nghỉ chưa xác nhận
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/show/number")
    public ResponseEntity searchOrderKids(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataPlus(principal);
        NumberSalaryPlusResponse response = salaryTeacherService.showNumberPlus(principal);
        return NewDataResponse.setDataSearch(response);
    }

}
