package com.example.onekids_project.controller.finance;

import com.example.onekids_project.common.MessageConstant;
import com.example.onekids_project.common.MessageWebConstant;
import com.example.onekids_project.request.base.IdListRequest;
import com.example.onekids_project.request.base.IdObjectRequest;
import com.example.onekids_project.request.common.DescriptionRequest;
import com.example.onekids_project.request.common.StatusListRequest;
import com.example.onekids_project.request.common.YearIdRequest;
import com.example.onekids_project.request.employeeSalary.*;
import com.example.onekids_project.request.finance.approved.IdAndDateNotNullRequest;
import com.example.onekids_project.request.finance.exportimport.ExportStatisticalSalaryRequest;
import com.example.onekids_project.request.finance.statistical.FinanceKidsStatisticalRequest;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.response.employeesalary.*;
import com.example.onekids_project.response.excel.ExcelNewResponse;
import com.example.onekids_project.response.excel.ExcelResponse;
import com.example.onekids_project.response.finance.order.OrderPrintResponse;
import com.example.onekids_project.response.finance.statistical.FinanceKidsStatisticalResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.employeesaraly.EmployeeSalaryService;
import com.example.onekids_project.service.servicecustom.employeesaraly.FinanceStatisticalEmployeeService;
import com.example.onekids_project.service.servicecustom.employeesaraly.OrderEmployeeHistoryService;
import com.example.onekids_project.service.servicecustom.employeesaraly.OrderSalaryService;
import com.example.onekids_project.util.RequestUtils;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/web/fn/salary")
public class FnEmployeeSalaryController {

    @Autowired
    private EmployeeSalaryService employeeSalaryService;

    @Autowired
    private OrderEmployeeHistoryService orderEmployeeHistoryService;

    @Autowired
    private OrderSalaryService orderSalaryService;

    @Autowired
    private FinanceStatisticalEmployeeService financeStatisticalEmployeeService;


    /**
     * thống kê khoản các khoản mặc đinh cho một nhân sự
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/salary-default")
    public ResponseEntity statisticalDefault(@CurrentUser UserPrincipal principal, @Valid IdObjectRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        List<EmployeeStatisticalDefaultResponse> responseList = employeeSalaryService.statisticalDefault(principal, request);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * Tạo khoản mặc định cho một nhân sự
     *
     * @param principal
     * @param idInfoEmployee
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/salary-default/{idInfoEmployee}")
    public ResponseEntity createEmployeeSalaryDefault(@CurrentUser UserPrincipal principal, @PathVariable Long idInfoEmployee, @Valid @RequestBody EmployeeSalaryDefaultCreateRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = employeeSalaryService.createEmployeeSalaryDefault(principal, idInfoEmployee, request);
        if (check) {
            return NewDataResponse.setDataCreate(check);
        }
        return NewDataResponse.setDataCustom(check, MessageConstant.CREATE_SALARY_DEFAULT_FAIL);
    }

    /**
     * update khoản mặc định của một nhân sự
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/salary-default")
    public ResponseEntity updateEmployeeSalary(@CurrentUser UserPrincipal principal, @Valid @RequestBody EmployeeSalaryUpdateRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = employeeSalaryService.updateEmployeeSalary(principal, request);
        return NewDataResponse.setDataUpdate(check);
    }

    /**
     * xóa một khoản mặc định
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/salary-default/{id}")
    public ResponseEntity deleteEmployeeSalaryDefaultOne(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequest(principal, id);
        boolean check = employeeSalaryService.deleteEmployeeSalaryDefault(principal, id);
        return NewDataResponse.setDataDelete(check);
    }

    /**
     * xóa nhiều khoản mặc định
     *
     * @param principal
     * @param idList
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/salary-default/many")
    public ResponseEntity deleteEmployeeSalaryDefaultMany(@CurrentUser UserPrincipal principal, @RequestParam List<Long> idList) {
        RequestUtils.getFirstRequest(principal, idList);
        boolean check = employeeSalaryService.deleteEmployeeSalaryDefaultMany(principal, idList);
        return NewDataResponse.setDataDelete(check);
    }

    /**
     * lấy chi tiết một khoản mặc định
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/salary-default/{id}")
    public ResponseEntity seachSalaryDefault(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequest(principal, id);
        FnEmployeeSalaryDefaultResponse response = employeeSalaryService.searchSalaryDefault(principal, id);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * active hoạc no active một khoản mặc định
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/salary-default/active/{id}")
    public ResponseEntity activeSalaryDefault(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequest(principal, id);
        ActiveSalaryResponse response = employeeSalaryService.activeSalaryDefault(principal, id);
        if (response.isActive()) {
            return NewDataResponse.setDataCustom(response, MessageConstant.ACTIVE_SARALY_DEFAULT);
        }
        return NewDataResponse.setDataCustom(response, MessageConstant.CANCEL_SARALY_DEFAULT);
    }


    /**
     * danh sách tab áp dụng ở dialog
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/salary-apply/dialog")
    public ResponseEntity statisticalApply(@CurrentUser UserPrincipal principal, @Valid EmployeeSalaryApplyRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        List<EmployeeStatisticalApplyResponse> responseList = employeeSalaryService.statisticalApply(principal, request);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * DelActive một khoản đã được active
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/salary-apply/{id}")
    public ResponseEntity deleteEmployeeSalaryApplyOne(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequest(principal, id);
        boolean check = employeeSalaryService.deleteEmployeeSalaryApply(principal, id);
        return NewDataResponse.setDataDelete(check);
    }

    /**
     * xóa nhiều khoản áp dụng
     *
     * @param principal
     * @param idList
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/salary-apply/many")
    public ResponseEntity deleteEmployeeSalaryApplyMany(@CurrentUser UserPrincipal principal, @RequestParam List<Long> idList) {
        RequestUtils.getFirstRequest(principal, idList);
        boolean check = employeeSalaryService.deleteEmployeeSalaryApplyMany(principal, idList);
        return NewDataResponse.setDataDelete(check);
    }

    /**
     * Lấy chi tiết một khoản đã được active
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/salary-apply/{id}")
    public ResponseEntity seachSaralyApply(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequest(principal, id);
        EmployeeStatisticalApplyResponse response = employeeSalaryService.searchSalaryApply(principal, id);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * Sửa một khoản đã được active
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/salary-apply")
    public ResponseEntity updateEmployeeSalaryApply(@CurrentUser UserPrincipal principal, @Valid @RequestBody EmployeeSalaryApplyUpdateRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = employeeSalaryService.updateEmployeeSalaryApply(principal, request);
        return NewDataResponse.setDataUpdate(check);
    }

    /**
     * tạo khoản áp dụng cho một nhân sự
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/salary-apply/create/{idInfoEmployee}")
    public ResponseEntity createEmployeeSalaryCustom(@CurrentUser UserPrincipal principal, @PathVariable Long idInfoEmployee, @Valid @RequestBody EmployeeSalaryApplyCreateRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = employeeSalaryService.createEmployeeSalaryCustom(principal, idInfoEmployee, request);
        return NewDataResponse.setDataCreate(check);
    }

    /**
     * khởi tạo áp dụng từ mặc định
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/generate/employee-salary/default")
    public ResponseEntity generateEmployeeSalaryFromDefault(@CurrentUser UserPrincipal principal, @Valid @RequestBody GenerateEmployeeSalaryFromDefaultRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        int number = employeeSalaryService.generateEmployeeSalaryFromDefault(principal, request);
        String message = number == 0 ? MessageWebConstant.ZERO_SALARY_CREATE : "Tạo thành công " + number + " khoản";
        return NewDataResponse.setDataCustom(number, message);
    }

    /**
     * khởi tạo áp dụng từ mẫu trường
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/generate/employee-salary/school")
    public ResponseEntity generateEmployeeSalaryFromSchool(@CurrentUser UserPrincipal principal, @Valid @RequestBody GenerateEmployeeSalaryFromSchoolRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = employeeSalaryService.generateEmployeeSalaryFromSchool(principal, request);
        String message = "Tạo thành công " + request.getIdSalaryList().size() + " khoản";
        return NewDataResponse.setDataCustom(check, message);
    }

    /**
     * khởi tạo khoản mặc định từ mẫu nhà trường
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/generate/employee-salary-default")
    public ResponseEntity generateEmployeeSalaryDefault(@CurrentUser UserPrincipal principal, @Valid @RequestBody GenerateEmployeeSalaryDefaultRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        int number = employeeSalaryService.generateEmployeeSalaryDefault(principal, request);
        String message = number == 0 ? MessageWebConstant.ZERO_SALARY : "Tạo thành công " + number + " khoản";
        return NewDataResponse.setDataCustom(number, message);
    }

    /**
     * lấy mẫu khoản thu của trường
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/generate/salary-sample/brief")
    public ResponseEntity getSalarySampleBrief(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        List<FnSalarySampleBriefResponse> responseList = employeeSalaryService.searchSalarySampleBrief(principal);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * tìm kiếm danh sách ở thiết lập tiền lương
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/salary-apply/list")
    public ResponseEntity searchEmployeeSalarySetting(@CurrentUser UserPrincipal principal, @Valid EmployeeSalaryApplySearchRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        List<EmployeeSalarySettingResponse> responseList = employeeSalaryService.searchEmployeeSalarySetting(principal, request);
        return NewDataResponse.setDataSearch(responseList);
    }
    //end thiết lập tiền lương

    //start mẫu công lương

    /**
     * search mẫu công lương theo tên
     *
     * @param principal
     * @param name
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/salary-sample")
    public ResponseEntity searchSalarySample(@CurrentUser UserPrincipal principal, @RequestParam String name) {
        RequestUtils.getFirstRequest(principal, name);
        List<SalarySampleResponse> responseList = employeeSalaryService.searchSalarySample(principal, name);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * lấy chi tiết một mẫu công lương
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/salary-sample/{id}")
    public ResponseEntity searchSalarySample(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequest(principal, id);
        SalarySampleResponse response = employeeSalaryService.detailSalarySample(principal, id);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * Tạo mới mẫu công lương
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/salary-sample")
    public ResponseEntity createSalarySample(@CurrentUser UserPrincipal principal, @Valid @RequestBody SalarySampleCreateRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = employeeSalaryService.createSalarySample(principal, request);
        return NewDataResponse.setDataCreate(check);
    }

    /**
     * Sửa mẫu công lương
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/salary-sample")
    public ResponseEntity updateSalarySample(@CurrentUser UserPrincipal principal, @Valid @RequestBody SalarySampleUpdateRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = employeeSalaryService.updateSalarySample(principal, request);
        return NewDataResponse.setDataUpdate(check);
    }

    /**
     * xóa khoản công lương
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/salary-sample/{id}")
    public ResponseEntity deleteSalarySample(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequest(principal, id);
        boolean check = employeeSalaryService.deleteSalarySample(principal, id);
        return NewDataResponse.setDataDelete(check);
    }
    //end mẫu công lương

    //start duyệt bảng lương

    /**
     * lấy danh sách duyệt khoản lương
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/approved/list")
    public ResponseEntity searchEmployeeApprovedSalary(@CurrentUser UserPrincipal principal, @Valid EmployeeSalaryApplySearchRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        List<EmployeeSalaryResponse> responseList = employeeSalaryService.searchEmployeeSalaryApproved(principal, request);
        return NewDataResponse.setDataSearch(responseList);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/approved/detail")
    public ResponseEntity searchEmployeeApprovedSalaryDetail(@CurrentUser UserPrincipal principal, @Valid IdAndDateNotNullRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        List<EmployeeSalaryResponse> responseList = employeeSalaryService.searchEmployeeSalaryApprovedDetail(principal, request);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * Lưu số lượng sử dụng, sử dụng số tính toán cho một học sinh
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/approved/use-number/one")
    public ResponseEntity numberUser(@CurrentUser UserPrincipal principal, @Valid @RequestBody SalaryNumberUserRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = employeeSalaryService.numberUser(principal, request);
        return NewDataResponse.setDataUpdate(check);
    }

    /**
     * lưu số sử dụng cho nhiều học sinh
     *
     * @param principal
     * @param requestList
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/approved/use-number/save/many")
    public ResponseEntity saveUseNumberMany(@CurrentUser UserPrincipal principal, @Valid @RequestBody List<SaveNumberManyEmployeeRequest> requestList) {
        RequestUtils.getFirstRequest(principal, requestList);
        boolean check = employeeSalaryService.saveUseNumberMany(principal, requestList);
        return NewDataResponse.setDataSave(check);
    }

    /**
     * chuyển số tính toán sang số sử dụng cho nhiều học sinh
     *
     * @param principal
     * @param requestList
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/approved/use-number/transfer/many")
    public ResponseEntity saveTransferNumberMany(@CurrentUser UserPrincipal principal, @Valid @RequestBody List<TransferNumberManyEmployeeRequest> requestList) {
        RequestUtils.getFirstRequest(principal, requestList);
        boolean check = employeeSalaryService.saveTransferNumberMany(principal, requestList);
        return NewDataResponse.setDataCustom(check, MessageWebConstant.NUMBER_TRANSFER);
    }

    /**
     * Duyệt một khoản lương
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/approved/approved/one/{id}")
    public ResponseEntity approvedOneSalary(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequest(principal, id);
        boolean check = employeeSalaryService.approvedOneSalary(principal, id);
        return NewDataResponse.setDataApproved(check);
    }

    /**
     * Duyệt nhiều khoản lương
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/approved/approved/many")
    public ResponseEntity approvedAllSalary(@CurrentUser UserPrincipal principal, @Valid @RequestBody SalaryStatusRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        employeeSalaryService.approvedAllSalary(principal, request);
        return NewDataResponse.setDataApproved(request.getStatus());
    }

    /**
     * Khóa một khoản lương
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/approved/locked/one/{id}")
    public ResponseEntity lockedOneSalary(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequest(principal, id);
        boolean check = employeeSalaryService.lockedOneSalary(principal, id);
        return NewDataResponse.setDataLocked(check);
    }

    /**
     * Khóa nhiều khoản lương
     *
     * @param principal
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/approved/locked/many")
    public ResponseEntity lockedAllSalary(@CurrentUser UserPrincipal principal, @Valid @RequestBody SalaryStatusRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = employeeSalaryService.lockedAllSalary(principal, request);
        return NewDataResponse.setDataLocked(request.getStatus());
    }
    //end duyệt bảng lương

    //start thanh toán lương

    /**
     * lấy danh sách ở màn thanh toán lương
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/order/list")
    public ResponseEntity searchEmployeeSalaryPaid(@CurrentUser UserPrincipal principal, @Valid EmployeeSalaryApplySearchRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        List<EmployeeSalaryPaidResponse> responseList = employeeSalaryService.searchOrderEmployee(principal, request);
        return NewDataResponse.setDataSearch(responseList);
    }


    /**
     * danh sách hóa đơn theo năm
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/order/month")
    public ResponseEntity salaryDetailYear(@CurrentUser UserPrincipal principal, @Valid YearIdRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        List<EmployeeSalaryDetailYearResponse> responseList = employeeSalaryService.searchOrderMonth(principal, request);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * khởi tạo hóa đơn
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/order/generate")
    public ResponseEntity generateBill(@CurrentUser UserPrincipal principal, @Valid @RequestBody SalaryMultiRequest request) throws ExecutionException, FirebaseMessagingException, InterruptedException {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = employeeSalaryService.generateBill(principal, request);
        return NewDataResponse.setDataCreate(check);
    }

    /**
     * gửi thông báo hóa đơn công lương
     *
     * @param principal
     * @param request
     * @return
     * @throws FirebaseMessagingException
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/order/notify")
    public ResponseEntity sendNotify(@CurrentUser UserPrincipal principal, @Valid @RequestBody SalaryMultiRequest request) throws FirebaseMessagingException, ExecutionException, InterruptedException {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = employeeSalaryService.sendNotifyOrder(principal, request);
        return NewDataResponse.setDataCustom(check, MessageWebConstant.SEND_NOTIFY);
    }


    /**
     * cập nhật hiện thị 1 hoặc nhiều hóa đơn
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/order/view")
    public ResponseEntity viewBill(@CurrentUser UserPrincipal principal, @Valid @RequestBody StatusListRequest request) throws ExecutionException, FirebaseMessagingException, InterruptedException {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = employeeSalaryService.viewBill(principal, request);
        String messenger = request.getStatus() ? MessageWebConstant.VIEW_ACTIVE : MessageWebConstant.VIEW_INACTIVE;
        return NewDataResponse.setDataCustom(check, messenger);
    }

    /**
     * cập nhật khóa 1 hoặc nhiều hóa đơn
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/order/locked")
    public ResponseEntity lockedBill(@CurrentUser UserPrincipal principal, @Valid @RequestBody StatusListRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = employeeSalaryService.lockedBill(principal, request);
        return NewDataResponse.setDataLocked(check);
    }

    /**
     * lấy thanh toán hóa đơn dialog
     *
     * @param principal
     * @param idOrder
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/order/payment/{idOrder}")
    public ResponseEntity searchBillPaid(@CurrentUser UserPrincipal principal, @PathVariable Long idOrder, @Valid BillPaidRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        FnEmployeeSalaryResponse responseList = employeeSalaryService.searchOrderPayment(principal, idOrder, request);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * thực hiện thanh toán dialog
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/order/payment/{idOrder}")
    public ResponseEntity orderSalaryPayment(@CurrentUser UserPrincipal principal, @PathVariable Long idOrder, @Valid @RequestBody OrderSalaryPaymentRequest request) throws ExecutionException, FirebaseMessagingException, InterruptedException {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = employeeSalaryService.orderSalaryPayment(principal, idOrder, request);
        return NewDataResponse.setDataCustom(check, MessageWebConstant.PAYMENT_SUCCESS);
    }

    /**
     * in hóa đơn
     *
     * @param principal
     * @param request
     * @param idList
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/order/print")
    public ResponseEntity orderPrint(@CurrentUser UserPrincipal principal, @Valid BillPaidRequest request, @Valid IdListRequest idList) {
        RequestUtils.getFirstRequestExtend(principal, request, idList);
        OrderPrintResponse response = employeeSalaryService.getPrintOrder(principal, request, idList);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * in hóa đơn in
     *
     * @param principal
     * @param request
     * @param idList
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/order/print/in")
    public ResponseEntity orderPrintIn(@CurrentUser UserPrincipal principal, @Valid BillPaidRequest request, @Valid IdListRequest idList) {
        RequestUtils.getFirstRequestExtend(principal, request, idList);
        OrderPrintResponse response = employeeSalaryService.getPrintOrderIn(principal, request, idList);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * in hóa đơn out
     *
     * @param principal
     * @param request
     * @param idList
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/order/print/out")
    public ResponseEntity orderPrintOut(@CurrentUser UserPrincipal principal, @Valid BillPaidRequest request, @Valid IdListRequest idList) {
        RequestUtils.getFirstRequestExtend(principal, request, idList);
        OrderPrintResponse response = employeeSalaryService.getPrintOrderOut(principal, request, idList);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * lịch sửa thanh toán hóa đơn
     *
     * @param principal
     * @param idOrder
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/order/history/{idOrder}")
    public ResponseEntity findOrderSalaryHistory(@CurrentUser UserPrincipal principal, @PathVariable Long idOrder) {
        RequestUtils.getFirstRequest(principal, idOrder);
        List<OrderSalaryHistoryResponse> responseList = orderEmployeeHistoryService.findOrderKidsHistory(principal, idOrder);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * chi tiết lịch sử một lần thanh toán hóa đơn
     *
     * @param principal
     * @param idOrderHistory
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/order/history/detail/{idOrderHistory}")
    public ResponseEntity findOrderSalaryHistoryDetail(@CurrentUser UserPrincipal principal, @PathVariable Long idOrderHistory) {
        RequestUtils.getFirstRequest(principal, idOrderHistory);
        List<OrderSalaryHistoryDetailResponse> responseList = orderEmployeeHistoryService.findOrderKidsHistoryDetail(principal, idOrderHistory);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * tìm kiếm chi tiết một hóa đơn
     *
     * @param principal
     * @param idOrder
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/order/detail/{idOrder}")
    public ResponseEntity findOrderSalaryDetail(@CurrentUser UserPrincipal principal, @PathVariable Long idOrder) {
        RequestUtils.getFirstRequest(principal, idOrder);
        ListOrderSalaryDetailResponse response = orderSalaryService.findSalaryPackagePaymentDetail(principal, idOrder);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * lưu mô tả hóa đơn
     *
     * @param principal
     * @param idOrder
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/order/description/{idOrder}")
    public ResponseEntity saveOrderSalaryDescription(@CurrentUser UserPrincipal principal, @PathVariable Long idOrder, @RequestBody DescriptionRequest request) {
        RequestUtils.getFirstRequest(principal, idOrder, request);
        boolean check = orderSalaryService.saveOrderSalaryDescription(principal, idOrder, request);
        return NewDataResponse.setDataSave(check);
    }

    /**
     * tìm kiếm lịch sử các lần thanh toán của một khoản thu
     *
     * @param principal
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/order/history/salary-package/{idSalaryPackage}")
    public ResponseEntity findSalaryPackagePaymentDetail(@CurrentUser UserPrincipal principal, @PathVariable Long idSalaryPackage) {
        RequestUtils.getFirstRequest(principal, idSalaryPackage);
        List<SalaryPackageHistoryPaymentResponse> responseList = orderEmployeeHistoryService.findSalaryPackagePaymentDetail(principal, idSalaryPackage);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * thống kê công lương
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/order/statistical/common")
    public ResponseEntity statisticalSalary(@CurrentUser UserPrincipal principal, @Valid FinanceKidsStatisticalRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        FinanceKidsStatisticalResponse response = financeStatisticalEmployeeService.statisticalFinanceEmployee(principal, request);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * xuất file thống kê công lương
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/order/statistical/export")
    public ResponseEntity exportSalaryStatistical(@CurrentUser UserPrincipal principal, @Valid ExportStatisticalSalaryRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        List<ExcelResponse> responseList = financeStatisticalEmployeeService.exportExcelOrder(principal, request);
        return NewDataResponse.setDataSearch(responseList);
    }
    //end thanh toán lương

    /**
     * Xuất file tổng hợp điểm danh nhân viên
     *
     * @param principal
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping(method = RequestMethod.GET, value = "/export-employ-attedance")
    public ResponseEntity getAllAttendanceKidCustom(@CurrentUser UserPrincipal principal, SearchAttendanceSalaryRequest request) throws IOException {
        RequestUtils.getFirstRequestPlus(principal, "/export-employ-attedance");
        ByteArrayInputStream in = orderEmployeeHistoryService.exportAttendanceSalary(principal, request);
        return ResponseEntity.ok().body(new InputStreamResource(in));
    }

    /**
     * Xuất file tổng hợp điểm danh nhân viên NEW
     *
     * @param principal
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping(method = RequestMethod.GET, value = "/export-employ-attedance-new")
    public ResponseEntity getAllAttendanceKidCustomNew(@CurrentUser UserPrincipal principal, SearchAttendanceSalaryRequest request) throws IOException {
        RequestUtils.getFirstRequestPlus(principal, "/export-employ-attedance-new");
        List<ExcelNewResponse> list = orderEmployeeHistoryService.exportAttendanceSalaryNew(principal, request);
        return NewDataResponse.setDataSearch(list);
    }

}
