package com.example.onekids_project.mobile.plus.controller;

import com.example.onekids_project.common.MessageWebConstant;
import com.example.onekids_project.mobile.plus.request.fees.WalletStatusRequest;
import com.example.onekids_project.mobile.plus.service.servicecustom.WalletPlusService;
import com.example.onekids_project.mobile.response.wallet.*;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.finance.WalletParentHistoryService;
import com.example.onekids_project.util.RequestUtils;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * date 2021-10-22 08:33
 *
 * @author lavanviet
 */
@RestController
@RequestMapping("/mob/plus/wallet")
public class WalletPlusController {
    @Autowired
    private WalletPlusService walletPlusService;
    @Autowired
    private WalletParentHistoryService walletParentHistoryService;

    @RequestMapping(method = RequestMethod.GET, value = "/class")
    public ResponseEntity getWalletInClass(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequestPlus(principal);
        List<WalletClassPlusResponse> responseList = walletPlusService.getWalletClass(principal.getIdSchoolLogin());
        return NewDataResponse.setDataSearch(responseList);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/class/{idClass}")
    public ResponseEntity getWalletInKids(@CurrentUser UserPrincipal principal, @PathVariable Long idClass, @RequestParam String kidsStatus, @RequestParam String fullName) {
        RequestUtils.getFirstRequestPlus(principal, idClass);
        List<WalletKidsPlusResponse> responseList = walletPlusService.getWalletKids(idClass, kidsStatus, fullName);
        return NewDataResponse.setDataSearch(responseList);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/info/{idKid}")
    public ResponseEntity getWalletInfo(@CurrentUser UserPrincipal principal, @PathVariable Long idKid) {
        RequestUtils.getFirstRequestPlus(principal, idKid);
        WalletInfoResponse response = walletPlusService.getWalletInfoByKid(idKid);
        return NewDataResponse.setDataSearch(response);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/history/{idWallet}")
    public ResponseEntity getWalletHistory(@CurrentUser UserPrincipal principal, @PathVariable Long idWallet, @Valid WalletStatusRequest request) {
        RequestUtils.getFirstRequestPlus(principal, idWallet);
        ListWalletHistoryPlusResponse responseList = walletPlusService.getWalletHistoryKids(idWallet, request.getStatus(), request.getPageNumber());
        return NewDataResponse.setDataSearch(responseList);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/history/unConfirm")
    public ResponseEntity getWalletHistoryUnConfirm(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequestPlus(principal);
        List<WalletKidsPlusResponse> responseList = walletPlusService.getWalletUnConfirm(principal.getIdSchoolLogin());
        return NewDataResponse.setDataSearch(responseList);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/history/detail/{id}")
    public ResponseEntity getWalletHistoryDetail(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequestPlus(principal, id);
        WalletHistoryDetailResponse response = walletPlusService.getWalletHistoryDetail(id);
        return NewDataResponse.setDataSearch(response);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/history/confirm/{id}")
    public ResponseEntity confirmWalletParentHistory(@CurrentUser UserPrincipal principal, @PathVariable Long id) throws FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal, id);
        boolean check = walletParentHistoryService.confirmWalletParentHistory(principal, id);
        return NewDataResponse.setDataCustom(check, MessageWebConstant.SUCCESS_CONFIRM);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/history/delete/{id}")
    public ResponseEntity deleteWalletParentHistory(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequest(principal, id);
        boolean check = walletParentHistoryService.deleteWalletParentHistory(principal, id);
        return NewDataResponse.setDataDelete(check);
    }

}
