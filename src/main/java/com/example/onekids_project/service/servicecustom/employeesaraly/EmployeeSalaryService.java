package com.example.onekids_project.service.servicecustom.employeesaraly;

import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.request.base.IdListRequest;
import com.example.onekids_project.request.base.IdObjectRequest;
import com.example.onekids_project.request.common.StatusListRequest;
import com.example.onekids_project.request.common.YearIdRequest;
import com.example.onekids_project.request.employeeSalary.*;
import com.example.onekids_project.request.finance.GenerateOrderKidsRequest;
import com.example.onekids_project.request.finance.approved.IdAndDateNotNullRequest;
import com.example.onekids_project.request.finance.order.OrderRequest;
import com.example.onekids_project.response.employeesalary.*;
import com.example.onekids_project.response.finance.order.OrderPrintResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.google.firebase.messaging.FirebaseMessagingException;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface EmployeeSalaryService {
    boolean createEmployeeSalaryDefault(UserPrincipal principal, Long idInfoEmployee, EmployeeSalaryDefaultCreateRequest request);

    List<EmployeeSalarySettingResponse> searchEmployeeSalarySetting(UserPrincipal principal, EmployeeSalaryApplySearchRequest request);

    List<EmployeeSalaryResponse> searchEmployeeSalaryApproved(UserPrincipal principal, EmployeeSalaryApplySearchRequest request);
    List<EmployeeSalaryResponse> searchEmployeeSalaryApprovedDetail(UserPrincipal principal, IdAndDateNotNullRequest request);

    boolean updateEmployeeSalary(UserPrincipal principal, EmployeeSalaryUpdateRequest request);

    boolean deleteEmployeeSalaryDefault(UserPrincipal principal, Long id);

    boolean deleteEmployeeSalaryDefaultMany(UserPrincipal principal, List<Long> request);

    List<EmployeeStatisticalDefaultResponse> statisticalDefault(UserPrincipal principal, IdObjectRequest request);

    int generateEmployeeSalaryCommon(InfoEmployeeSchool infoEmployeeSchool, int month, int year, Long idUser, String generateTy);

    int generateEmployeeSalaryFromDefault(UserPrincipal principal, GenerateEmployeeSalaryFromDefaultRequest request);

    boolean generateEmployeeSalaryFromSchool(UserPrincipal principal, GenerateEmployeeSalaryFromSchoolRequest request);

    int generateEmployeeSalaryDefault(UserPrincipal principal, GenerateEmployeeSalaryDefaultRequest request);

    List<EmployeeStatisticalApplyResponse> statisticalApply(UserPrincipal principal, EmployeeSalaryApplyRequest request);

    boolean deleteEmployeeSalaryApply(UserPrincipal principal, Long id);

    boolean deleteEmployeeSalaryApplyMany(UserPrincipal principal, List<Long> request);

    boolean updateEmployeeSalaryApply(UserPrincipal principal, EmployeeSalaryApplyUpdateRequest request);

    ActiveSalaryResponse activeSalaryDefault(UserPrincipal principal, Long id);

    FnEmployeeSalaryDefaultResponse searchSalaryDefault(UserPrincipal principal, Long id);

    EmployeeStatisticalApplyResponse searchSalaryApply(UserPrincipal principal, Long id);

    boolean createEmployeeSalaryCustom(UserPrincipal principal, Long idInfoEmployee, EmployeeSalaryApplyCreateRequest request);

    List<SalarySampleResponse> searchSalarySample(UserPrincipal principal, String name);

    List<FnSalarySampleBriefResponse> searchSalarySampleBrief(UserPrincipal principal);

    boolean createSalarySample(UserPrincipal principal, SalarySampleCreateRequest request);

    boolean updateSalarySample(UserPrincipal principal, SalarySampleUpdateRequest request);

    boolean deleteSalarySample(UserPrincipal principal, Long id);

    SalarySampleResponse detailSalarySample(UserPrincipal principal, Long id);

    boolean approvedSalary(UserPrincipal principal, IdListRequest request);

    boolean approvedOneSalary(UserPrincipal principal, Long id);

    boolean lockedOneSalary(UserPrincipal principal, Long id);

    boolean approvedAllSalary(UserPrincipal principal, SalaryStatusRequest request);

    boolean lockedAllSalary(UserPrincipal principal, SalaryStatusRequest request);

    boolean numberUser(UserPrincipal principal, SalaryNumberUserRequest request);

    List<EmployeeSalaryPaidResponse> searchOrderEmployee(UserPrincipal principal, EmployeeSalaryApplySearchRequest request);

    List<EmployeeSalaryDetailYearResponse> searchOrderMonth(UserPrincipal principal, YearIdRequest request);

    boolean generateBill(UserPrincipal principal, SalaryMultiRequest request);
    boolean sendNotifyOrder(UserPrincipal principal, SalaryMultiRequest request) throws FirebaseMessagingException, ExecutionException, InterruptedException;

    void generateBillAuto(List<InfoEmployeeSchool> infoEmployeeSchoolList);

    boolean viewBill(UserPrincipal principal, StatusListRequest request) throws ExecutionException, FirebaseMessagingException, InterruptedException;

    boolean lockedBill(UserPrincipal principal, StatusListRequest request);

    FnEmployeeSalaryResponse searchOrderPayment(UserPrincipal principal, Long idOrder, BillPaidRequest request);

    OrderPrintResponse getPrintOrder(UserPrincipal principal, BillPaidRequest request, IdListRequest idList);

    OrderPrintResponse getPrintOrderIn(UserPrincipal principal, BillPaidRequest request, IdListRequest idList);

    OrderPrintResponse getPrintOrderOut(UserPrincipal principal, BillPaidRequest request, IdListRequest idList);

    boolean orderSalaryPayment(UserPrincipal principal, Long idOrder, OrderSalaryPaymentRequest request) throws ExecutionException, FirebaseMessagingException, InterruptedException;

    boolean saveUseNumberMany(UserPrincipal principal, List<SaveNumberManyEmployeeRequest> requestList);

    boolean saveTransferNumberMany(UserPrincipal principal, List<TransferNumberManyEmployeeRequest> requestList);
}
