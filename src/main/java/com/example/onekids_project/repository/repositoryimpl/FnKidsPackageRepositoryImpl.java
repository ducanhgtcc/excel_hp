package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.finance.fees.FnKidsPackage;
import com.example.onekids_project.repository.repositorycustom.FnKidsPackageRepositoryCustom;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FnKidsPackageRepositoryImpl extends BaseRepositoryimpl<FnKidsPackage> implements FnKidsPackageRepositoryCustom {
    @Override
    public List<FnKidsPackage> getKidsPackageForKid(Long idSchool, Long idKid, int month, int year) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and exists(select * from ma_kids as model1 where model.id_kid=model1.id and id_school=:idSchool) ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("and id_kid=:idKid ");
        mapParams.put("idKid", idKid);
        queryStr.append("and month=:month ");
        mapParams.put("month", month);
        queryStr.append("and year=:year ");
        mapParams.put("year", year);
        queryStr.append("order by (select category from fn_package model1 where model1.id=model.id_package), (select id from fn_package model1 where model1.id=model.id_package) ");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<FnKidsPackage> searchKidsPackageParent(Long idKid, int year) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_kid=:idKid ");
        mapParams.put("idKid", idKid);
        queryStr.append("and year=:year ");
        mapParams.put("year", year);
        queryStr.append("and approved=true ");
        queryStr.append("order by month desc ");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<FnKidsPackage> searchKidsPackageFlowPackage(Long idPackage, LocalDate date) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_package=:idPackage ");
        mapParams.put("idPackage", idPackage);
        if (date != null) {
            queryStr.append("and month=:month ");
            mapParams.put("month", date.getMonthValue());
            queryStr.append("and year=:year ");
            mapParams.put("year", date.getYear());
        } else {
            queryStr.append("order by year, month desc ");
        }
        return findAllNoPaging(queryStr.toString(), mapParams);
    }
}
