package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.FinanceConstant;
import com.example.onekids_project.entity.parent.WalletParentHistory;
import com.example.onekids_project.repository.repositorycustom.WalletParentHistoryRepositoryCustom;
import com.example.onekids_project.request.finance.wallet.SearchWalletParentHistoryRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * date 2021-02-25 08:57
 *
 * @author lavanviet
 */
public class WalletParentHistoryRepositoryImpl extends BaseRepositoryimpl<WalletParentHistory> implements WalletParentHistoryRepositoryCustom {
    @Override
    public List<WalletParentHistory> searchWalletParentHistory(SearchWalletParentHistoryRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.setWalletParentHistory(queryStr, mapParams, request);
        return findAllWebPaging(queryStr.toString(), mapParams, request.getPageNumber(), request.getMaxPageItem());
    }

    @Override
    public List<WalletParentHistory> searchWalletParentHistoryFalse(Long idWalletParent) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_wallet_parent=:idWalletParent ");
        mapParams.put("idWalletParent", idWalletParent);
        queryStr.append("and confirm=:confirm ");
        mapParams.put("confirm", AppConstant.APP_FALSE);
        queryStr.append("order by id desc ");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public long countWalletParentHistory(SearchWalletParentHistoryRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.setWalletParentHistory(queryStr, mapParams, request);
        return countAll(queryStr.toString(), mapParams);
    }

    @Override
    public long countWalletParentHistoryFalse(Long idWalletParent) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_wallet_parent=:idWalletParent ");
        mapParams.put("idWalletParent", idWalletParent);
        queryStr.append("and confirm=:confirm ");
        mapParams.put("confirm", AppConstant.APP_FALSE);
        return countAll(queryStr.toString(), mapParams);
    }

    @Override
    public List<WalletParentHistory> getWalletParentHistory(Long idWallet, int year) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_wallet_parent=:idWalletParent ");
        mapParams.put("idWalletParent", idWallet);
        queryStr.append("and year(date)=:year ");
        mapParams.put("year", year);
        queryStr.append("order by id desc ");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<WalletParentHistory> searchWalletParentHistory(Long idWallet, int year, String description) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_wallet_parent=:idWalletParent ");
        mapParams.put("idWalletParent", idWallet);
        queryStr.append("and year(date)=:year ");
        mapParams.put("year", year);
        if (StringUtils.isNotBlank(description)) {
            queryStr.append("and description like :description ");
            mapParams.put("description", "%" + description + "%");
        }
        queryStr.append("order by id desc ");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<WalletParentHistory> searchWalletParentHistoryChart(Long idSchool, int year, Boolean confirm) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and year(date)=:year ");
        mapParams.put("year", year);
        if (confirm != null) {
            queryStr.append("and confirm=:confirm ");
            mapParams.put("confirm", confirm);
        }
        queryStr.append("and EXISTS (SELECT * FROM wallet_parent as model1 WHERE model.id_wallet_parent = model1.id and del_active=true and id_school=:idSchool) ");
        mapParams.put("idSchool", idSchool);
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public long countWalletParentHistoryChart(Long idSchool, int month, int year, Boolean confirm) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and year(date)=:year ");
        mapParams.put("year", year);
        queryStr.append("and month(date)=:month ");
        mapParams.put("month", month);
        if (confirm != null) {
            queryStr.append("and confirm=:confirm ");
            mapParams.put("confirm", confirm);
        }
        queryStr.append("and EXISTS (SELECT * FROM wallet_parent as model1 WHERE model.id_wallet_parent = model1.id and del_active=true and id_school=:idSchool) ");
        mapParams.put("idSchool", idSchool);
        return countAll(queryStr.toString(), mapParams);
    }

    @Override
    public List<WalletParentHistory> searchWalletForPlus(Long idWallet, String status, int pageNumber) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_wallet_parent=:idWalletParent ");
        mapParams.put("idWalletParent", idWallet);
        if (StringUtils.isNotBlank(status)) {
            if (status.equals(FinanceConstant.WALLET_CONFIRM)) {
                queryStr.append("and confirm=true ");
            } else if (status.equals(FinanceConstant.WALLET_UNCONFIRM)) {
                queryStr.append("and confirm=false ");
            } else if (status.equals(FinanceConstant.WALLET_UNCONFIRM_SCHOOL)) {
                queryStr.append("and confirm=false ");
                queryStr.append("and type=:type and category=:category ");
                mapParams.put("type", FinanceConstant.WALLET_TYPE_PARENT);
                mapParams.put("category", FinanceConstant.CATEGORY_IN);
            } else if (status.equals(FinanceConstant.WALLET_UNCONFIRM_PARENT)) {
                queryStr.append("and confirm=false ");
                queryStr.append("and type=:type and category=:category ");
                mapParams.put("type", FinanceConstant.WALLET_TYPE_SCHOOL);
                mapParams.put("category", FinanceConstant.CATEGORY_OUT);
            }
        }
        queryStr.append("order by date desc ");
        return findAllMobilePaging(queryStr.toString(), mapParams, pageNumber);
    }

    private void setWalletParentHistory(StringBuilder queryStr, Map<String, Object> mapParams, SearchWalletParentHistoryRequest request) {
        queryStr.append("and id_wallet_parent=:idWalletParent ");
        mapParams.put("idWalletParent", request.getIdWalletParent());
        if (CollectionUtils.isNotEmpty(request.getDateList())) {
            LocalDate startDate = request.getDateList().get(0);
            LocalDate endDate = request.getDateList().get(1);
            queryStr.append("and date>=:startDate and date<=:endDate ");
            mapParams.put("startDate", startDate);
            mapParams.put("endDate", endDate);
        }
        if (StringUtils.isNotBlank(request.getCategory())) {
            queryStr.append("and category=:category ");
            mapParams.put("category", request.getCategory());
        }
        if (StringUtils.isNotBlank(request.getType())) {
            queryStr.append("and type=:type ");
            mapParams.put("type", request.getType());
        }
        if (request.getStatus() != null) {
            queryStr.append("and confirm=:confirm ");
            mapParams.put("confirm", request.getStatus());
        }
        queryStr.append("order by id desc ");
    }
}
