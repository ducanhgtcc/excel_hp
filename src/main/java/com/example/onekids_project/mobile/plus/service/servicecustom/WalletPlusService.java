package com.example.onekids_project.mobile.plus.service.servicecustom;

import com.example.onekids_project.mobile.response.wallet.*;

import java.util.List;

/**
 * date 2021-10-22 08:35
 *
 * @author lavanviet
 */
public interface WalletPlusService {

    List<WalletClassPlusResponse> getWalletClass(Long idSchool);

    List<WalletKidsPlusResponse> getWalletKids(Long idClass, String kidsStatus, String fullName);

    WalletInfoResponse getWalletInfoByKid(Long idKid);

    ListWalletHistoryPlusResponse getWalletHistoryKids(Long idWallet, String status, int pageNumber);

    List<WalletKidsPlusResponse> getWalletUnConfirm(Long idSchool);

    WalletHistoryDetailResponse getWalletHistoryDetail(Long id);
}
