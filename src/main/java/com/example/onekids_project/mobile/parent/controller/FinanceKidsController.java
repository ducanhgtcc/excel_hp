package com.example.onekids_project.mobile.parent.controller;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.MessageConstant;
import com.example.onekids_project.common.MessageWebConstant;
import com.example.onekids_project.mobile.parent.request.finance.KidsPackageParentRequest;
import com.example.onekids_project.mobile.parent.request.finance.OrderKidsParentRequest;
import com.example.onekids_project.mobile.parent.request.finance.WalletParentHistoryCreateParentRequest;
import com.example.onekids_project.mobile.parent.request.finance.WalletParentHistoryParentRequest;
import com.example.onekids_project.mobile.parent.response.finance.kidspackage.KidsPackageDetailParentResponse;
import com.example.onekids_project.mobile.parent.response.finance.kidspackage.KidsPackageWrapperParentResponse;
import com.example.onekids_project.mobile.parent.response.finance.order.*;
import com.example.onekids_project.mobile.parent.response.finance.walletparent.ListBankParentResponse;
import com.example.onekids_project.mobile.parent.response.finance.walletparent.WalletParentHistoryWrapperParentResponse;
import com.example.onekids_project.mobile.parent.response.finance.walletparent.WalletParentParentResponse;
import com.example.onekids_project.mobile.parent.service.servicecustom.FinanceKidsService;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.RequestUtils;
import com.example.onekids_project.validate.CommonValidate;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * date 2021-03-16 09:23
 *
 * @author lavanviet
 */
@RestController
@RequestMapping("/mob/parent/finance")
public class FinanceKidsController {
    @Autowired
    private FinanceKidsService financeKidsService;

    /**
     * lấy hóa đơn
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/order")
    public ResponseEntity searchOrderKids(@CurrentUser UserPrincipal principal, @Valid OrderKidsParentRequest request) {
        RequestUtils.getFirstRequestParent(principal, request);
        CommonValidate.checkDataParent(principal);
        List<OrderKidsParentResponse> responseList = financeKidsService.searchOrderKids(principal, principal.getIdKidLogin(), request);
        String message = CollectionUtils.isEmpty(responseList) ? MessageConstant.ORDER_EMPTY : AppConstant.SUCCESS_SEARCH;
        return NewDataResponse.setDataCustom(responseList, message);
    }

    /**
     * lấy lịch sử hóa đơn
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/order/history/{id}")
    public ResponseEntity searchOrderKidsHistory(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequestParent(principal, id);
        CommonValidate.checkDataParent(principal);
        List<OrderKidsHistoryParentResponse> responseList = financeKidsService.searchOrderKidsHistory(id);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * thống kê số tiền toàn bộ hóa đơn
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/order/statistical")
    public ResponseEntity statisticalOrderKids(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequestParent(principal);
        CommonValidate.checkDataParent(principal);
        OrderKidsStatisticalResponse response = financeKidsService.statisticalOrderKids(principal.getIdKidLogin());
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * chi tiết khoản thu ở trong hóa đơn
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/order/kids-package/{id}")
    public ResponseEntity getOrderKidsPackage(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequestParent(principal, id);
        CommonValidate.checkDataParent(principal);
        OrderKidsPackageParentResponse response = financeKidsService.findOrderKidsPackage(id);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * tìm kiếm khoản thu trong năm
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/kids-package")
    public ResponseEntity searchKidsPackage(@CurrentUser UserPrincipal principal, @Valid KidsPackageParentRequest request) {
        RequestUtils.getFirstRequestParent(principal, request);
        CommonValidate.checkDataParent(principal);
        List<KidsPackageWrapperParentResponse> responseList = financeKidsService.searchKidsPackageYear(principal, principal.getIdKidLogin(), request);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * tìm kiếm khoản thu theo id
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/kids-package/{id}")
    public ResponseEntity searchKidsPackage(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequestParent(principal, id);
        CommonValidate.checkDataParent(principal);
        KidsPackageDetailParentResponse response = financeKidsService.getKidsPackageById(id);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * tìm kiếm lịch sử ví
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/wallet-parent/history")
    public ResponseEntity searchWalletParentYear(@CurrentUser UserPrincipal principal, @Valid WalletParentHistoryParentRequest request) {
        RequestUtils.getFirstRequestParent(principal, request);
        List<WalletParentHistoryWrapperParentResponse> responseList = financeKidsService.searchWalletParentHistoryYear(principal, request);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * xác nhận lịch sử khoản thu
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/wallet-parent/history/confirm/{id}")
    public ResponseEntity confirmWalletParent(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequestParent(principal, id);
        boolean check = financeKidsService.confirmWalletParent(principal, id);
        return NewDataResponse.setDataCustom(check, MessageWebConstant.SUCCESS_CONFIRM);
    }

    /**
     * tìm kiếm thông tin ví
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/wallet-parent")
    public ResponseEntity findWalletParent(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequestParent(principal);
        WalletParentParentResponse response = financeKidsService.getWalletParent(principal);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * lấy thông tin ngân hàng
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/wallet-parent/bank")
    public ResponseEntity findBankParent(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequestParent(principal);
        ListBankParentResponse response = financeKidsService.findBankInSchool(principal);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * nạp tiền cho ví
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/wallet-parent/history")
    public ResponseEntity createWalletHistory(@CurrentUser UserPrincipal principal, @Valid @ModelAttribute WalletParentHistoryCreateParentRequest request) throws IOException, FirebaseMessagingException {
        RequestUtils.getFirstRequestParent(principal, request);
        boolean check = financeKidsService.createWalletParentHistory(principal, request);
        return NewDataResponse.setDataCustom(check, MessageConstant.WALLET_PARENT_IN);
    }

    /**
     * thống kê số lượng
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/statistical")
    public ResponseEntity statisticalNumber(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequestParent(principal);
        StatisticalOrderKidsResponse response = financeKidsService.statisticalNumber(principal);
        return NewDataResponse.setDataSearch(response);
    }
}
