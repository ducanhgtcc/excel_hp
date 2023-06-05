package com.example.onekids_project.mobile.plus.controller;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.MessageConstant;
import com.example.onekids_project.mobile.parent.request.finance.KidsPackageParentRequest;
import com.example.onekids_project.mobile.parent.request.finance.OrderKidsParentRequest;
import com.example.onekids_project.mobile.parent.response.finance.kidspackage.KidsPackageDetailParentResponse;
import com.example.onekids_project.mobile.parent.response.finance.kidspackage.KidsPackageWrapperParentResponse;
import com.example.onekids_project.mobile.parent.response.finance.order.OrderKidsHistoryParentResponse;
import com.example.onekids_project.mobile.parent.response.finance.order.OrderKidsPackageParentResponse;
import com.example.onekids_project.mobile.parent.response.finance.order.OrderKidsParentResponse;
import com.example.onekids_project.mobile.parent.response.finance.order.OrderKidsStatisticalResponse;
import com.example.onekids_project.mobile.parent.service.servicecustom.FinanceKidsService;
import com.example.onekids_project.mobile.plus.response.fees.FeesClassResponse;
import com.example.onekids_project.mobile.plus.response.fees.KidsFeesResponse;
import com.example.onekids_project.mobile.plus.service.servicecustom.FeesPlusService;
import com.example.onekids_project.mobile.request.DateNotNullRequest;
import com.example.onekids_project.mobile.request.IdAndDateRequest;
import com.example.onekids_project.mobile.request.IdListAndDateRequest;
import com.example.onekids_project.request.common.StatusRequest;
import com.example.onekids_project.request.finance.statistical.FinanceKidsStatisticalRequest;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.response.finance.statistical.FinanceKidsStatisticalResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.finance.FinanceStatisticalKidsService;
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
 * date 2021-06-07 08:44
 *
 * @author lavanviet
 */
@RestController
@RequestMapping("/mob/plus/fees")
public class FeesPlusController {
    @Autowired
    private FeesPlusService feesPlusService;
    @Autowired
    private FinanceStatisticalKidsService financeStatisticalKidsService;
    @Autowired
    private FinanceKidsService financeKidsService;

    @RequestMapping(method = RequestMethod.GET, value = "/class")
    public ResponseEntity getFeesClass(@CurrentUser UserPrincipal principal, @Valid DateNotNullRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        List<FeesClassResponse> responseList = feesPlusService.getFeesClass(principal, request.getDate());
        return NewDataResponse.setDataSearch(responseList);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/statistical")
    public ResponseEntity statisticalFinanceKids(@CurrentUser UserPrincipal principal, @Valid FinanceKidsStatisticalRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        FinanceKidsStatisticalResponse response = financeStatisticalKidsService.statisticalFinanceKids(principal, request);
        return NewDataResponse.setDataSearch(response);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/class/one")
    public ResponseEntity getClassOne(@CurrentUser UserPrincipal principal, @Valid IdAndDateRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        List<KidsFeesResponse> responseList = feesPlusService.getKidsInClass(principal, request);
        return NewDataResponse.setDataSearch(responseList);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/sms")
    public ResponseEntity sendSmsFees(@CurrentUser UserPrincipal principal, @Valid @RequestBody IdListAndDateRequest request) throws ExecutionException, InterruptedException {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        feesPlusService.sendSms(principal, request.getIdList(), request.getDate());
        return NewDataResponse.setSendSms();
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/app")
    public ResponseEntity sendAppFees(@CurrentUser UserPrincipal principal, @Valid @RequestBody IdListAndDateRequest request) throws ExecutionException, InterruptedException, FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        feesPlusService.sendApp(principal, request.getIdList(), request.getDate());
        return NewDataResponse.setSendApp();
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/order/view")
    public ResponseEntity setOrderShow(@CurrentUser UserPrincipal principal, @RequestBody List<StatusRequest> request) throws ExecutionException, InterruptedException, FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        feesPlusService.setOrderShow(principal, request);
        return NewDataResponse.setDataCustom(AppConstant.APP_TRUE, "Cập nhật hiện thị hóa đơn thành công");
    }

    @RequestMapping(method = RequestMethod.GET, value = "/order/{idKid}")
    public ResponseEntity searchOrderKids(@CurrentUser UserPrincipal principal, @PathVariable Long idKid, @Valid OrderKidsParentRequest request) {
        RequestUtils.getFirstRequestExtend(principal, idKid, request);
        CommonValidate.checkDataPlus(principal);
        List<OrderKidsParentResponse> responseList = financeKidsService.searchOrderKids(principal, idKid, request);
        String message = CollectionUtils.isEmpty(responseList) ? MessageConstant.ORDER_EMPTY : AppConstant.SUCCESS_SEARCH;
        return NewDataResponse.setDataCustom(responseList, message);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/order/history/{id}")
    public ResponseEntity searchOrderKidsHistory(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkDataPlus(principal);
        List<OrderKidsHistoryParentResponse> responseList = financeKidsService.searchOrderKidsHistory(id);
        return NewDataResponse.setDataSearch(responseList);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/order/statistical/{idKid}")
    public ResponseEntity statisticalOrderKids(@CurrentUser UserPrincipal principal, @PathVariable Long idKid) {
        RequestUtils.getFirstRequest(principal, idKid);
        CommonValidate.checkDataPlus(principal);
        OrderKidsStatisticalResponse response = financeKidsService.statisticalOrderKids(idKid);
        return NewDataResponse.setDataSearch(response);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/order/kids-package/{id}")
    public ResponseEntity getOrderKidsPackage(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequestParent(principal, id);
        CommonValidate.checkDataParent(principal);
        OrderKidsPackageParentResponse response = financeKidsService.findOrderKidsPackage(id);
        return NewDataResponse.setDataSearch(response);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/kids-package/kids/{idKid}")
    public ResponseEntity searchKidsPackage(@CurrentUser UserPrincipal principal, @PathVariable Long idKid, @Valid KidsPackageParentRequest request) {
        RequestUtils.getFirstRequestParent(principal, request);
        CommonValidate.checkDataPlus(principal);
        List<KidsPackageWrapperParentResponse> responseList = financeKidsService.searchKidsPackageYear(principal, idKid, request);
        return NewDataResponse.setDataSearch(responseList);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/kids-package/{id}")
    public ResponseEntity searchKidsPackage(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequestParent(principal, id);
        CommonValidate.checkDataPlus(principal);
        KidsPackageDetailParentResponse response = financeKidsService.getKidsPackageById(id);
        return NewDataResponse.setDataSearch(response);
    }

}
