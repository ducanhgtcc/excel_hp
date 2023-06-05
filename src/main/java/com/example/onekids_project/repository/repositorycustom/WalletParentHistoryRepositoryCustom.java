package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.parent.WalletParentHistory;
import com.example.onekids_project.request.finance.wallet.SearchWalletParentHistoryRequest;

import java.util.List;

/**
 * date 2021-02-25 08:56
 *
 * @author lavanviet
 */
public interface WalletParentHistoryRepositoryCustom {
    List<WalletParentHistory> searchWalletParentHistory(SearchWalletParentHistoryRequest request);
    List<WalletParentHistory> searchWalletParentHistoryFalse(Long idWalletParent);

    long countWalletParentHistory(SearchWalletParentHistoryRequest request);

    long countWalletParentHistoryFalse(Long idWalletParent);

    List<WalletParentHistory> getWalletParentHistory(Long idWallet, int year);

    List<WalletParentHistory> searchWalletParentHistory(Long idWallet, int year, String description);

    List<WalletParentHistory> searchWalletParentHistoryChart(Long idSchool, int year, Boolean confirm);

    long countWalletParentHistoryChart(Long idSchool, int month, int year, Boolean confirm);

    List<WalletParentHistory> searchWalletForPlus(Long idWallet, String status, int pageNumber);
}
