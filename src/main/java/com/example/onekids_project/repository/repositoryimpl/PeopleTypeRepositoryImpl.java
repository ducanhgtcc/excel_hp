package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.finance.CashInternal.FnPeopleType;
import com.example.onekids_project.repository.repositorycustom.PeopleTypeRepositoryCustom;
import com.example.onekids_project.request.cashinternal.SeacrhCashInternalRequest;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PeopleTypeRepositoryImpl extends BaseRepositoryimpl<FnPeopleType> implements PeopleTypeRepositoryCustom {

    @Override
    public List<FnPeopleType> searchPeopleType(Long idSchool, SeacrhCashInternalRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchObject(idSchool, request, queryStr, mapParams);
        return findAllWebPaging(queryStr.toString(), mapParams, request.getPageNumber(), request.getMaxPageItem());
    }

    @Override
    public List<FnPeopleType> searchPeopleTypeOther(Long idSchool) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("order by default_status desc, type desc");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public long countSearch(Long idSchool, SeacrhCashInternalRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchObject(idSchool, request, queryStr, mapParams);
        return countAll(queryStr.toString(), mapParams);
    }

    private void setSearchObject(Long idSchool, SeacrhCashInternalRequest request, StringBuilder queryStr, Map<String, Object> mapParams) {
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        if (StringUtils.isNotBlank(request.getNameOrPhone())) {
            queryStr.append("and (name like :name ");
            mapParams.put("name", "%" + request.getNameOrPhone().trim() + "%");
            queryStr.append("or phone like :phone) ");
            mapParams.put("phone", "%" + request.getNameOrPhone().trim() + "%");
        }
        if (StringUtils.isNotBlank(request.getType())) {
            queryStr.append("and type=:type ");
            mapParams.put("type", request.getType());
        }
        queryStr.append("order by default_status desc, type desc , name asc ");
//        queryStr.append("order by department_name collate utf8_vietnamese_ci");

    }

}
