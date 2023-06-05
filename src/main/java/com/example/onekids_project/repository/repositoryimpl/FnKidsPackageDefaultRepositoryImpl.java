package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.finance.fees.FnKidsPackageDefault;
import com.example.onekids_project.repository.repositorycustom.FnKidsPackageDefaultRepositoryCustom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FnKidsPackageDefaultRepositoryImpl extends BaseRepositoryimpl<FnKidsPackageDefault> implements FnKidsPackageDefaultRepositoryCustom {
    @Override
    public List<FnKidsPackageDefault> getPackageDefaultKids(Long idKid) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_kid=:idKid ");
        mapParams.put("idKid", idKid);
        queryStr.append("order by (select category from fn_package model1 where model1.id=model.id_package), (select id from fn_package model1 where model1.id=model.id_package) ");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }
}
