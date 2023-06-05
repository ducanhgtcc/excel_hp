package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.request.accounttype.AccountTypeCreateRequest;
import com.example.onekids_project.request.accounttype.AccountTypeUpdateRequest;
import com.example.onekids_project.response.accounttype.AccountTypeDetailReponse;
import com.example.onekids_project.response.accounttype.AccountTypeResponse;
import com.example.onekids_project.security.model.UserPrincipal;

import java.util.List;

public interface AccountTypeService {
    List<AccountTypeResponse> findAccountType(UserPrincipal principal);

    AccountTypeDetailReponse findAccountTypeById(UserPrincipal principal, Long id);

    boolean createAccountType(UserPrincipal principal, AccountTypeCreateRequest request);

    boolean updateAccountType(UserPrincipal principal, AccountTypeUpdateRequest request);

    boolean deleteAccountTypeById(UserPrincipal principal, Long id);

}
