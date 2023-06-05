package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.finance.CashInternal.FnPeopleType;
import com.example.onekids_project.entity.school.FnBank;
import com.example.onekids_project.request.cashinternal.SeacrhCashInternalRequest;

import java.util.List;

public interface FnBankRepositoryCustom {

    List<FnBank> searchBank(Long idSchool);
}
