package com.example.onekids_project.service.servicecustom.cashinternal;

import com.example.onekids_project.request.base.IdObjectRequest;
import com.example.onekids_project.request.cashinternal.CreateBankInforRequest;
import com.example.onekids_project.request.cashinternal.UpdateBankInfoRequest;
import com.example.onekids_project.request.finance.wallet.BankBriefResponse;
import com.example.onekids_project.response.caskinternal.BankResponse;
import com.example.onekids_project.security.model.UserPrincipal;

import java.util.List;

public interface FnBankService {
    List<BankBriefResponse> getBankBrief(UserPrincipal principal);

    List<BankResponse> searchBankInfo(UserPrincipal principal);

    BankResponse findByIdBankInfo(UserPrincipal principal, Long id);

    boolean updateBankInfo(UserPrincipal principal, UpdateBankInfoRequest request);

    boolean deleteBankInFo(UserPrincipal principal, Long id);

    boolean createBankInfo(UserPrincipal principal, CreateBankInforRequest request);

    boolean updateCheckedBank(UserPrincipal principal, IdObjectRequest request);
}
