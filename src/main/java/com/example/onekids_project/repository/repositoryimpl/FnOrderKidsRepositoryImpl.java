package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.finance.fees.FnOrderKids;
import com.example.onekids_project.repository.repositorycustom.FnOrderKidsRepositoryCustom;
import com.example.onekids_project.request.finance.order.SearchOrderKidsAllRequest;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * date 2021-02-23 15:20
 *
 * @author lavanviet
 */
public class FnOrderKidsRepositoryImpl extends BaseRepositoryimpl<FnOrderKids> implements FnOrderKidsRepositoryCustom {
    @Override
    public List<FnOrderKids> searchOrderForKids(SearchOrderKidsAllRequest request) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_kid=:idKid ");
        mapParams.put("idKid", request.getIdKid());
        if (request.getYear() != null) {
            queryStr.append("and year=:year ");
            mapParams.put("year", request.getYear());
        }
        queryStr.append("order by year desc, month desc ");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<FnOrderKids> searchOrderKidsYearParent(Long idKid, int year) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_kid=:idKid ");
        mapParams.put("idKid", idKid);
        queryStr.append("and year=:year ");
        mapParams.put("year", year);
//        LocalDate nowDate = LocalDate.now();
//        if (year >= nowDate.getYear()) {
//            queryStr.append("and month<=:month ");
//            mapParams.put("month", nowDate.getMonthValue());
//        }
        queryStr.append("order by month desc ");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<FnOrderKids> findOrderKidsParent(Long idKid) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_kid=:idKid ");
        mapParams.put("idKid", idKid);
        queryStr.append("and view=true ");
        LocalDate nowDate = LocalDate.now();
        queryStr.append("and month<=:month ");
        mapParams.put("month", nowDate.getMonthValue());
        queryStr.append("order by year desc, month desc ");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }
}
