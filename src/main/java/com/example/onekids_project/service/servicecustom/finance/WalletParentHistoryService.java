package com.example.onekids_project.service.servicecustom.finance;

import com.example.onekids_project.entity.finance.fees.OrderKidsHistory;
import com.example.onekids_project.entity.parent.WalletParent;
import com.example.onekids_project.request.finance.wallet.SearchWalletParentHistoryRequest;
import com.example.onekids_project.request.finance.wallet.WalletParentHistoryCreateRequest;
import com.example.onekids_project.response.finance.wallet.ListWalletParentHistoryResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.google.firebase.messaging.FirebaseMessagingException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.concurrent.ExecutionException;

/**
 * date 2021-02-25 09:05
 *
 * @author lavanviet
 */
public interface WalletParentHistoryService {
    boolean createWalletParentHistory(UserPrincipal principal, WalletParentHistoryCreateRequest request) throws IOException, FirebaseMessagingException, ExecutionException, InterruptedException;

    ListWalletParentHistoryResponse searchWalletParentHistory(UserPrincipal principal, SearchWalletParentHistoryRequest request);

    ListWalletParentHistoryResponse searchWalletParentHistoryFalse(UserPrincipal principal, Long idWalletParent);

    void saveMoneyWalletHistory(long idUser, String name, LocalDate date, String orderCode, String category, double money, WalletParent walletParent, OrderKidsHistory orderKidsHistory);

    boolean confirmWalletParentHistory(UserPrincipal principal, Long idWalletHistory) throws FirebaseMessagingException;

    boolean deleteWalletParentHistory(UserPrincipal principal, Long idWalletHistory);
}
