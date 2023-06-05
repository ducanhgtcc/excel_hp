package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.finance.CashInternal.FnPeopleType;
import com.example.onekids_project.request.cashinternal.SeacrhCashInternalRequest;

import java.util.List;

public interface PeopleTypeRepositoryCustom {

    long countSearch(Long idSchool, SeacrhCashInternalRequest request);

    List<FnPeopleType> searchPeopleType(Long idSchool, SeacrhCashInternalRequest request);

    List<FnPeopleType> searchPeopleTypeOther(Long idSchool);
}
