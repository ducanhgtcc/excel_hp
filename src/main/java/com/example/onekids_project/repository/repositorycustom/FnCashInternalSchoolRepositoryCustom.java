package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.finance.CashInternal.FnCashInternal;
import com.example.onekids_project.mobile.plus.request.cashinternal.CashInternalPlusRequest;
import com.example.onekids_project.request.cashinternal.SeacrhListpayRequest;
import com.example.onekids_project.request.cashinternal.SearchPayDateMonth;

import java.time.LocalDate;
import java.util.List;

public interface FnCashInternalSchoolRepositoryCustom {

    List<FnCashInternal> searchInternalPay(Long idSchool, SeacrhListpayRequest request);

    List<FnCashInternal> getInternalPayTotal(Long idSchool, SearchPayDateMonth request);
    //Notify
    List<FnCashInternal> getInternalPayTotalNotify(Long idSchool, LocalDate endDate);

    long countSearchInternalPay(Long idSchool, SeacrhListpayRequest request);

    List<FnCashInternal> searchByIdSchoolandCategory(Long idSchool);

    List<FnCashInternal> searchcollectCash(Long idSchool, SeacrhListpayRequest request);

    long countSearchCollectCash(Long idSchool, SeacrhListpayRequest request);

    List<FnCashInternal> searchByIdSchoolandCategoryin(Long idSchool);

    List<FnCashInternal> searchCashInternalStartEndMonth(Long idSchool, int startMonth, int endMonth, int year);

    List<FnCashInternal> searchCashInternalMonth(Long idSchool, int month, int year);

    List<FnCashInternal> getCashInternalPlus(Long idSchool, CashInternalPlusRequest request, String category);

}
