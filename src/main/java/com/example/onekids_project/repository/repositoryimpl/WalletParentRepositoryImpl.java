package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.common.FinanceConstant;
import com.example.onekids_project.entity.parent.WalletParent;
import com.example.onekids_project.repository.repositorycustom.WalletParentRepositoryCustom;
import com.example.onekids_project.request.finance.wallet.WalletParentStatisticalRequest;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * date 2021-02-22 10:09
 *
 * @author lavanviet
 */
public class WalletParentRepositoryImpl extends BaseRepositoryimpl<WalletParent> implements WalletParentRepositoryCustom {
    @Override
    public List<WalletParent> searchWalletParentStatistical(Long idSchool, WalletParentStatisticalRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.setParentStatistical(idSchool, request, queryStr, mapParams);
        return findAllWebPaging(queryStr.toString(), mapParams, request.getPageNumber(), request.getMaxPageItem());
    }

    @Override
    public long countWalletParentStatistical(Long idSchool, WalletParentStatisticalRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.setParentStatistical(idSchool, request, queryStr, mapParams);
        return countAll(queryStr.toString(), mapParams);
    }

    private void setParentStatistical(Long idSchool, WalletParentStatisticalRequest request, StringBuilder queryStr, Map<String, Object> mapParams) {
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        if (StringUtils.isNotBlank(request.getCode())) {
            queryStr.append("and lower(code) like lower(:code) ");
            mapParams.put("code", "%" + request.getCode().trim() + "%");
        }
        if (StringUtils.isNotBlank(request.getType())) {
            String type = request.getType();
            if (type.equals(FinanceConstant.TYPE_POSITIVE)) {
                queryStr.append("and money_in-money_out>0 ");
            } else if (type.equals(FinanceConstant.TYPE_ZERO)) {
                queryStr.append("and money_in-money_out=0 ");
            }
        }
        if (StringUtils.isNotBlank(request.getKidName())) {
            queryStr.append("and exists (select * from ma_parent as model1 where model1.id =  model.id_parent and  exists (select * from ma_kids as model2 where model2.id_parent =  model1.id and lower(full_name) like lower(:kidName))) ");
            mapParams.put("kidName", "%" + request.getKidName().trim() + "%");
        }
        queryStr.append("order by (money_in-money_out) desc ");

    }
}
