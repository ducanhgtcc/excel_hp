package com.example.onekids_project.controller.finance;

import com.example.onekids_project.request.finance.financegroup.*;
import com.example.onekids_project.request.finance.statistical.FinanceKidsStatisticalRequest;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.response.excel.ExcelResponse;
import com.example.onekids_project.response.finance.financegroup.*;
import com.example.onekids_project.response.finance.statistical.FinanceKidsStatisticalResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.employeesaraly.FinanceStatisticalEmployeeService;
import com.example.onekids_project.service.servicecustom.finance.FinanceStatisticalKidsService;
import com.example.onekids_project.service.servicecustom.finance.FnPackageGroupService;
import com.example.onekids_project.util.RequestUtils;
import com.example.onekids_project.validate.CommonValidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * date 2021-06-01 15:34
 *
 * @author lavanviet
 */
@RestController
@RequestMapping("/web/fn/statistical")
public class FinanceStatisticalController {
    @Autowired
    private FnPackageGroupService fnPackageGroupService;
    @Autowired
    private FinanceStatisticalKidsService financeStatisticalKidsService;
    @Autowired
    private FinanceStatisticalEmployeeService financeStatisticalEmployeeService;

    //nhóm học phí
    @RequestMapping(method = RequestMethod.GET, value = "/kids/group")
    public ResponseEntity searchPackageGroup(@CurrentUser UserPrincipal principal, @RequestParam String name) {
        RequestUtils.getFirstRequest(principal, name);
        CommonValidate.checkDataPlus(principal);
        List<PackageGroupResponse> responseList = fnPackageGroupService.searchPackageGroup(principal, name);
        return NewDataResponse.setDataSearch(responseList);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/kids/group/{id}")
    public ResponseEntity getPackageGroupById(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkDataPlus(principal);
        PackageGroupResponse response = fnPackageGroupService.getPackageGroupById(principal, id);
        return NewDataResponse.setDataSearch(response);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/kids/group")
    public ResponseEntity createPackageGroup(@CurrentUser UserPrincipal principal, @Valid @RequestBody PackageGroupCreateRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        boolean check = fnPackageGroupService.createPackageGroup(principal, request);
        return NewDataResponse.setDataCreate(check);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/kids/group")
    public ResponseEntity updatePackageGroup(@CurrentUser UserPrincipal principal, @Valid @RequestBody PackageGroupUpdateRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        boolean check = fnPackageGroupService.updatePackageGroup(principal, request);
        return NewDataResponse.setDataUpdate(check);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/kids/group/{id}")
    public ResponseEntity deletePackageGroupById(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkDataPlus(principal);
        boolean check = fnPackageGroupService.deletePackageGroupById(principal, id);
        return NewDataResponse.setDataDelete(check);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/kids/group/add")
    public ResponseEntity getPackageOutGroup(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataPlus(principal);
        List<FnKidsPackageForGroupResponse> responseList = fnPackageGroupService.getPackageForAdd(principal);
        return NewDataResponse.setDataSearch(responseList);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/kids/group/add/{id}")
    public ResponseEntity addPackageIntoGroup(@CurrentUser UserPrincipal principal, @PathVariable Long id, @RequestParam List<Long> idList) {
        RequestUtils.getFirstRequestExtend(principal, id, idList);
        CommonValidate.checkDataPlus(principal);
        boolean check = fnPackageGroupService.addPackageIntoGroup(principal, id, idList);
        return NewDataResponse.setDataUpdate(check);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/kids/group/remove/{id}")
    public ResponseEntity getPackageInGroup(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkDataPlus(principal);
        List<FnKidsPackageForGroupResponse> responseList = fnPackageGroupService.getPackageForRemove(principal, id);
        return NewDataResponse.setDataUpdate(responseList);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/kids/group/remove")
    public ResponseEntity removePackageInGroup(@CurrentUser UserPrincipal principal, @RequestParam List<Long> idList) {
        RequestUtils.getFirstRequest(principal, idList);
        CommonValidate.checkDataPlus(principal);
        boolean check = fnPackageGroupService.removePackageInGroup(principal, idList);
        return NewDataResponse.setDataUpdate(check);
    }


    //nhóm công lương
    @RequestMapping(method = RequestMethod.GET, value = "/employee/group")
    public ResponseEntity searchSalaryGroup(@CurrentUser UserPrincipal principal, @RequestParam String name) {
        RequestUtils.getFirstRequest(principal, name);
        CommonValidate.checkDataPlus(principal);
        List<PackageGroupResponse> responseList = fnPackageGroupService.searchSalaryGroup(principal, name);
        return NewDataResponse.setDataSearch(responseList);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/employee/group/{id}")
    public ResponseEntity getSalaryGroupById(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkDataPlus(principal);
        PackageGroupResponse response = fnPackageGroupService.getSalaryGroupById(principal, id);
        return NewDataResponse.setDataSearch(response);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/employee/group")
    public ResponseEntity createSalaryGroup(@CurrentUser UserPrincipal principal, @Valid @RequestBody PackageGroupCreateRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        boolean check = fnPackageGroupService.createSalaryGroup(principal, request);
        return NewDataResponse.setDataCreate(check);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/employee/group")
    public ResponseEntity updateSalaryGroup(@CurrentUser UserPrincipal principal, @Valid @RequestBody PackageGroupUpdateRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        boolean check = fnPackageGroupService.updateSalaryGroup(principal, request);
        return NewDataResponse.setDataUpdate(check);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/employee/group/{id}")
    public ResponseEntity deleteSalaryGroupById(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkDataPlus(principal);
        boolean check = fnPackageGroupService.deleteSalaryGroupById(principal, id);
        return NewDataResponse.setDataDelete(check);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/employee/group/add")
    public ResponseEntity getSalaryGroup(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataPlus(principal);
        List<PackageGroupResponse> responseList = fnPackageGroupService.getSalaryGroup(principal);
        return NewDataResponse.setDataSearch(responseList);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/employee/group/add/{id}")
    public ResponseEntity addSampleSalaryIntoGroup(@CurrentUser UserPrincipal principal, @PathVariable Long id, @RequestParam List<Long> idList, String type) {
        RequestUtils.getFirstRequestExtend(principal, id, idList);
        CommonValidate.checkDataPlus(principal);
        boolean check = fnPackageGroupService.addSalarySampleIntoGroup(principal, id, idList, type);
        return NewDataResponse.setDataUpdate(check);
    }

    //số liệu báo cáo
    @RequestMapping(method = RequestMethod.GET, value = "/money/kids/statistical")
    public ResponseEntity getStatisticalFees(@CurrentUser UserPrincipal principal, @Valid PackageGroupSearchRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        List<PackageGroupStatisticalResponse> responseList = fnPackageGroupService.getStatisticalFees(principal, request);
        return NewDataResponse.setDataUpdate(responseList);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/money/kids/export")
    public ResponseEntity getStatisticalFeesExport(@CurrentUser UserPrincipal principal, @Valid ExportRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        List<ExcelResponse> responseList = fnPackageGroupService.exportStatisticalFees(principal, request);
        return NewDataResponse.setDataUpdate(responseList);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/money/employee/statistical")
    public ResponseEntity getStatisticalSalary(@CurrentUser UserPrincipal principal, @Valid PackageGroupSearchRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        List<PackageGroupStatisticalResponse> responseList = fnPackageGroupService.getStatisticalSalary(principal, request);
        return NewDataResponse.setDataUpdate(responseList);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/money/employee/export")
    public ResponseEntity getStatisticalSalaryExport(@CurrentUser UserPrincipal principal, @Valid ExportRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        List<ExcelResponse> responseList = fnPackageGroupService.exportStatisticalSalary(principal, request);
        return NewDataResponse.setDataUpdate(responseList);
    }

    //thống kê
    @RequestMapping(method = RequestMethod.GET, value = "/statistical/cashbook")
    public ResponseEntity getStatisticalCashbook(@CurrentUser UserPrincipal principal, @Valid CashBookMoneyRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        CashBookMoneyResponse response = fnPackageGroupService.getCashBookStatistical(principal, request);
        return NewDataResponse.setDataSearch(response);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/statistical/internal")
    public ResponseEntity getStatisticalInternal(@CurrentUser UserPrincipal principal, @Valid FinanceKidsStatisticalRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        InternalMoneyResponse response = fnPackageGroupService.getInternalStatistical(principal, request);
        return NewDataResponse.setDataSearch(response);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/statistical/fees")
    public ResponseEntity statisticalFinanceKids(@CurrentUser UserPrincipal principal, @Valid FinanceKidsStatisticalRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        FinanceKidsStatisticalResponse response = financeStatisticalKidsService.statisticalFinanceKids(principal, request);
        return NewDataResponse.setDataSearch(response);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/statistical/salary")
    public ResponseEntity statisticalFinanceEmployee(@CurrentUser UserPrincipal principal, @Valid FinanceKidsStatisticalRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        FinanceKidsStatisticalResponse response = financeStatisticalEmployeeService.statisticalFinanceEmployee(principal, request);
        return NewDataResponse.setDataSearch(response);
    }

}
