package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.parent.WalletParent;
import com.example.onekids_project.request.finance.wallet.WalletParentStatisticalRequest;

import java.util.List;

/**
 * date 2021-02-22 10:09
 *
 * @author lavanviet
 */
public interface WalletParentRepositoryCustom {
    List<WalletParent> searchWalletParentStatistical(Long idSchool, WalletParentStatisticalRequest request);

    long countWalletParentStatistical(Long idSchool, WalletParentStatisticalRequest request);
}

