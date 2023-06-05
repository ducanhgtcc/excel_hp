package com.example.onekids_project.mobile.plus.controller;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.FinanceConstant;
import com.example.onekids_project.mobile.plus.request.cashinternal.CashBookPlusRequest;
import com.example.onekids_project.mobile.plus.request.cashinternal.CashInternalPlusRequest;
import com.example.onekids_project.mobile.plus.response.cashinternal.CashBookPlusResponse;
import com.example.onekids_project.mobile.plus.response.cashinternal.NumberCashInternalResponse;
import com.example.onekids_project.mobile.plus.service.servicecustom.CashInternalPlusService;
import com.example.onekids_project.mobile.request.IdAndStatusRequest;
import com.example.onekids_project.mobile.response.ListCashInternalPlusResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.RequestUtils;
import com.example.onekids_project.validate.CommonValidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * date 2021-06-18 13:36
 *
 * @author lavanviet
 */
@RestController
@RequestMapping("/mob/plus/cashInternal")
public class CashInternalPlusController {

    @Autowired
    private CashInternalPlusService cashInternalPlusService;

    @RequestMapping(method = RequestMethod.GET, value = "/in")
    public ResponseEntity getCashInternalIn(@CurrentUser UserPrincipal principal, @Valid CashInternalPlusRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        ListCashInternalPlusResponse response = cashInternalPlusService.getCashInternalPlus(principal, request, FinanceConstant.CATEGORY_IN);
        return NewDataResponse.setDataSearch(response);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/out")
    public ResponseEntity getCashInternalOut(@CurrentUser UserPrincipal principal, @Valid CashInternalPlusRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        ListCashInternalPlusResponse response = cashInternalPlusService.getCashInternalPlus(principal, request, FinanceConstant.CATEGORY_OUT);
        return NewDataResponse.setDataSearch(response);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/approved")
    public ResponseEntity approvedCashInternalOut(@CurrentUser UserPrincipal principal, @Valid @RequestBody List<IdAndStatusRequest> request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        boolean check = cashInternalPlusService.approvedCashInternalPlus(principal, request);
        return NewDataResponse.setDataCustom(check, AppConstant.SUCCESS_APPROVED);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/canceled/{id}")
    public ResponseEntity canceledCashInternalOut(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkDataPlus(principal);
        boolean check = cashInternalPlusService.canceledCashInternalPlus(principal, id);
        return NewDataResponse.setDataCustom(check, "Hủy phiếu thành công");
    }

    @RequestMapping(method = RequestMethod.GET, value = "/cashbook")
    public ResponseEntity getCashBook(@CurrentUser UserPrincipal principal, CashBookPlusRequest request) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataPlus(principal);
        List<CashBookPlusResponse> responseList = cashInternalPlusService.getCashBookPlus(principal, request);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * hiện thị số lượng
     * - phiếu thu chưa duyệt
     * - phiếu chi chưa duyệt
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/show/number")
    public ResponseEntity searchOrderKids(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataPlus(principal);
        NumberCashInternalResponse response = cashInternalPlusService.getShowNumber(principal);
        return NewDataResponse.setDataSearch(response);
    }
}
