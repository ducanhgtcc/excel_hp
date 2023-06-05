package com.example.onekids_project.mobile.parent.service.servicecustom;

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
import com.example.onekids_project.security.model.UserPrincipal;
import com.google.firebase.messaging.FirebaseMessagingException;

import java.io.IOException;
import java.util.List;

/**
 * date 2021-03-16 09:24
 *
 * @author lavanviet
 */
public interface FinanceKidsService {

    List<OrderKidsParentResponse> searchOrderKids(UserPrincipal principal, Long idKid, OrderKidsParentRequest request);

    List<OrderKidsHistoryParentResponse> searchOrderKidsHistory(Long idOrderKids);

    OrderKidsStatisticalResponse statisticalOrderKids(Long idKid);

    OrderKidsPackageParentResponse findOrderKidsPackage(Long idKidsPackage);

    List<KidsPackageWrapperParentResponse> searchKidsPackageYear(UserPrincipal principal, Long idKid, KidsPackageParentRequest request);

    KidsPackageDetailParentResponse getKidsPackageById(Long idKidsPackage);

    List<WalletParentHistoryWrapperParentResponse> searchWalletParentHistoryYear(UserPrincipal principal, WalletParentHistoryParentRequest request);

    boolean confirmWalletParent(UserPrincipal principal, Long idWalletHistory);

    WalletParentParentResponse getWalletParent(UserPrincipal principal);

    ListBankParentResponse findBankInSchool(UserPrincipal principal);

    boolean createWalletParentHistory(UserPrincipal principal, WalletParentHistoryCreateParentRequest request) throws IOException, FirebaseMessagingException;

    StatisticalOrderKidsResponse statisticalNumber(UserPrincipal principal);

    int getNumberOrderKidsNoCompleteYear(Long idKid, int year);
}
