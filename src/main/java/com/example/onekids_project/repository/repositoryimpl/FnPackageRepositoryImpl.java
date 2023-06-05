package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.finance.fees.FnPackage;
import com.example.onekids_project.repository.repositorycustom.FnPackageRepositoryCustom;
import com.example.onekids_project.request.finance.PackageSearchRequest;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FnPackageRepositoryImpl extends BaseRepositoryimpl<FnPackage> implements FnPackageRepositoryCustom {
    @Override
    public List<FnPackage> searchPackage(Long idSchool, PackageSearchRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        if (StringUtils.isNotBlank(request.getName())) {
            queryStr.append("and lower(name) like lower(:name) ");
            mapParams.put("name", "%" + request.getName().trim() + "%");
        }
        if (Objects.nonNull(request.getUsingStatus())) {
            queryStr.append("and using_status =:usingStatus ");
            mapParams.put("usingStatus", request.getUsingStatus());
        }
        if (StringUtils.isNotBlank(request.getCategory())) {
            queryStr.append("and category =:category ");
            mapParams.put("category", request.getCategory());
        }
        queryStr.append("and root_status is false ");
        queryStr.append("order by sort_number ");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<FnPackage> getPackageExcludePackageDefaultKid(Long idSchool, Long idKid) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("and id not in(SELECT id_package FROM fn_kids_package_default where id_kid=:idKid) ");
        mapParams.put("idKid", idKid);
        queryStr.append("and root_status is false ");
        queryStr.append("order by sort_number ");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<FnPackage> getPackageExcludePackageDefaultAndKidsPackage(Long idSchool, Long idKid, int month, int year) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("and id not in(SELECT id_package FROM fn_kids_package_default where id_kid=:idKid) and id not in(SELECT id_package FROM fn_kids_package where id_kid=:idKid and year=:year and month=:month and del_active=true) ");
        mapParams.put("idKid", idKid);
        mapParams.put("year", year);
        mapParams.put("month", month);
        queryStr.append("and root_status is false ");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<FnPackage> getPackageExcludePackageDefaultAndKidsPackageForAdd(List<Long> idList, Long idSchool, Long idKid, int month, int year) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id in :idList ");
        mapParams.put("idList", idList);
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("and id not in(SELECT id_package FROM fn_kids_package_default where id_kid=:idKid) and id not in(SELECT id_package FROM fn_kids_package where id_kid=:idKid and year=:year and month=:month and del_active=true) ");
        mapParams.put("idKid", idKid);
        mapParams.put("year", year);
        mapParams.put("month", month);
        queryStr.append("and root_status is false ");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }
}
