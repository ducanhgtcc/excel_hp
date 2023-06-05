package com.example.onekids_project.service.servicecustom.finance;

import com.example.onekids_project.request.base.IdListRequest;
import com.example.onekids_project.request.common.SearchKidsCommonExcelRequest;
import com.example.onekids_project.request.common.SearchKidsCommonRequest;
import com.example.onekids_project.request.finance.wallet.WalletParentStatisticalRequest;
import com.example.onekids_project.response.excel.ExcelResponse;
import com.example.onekids_project.response.finance.wallet.ListWalletParentStatisticalResponse;
import com.example.onekids_project.response.finance.wallet.WalletParentResponse;
import com.example.onekids_project.security.model.UserPrincipal;

import java.util.List;

/**
 * date 2021-02-24 15:53
 *
 * @author lavanviet
 */
public interface WalletParentService {
    List<WalletParentResponse> searchWalletParent(UserPrincipal principal, SearchKidsCommonRequest request);

    List<WalletParentResponse> searchWalletParentUnConfirm(UserPrincipal principal);

    ListWalletParentStatisticalResponse searchWalletParentStatistical(UserPrincipal principal, WalletParentStatisticalRequest request);

    List<WalletParentResponse> searchWalletParentExcel(IdListRequest request);

    List<WalletParentResponse> searchWalletParentExcelProviso(SearchKidsCommonExcelRequest request);

    List<ExcelResponse> exportWalletParentExcel(UserPrincipal principal, List<WalletParentResponse> request);




}
