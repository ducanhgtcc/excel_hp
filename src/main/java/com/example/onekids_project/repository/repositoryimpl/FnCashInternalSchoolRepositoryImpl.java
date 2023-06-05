package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.FinanceConstant;
import com.example.onekids_project.entity.finance.CashInternal.FnCashInternal;
import com.example.onekids_project.mobile.plus.request.cashinternal.CashInternalPlusRequest;
import com.example.onekids_project.repository.repositorycustom.FnCashInternalSchoolRepositoryCustom;
import com.example.onekids_project.request.cashinternal.SeacrhListpayRequest;
import com.example.onekids_project.request.cashinternal.SearchPayDateMonth;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FnCashInternalSchoolRepositoryImpl extends BaseRepositoryimpl<FnCashInternal> implements FnCashInternalSchoolRepositoryCustom {
    @Override
    public List<FnCashInternal> searchInternalPay(Long idSchool, SeacrhListpayRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchInternalPay(idSchool, request, queryStr, mapParams);
        return findAllWebPaging(queryStr.toString(), mapParams, request.getPageNumber(), request.getMaxPageItem());
    }

    @Override
    public List<FnCashInternal> getInternalPayTotal(Long idSchool, SearchPayDateMonth request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchInternalPayTotal(idSchool, request, queryStr, mapParams);
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<FnCashInternal> getInternalPayTotalNotify(Long idSchool, LocalDate endDate) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.setNotifyInternalPayTotal(idSchool, endDate, queryStr, mapParams);
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public long countSearchInternalPay(Long idSchool, SeacrhListpayRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchInternalPay(idSchool, request, queryStr, mapParams);
        return countAll(queryStr.toString(), mapParams);
    }

    @Override
    public List<FnCashInternal> searchByIdSchoolandCategory(Long idSchool) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("and category=:category ");
        mapParams.put("category", FinanceConstant.CATEGORY_OUT);
        queryStr.append("order by id desc");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<FnCashInternal> searchcollectCash(Long idSchool, SeacrhListpayRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchColletCash(idSchool, request, queryStr, mapParams);
        return findAllWebPaging(queryStr.toString(), mapParams, request.getPageNumber(), request.getMaxPageItem());
    }

    @Override
    public long countSearchCollectCash(Long idSchool, SeacrhListpayRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchColletCash(idSchool, request, queryStr, mapParams);
        return countAll(queryStr.toString(), mapParams);
    }

    @Override
    public List<FnCashInternal> searchByIdSchoolandCategoryin(Long idSchool) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("and category=:category ");
        mapParams.put("category", FinanceConstant.CATEGORY_IN);
        queryStr.append("order by id desc");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<FnCashInternal> searchCashInternalStartEndMonth(Long idSchool, int startMonth, int endMonth, int year) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("and payment=true ");
        queryStr.append("and month(date)>=:startMonth and month(date)<=:endMonth and year(date)=:year ");
        mapParams.put("startMonth", startMonth);
        mapParams.put("endMonth", endMonth);
        mapParams.put("year", year);

        queryStr.append("order by id desc");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<FnCashInternal> searchCashInternalMonth(Long idSchool, int month, int year) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("and payment=true ");
        queryStr.append("and month(date)=:month and year(date)=:year ");
        mapParams.put("month", month);
        mapParams.put("year", year);
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<FnCashInternal> getCashInternalPlus(Long idSchool, CashInternalPlusRequest request, String category) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.getCashInternalPlus(queryStr, mapParams, idSchool, request, category);
        queryStr.append("order by id desc");
        return findAllMobilePaging(queryStr.toString(), mapParams, request.getPageNumber());
    }

    private void getCashInternalPlus(StringBuilder queryStr, Map<String, Object> mapParams, Long idSchool, CashInternalPlusRequest request, String category) {
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("and category=:category ");
        mapParams.put("category", category);
        queryStr.append("and canceled=false ");
        if (request.getStartDate() != null) {
            queryStr.append("and date>=:startDate ");
            mapParams.put("startDate", request.getStartDate());
        }
        if (request.getEndDate() != null) {
            queryStr.append("and date<=:endDate ");
            mapParams.put("endDate", request.getEndDate());
        }
        if (request.getApproved() != null) {
            queryStr.append("and approved=:approved ");
            mapParams.put("approved", request.getApproved());
        }
        if (StringUtils.isNotBlank(request.getCode())) {
            queryStr.append("and code=:code ");
            mapParams.put("code", request.getCode());
        }
    }


    private void setSearchColletCash(Long idSchool, SeacrhListpayRequest request, StringBuilder queryStr, Map<String, Object> mapParams) {
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("and category=:category ");
        mapParams.put("category", FinanceConstant.CATEGORY_IN);
        if (!CollectionUtils.isEmpty(request.getDateStartEnd())) {
            queryStr.append("and created_date>=:dateStart and created_date<=:dateEnd ");
            mapParams.put("dateStart", request.getDateStartEnd().get(0));
            mapParams.put("dateEnd", request.getDateStartEnd().get(1).plusDays(1));
        }
        if (StringUtils.isNotBlank(request.getStatus())) {
            if (AppConstant.APPROVE_TRUE.equals(request.getStatus()) || AppConstant.APPROVE_FALSE.equals(request.getStatus())) {
                queryStr.append("and approved=:approved ");
                if (AppConstant.APPROVE_TRUE.equals(request.getStatus())) {
                    mapParams.put("approved", AppConstant.APP_TRUE);
                } else if (AppConstant.APPROVE_FALSE.equals(request.getStatus())) {
                    mapParams.put("approved", AppConstant.APP_FALSE);
                }
                queryStr.append("and canceled=:canceled ");
                mapParams.put("canceled", AppConstant.APP_FALSE);
            }
            if (AppConstant.PAY_TRUE.equals(request.getStatus()) || AppConstant.PAY_FALSE.equals(request.getStatus())) {
                queryStr.append("and payment=:payment ");
                if (AppConstant.PAY_TRUE.equals(request.getStatus())) {
                    mapParams.put("payment", AppConstant.APP_TRUE);
                } else if (AppConstant.PAY_FALSE.equals(request.getStatus())) {
                    mapParams.put("payment", AppConstant.APP_FALSE);
                }
                queryStr.append("and canceled=:canceled ");
                mapParams.put("canceled", AppConstant.APP_FALSE);
            }
            if (AppConstant.APPROVE_DELL.equals(request.getStatus())) {
                queryStr.append("and canceled=:canceled ");
                if (AppConstant.APPROVE_DELL.equals(request.getStatus())) {
                    mapParams.put("canceled", AppConstant.APP_TRUE);
                } else {
                    mapParams.put("canceled", AppConstant.APP_FALSE);
                }
            }
        } else {
            queryStr.append("and canceled=:canceled ");
            mapParams.put("canceled", AppConstant.APP_FALSE);
        }
        if (StringUtils.isNotBlank(request.getCode())) {
            queryStr.append("and code like :code ");
            mapParams.put("code", "%" + request.getCode() + "%");
        }
        queryStr.append("order by id desc");
    }

    private void setSearchInternalPay(Long idSchool, SeacrhListpayRequest request, StringBuilder queryStr, Map<String, Object> mapParams) {
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("and category=:category ");
        mapParams.put("category", FinanceConstant.CATEGORY_OUT);
        if (!CollectionUtils.isEmpty(request.getDateStartEnd())) {
            queryStr.append("and created_date>=:dateStart and created_date<=:dateEnd ");
            mapParams.put("dateStart", request.getDateStartEnd().get(0));
            mapParams.put("dateEnd", request.getDateStartEnd().get(1).plusDays(1));
        }
        if (StringUtils.isNotBlank(request.getStatus())) {
            if (AppConstant.APPROVE_TRUE.equals(request.getStatus()) || AppConstant.APPROVE_FALSE.equals(request.getStatus())) {
                queryStr.append("and approved=:approved ");
                if (AppConstant.APPROVE_TRUE.equals(request.getStatus())) {
                    mapParams.put("approved", AppConstant.APP_TRUE);
                } else if (AppConstant.APPROVE_FALSE.equals(request.getStatus())) {
                    mapParams.put("approved", AppConstant.APP_FALSE);
                }
                queryStr.append("and canceled=:canceled ");
                mapParams.put("canceled", AppConstant.APP_FALSE);
            }
            if (AppConstant.PAY_TRUE.equals(request.getStatus()) || AppConstant.PAY_FALSE.equals(request.getStatus())) {
                queryStr.append("and payment=:payment ");
                if (AppConstant.PAY_TRUE.equals(request.getStatus())) {
                    mapParams.put("payment", AppConstant.APP_TRUE);
                } else if (AppConstant.PAY_FALSE.equals(request.getStatus())) {
                    mapParams.put("payment", AppConstant.APP_FALSE);
                }
                queryStr.append("and canceled=:canceled ");
                mapParams.put("canceled", AppConstant.APP_FALSE);
            }
            if (AppConstant.APPROVE_DELL.equals(request.getStatus())) {
                queryStr.append("and canceled=:canceled ");
                if (AppConstant.APPROVE_DELL.equals(request.getStatus())) {
                    mapParams.put("canceled", AppConstant.APP_TRUE);
                } else {
                    mapParams.put("canceled", AppConstant.APP_FALSE);
                }
            }
        } else {
            queryStr.append("and canceled=:canceled ");
            mapParams.put("canceled", AppConstant.APP_FALSE);
        }
        if (StringUtils.isNotBlank(request.getCode())) {
            queryStr.append("and code like :code ");
            mapParams.put("code", "%" + request.getCode() + "%");
        }
        queryStr.append("order by id desc");
    }

    private void setSearchInternalPayTotal(Long idSchool, SearchPayDateMonth request, StringBuilder queryStr, Map<String, Object> mapParams) {
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("and payment=:payment ");
        mapParams.put("payment", AppConstant.APP_TRUE);
        queryStr.append("and canceled=:canceled ");
        mapParams.put("canceled", AppConstant.APP_FALSE);
        queryStr.append("and approved=:approved ");
        mapParams.put("approved", AppConstant.APP_TRUE);
        int year = LocalDate.now().getYear();
        LocalDate startDate;
        LocalDate endDate;
        String type = request.getType();
        if (FinanceConstant.TYPE_MONTH.equals(type) && request.getMonth() != null) {
            startDate = LocalDate.of(year, request.getMonth(), 1);
            endDate = startDate.plusMonths(1).minusDays(1);
        } else if (FinanceConstant.TYPE_DATE.equals(type) && !CollectionUtils.isEmpty(request.getDateList())) {
            startDate = request.getDateList().get(0);
            endDate = request.getDateList().get(1);
        } else {
            startDate = LocalDate.of(year, 1, 1);
            endDate = startDate.plusYears(1).minusDays(1);
        }
        queryStr.append("and date>=:startDate and date<=:endDate ");
        mapParams.put("startDate", startDate);
        mapParams.put("endDate", endDate);
        queryStr.append("order by id desc");
    }

    private void setNotifyInternalPayTotal(Long idSchool, LocalDate endDate, StringBuilder queryStr, Map<String, Object> mapParams) {
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("and payment=:payment ");
        mapParams.put("payment", AppConstant.APP_TRUE);
        queryStr.append("and canceled=:canceled ");
        mapParams.put("canceled", AppConstant.APP_FALSE);
        queryStr.append("and approved=:approved ");
        mapParams.put("approved", AppConstant.APP_TRUE);
        int year = LocalDate.now().getYear();
        LocalDate startDate = LocalDate.of(year, 1, 1);
        queryStr.append("and date>=:startDate and date<=:endDate ");
        mapParams.put("startDate", startDate);
        mapParams.put("endDate", endDate);
        queryStr.append("order by id desc");
    }
}
