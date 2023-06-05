package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.common.MessageWebConstant;
import com.example.onekids_project.entity.employee.AccountType;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.AccountTypeRepository;
import com.example.onekids_project.request.accounttype.AccountTypeCreateRequest;
import com.example.onekids_project.request.accounttype.AccountTypeUpdateRequest;
import com.example.onekids_project.response.accounttype.AccountTypeDetailReponse;
import com.example.onekids_project.response.accounttype.AccountTypeResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.AccountTypeService;
import com.example.onekids_project.validate.CommonValidate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AccountTypeServiceImpl implements AccountTypeService {
    @Autowired
    private ListMapper listMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AccountTypeRepository accountTypeRepository;

    @Override
    public List<AccountTypeResponse> findAccountType(UserPrincipal principal) {
        CommonValidate.checkExistIdSchoolInPrinciple(principal);
        List<AccountType> accountTypeList = accountTypeRepository.findByIdSchool(principal.getIdSchoolLogin());
        List<AccountTypeResponse> responseList = listMapper.mapList(accountTypeList, AccountTypeResponse.class);
        return responseList;
    }

    @Override
    public AccountTypeDetailReponse findAccountTypeById(UserPrincipal principal, Long id) {
        CommonValidate.checkExistIdSchoolInPrinciple(principal);
        AccountType accountType = accountTypeRepository.findById(id).orElseThrow();
        return modelMapper.map(accountType, AccountTypeDetailReponse.class);
    }

    @Override
    public boolean createAccountType(UserPrincipal principal, AccountTypeCreateRequest request) {
        CommonValidate.checkExistIdSchoolInPrinciple(principal);
        AccountType accountType = modelMapper.map(request, AccountType.class);
        accountType.setIdSchool(principal.getIdSchoolLogin());
        accountTypeRepository.save(accountType);
        return true;
    }


    @Override
    public boolean updateAccountType(UserPrincipal principal, AccountTypeUpdateRequest request) {
        CommonValidate.checkExistIdSchoolInPrinciple(principal);
        AccountType accountType = accountTypeRepository.findById(request.getId()).orElseThrow();
        modelMapper.map(request, accountType);
        accountTypeRepository.save(accountType);
        return true;
    }

    @Override
    public boolean deleteAccountTypeById(UserPrincipal principal, Long id) {
        CommonValidate.checkExistIdSchoolInPrinciple(principal);
        AccountType accountType = accountTypeRepository.findById(id).orElseThrow();
        if (accountType.getInfoEmployeeSchoolList().size() > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MessageWebConstant.DELETE_ACCOUNT_TYPE_FAIL);
        }
        accountTypeRepository.deleteById(accountType.getId());
        return true;
    }
}
