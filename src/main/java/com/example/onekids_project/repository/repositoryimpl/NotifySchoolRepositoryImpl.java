package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.school.NotifySchool;
import com.example.onekids_project.mobile.plus.request.notifyschool.SearchNotifySchoolPlusRequest;
import com.example.onekids_project.mobile.request.PageNumberRequest;
import com.example.onekids_project.repository.repositorycustom.NotifySchoolRepositoryCustom;
import com.example.onekids_project.request.notifyschool.SearchNotifySchoolRequest;
import com.example.onekids_project.util.SchoolUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * date 2021-10-21 10:01 AM
 *
 * @author nguyễn văn thụ
 */
public class NotifySchoolRepositoryImpl extends BaseRepositoryimpl<NotifySchool> implements NotifySchoolRepositoryCustom {

    @Override
    public List<NotifySchool> searchNotifySchool(Long idSchool, SearchNotifySchoolRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.setNotifySchool(queryStr, mapParams, request);
        queryStr.append("order by id desc");
        return findAllWebPaging(queryStr.toString(), mapParams, request.getPageNumber(), request.getMaxPageItem());
    }

    @Override
    public long countNotifySchool(Long idSchool, SearchNotifySchoolRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.setNotifySchool(queryStr, mapParams, request);
        return countAll(queryStr.toString(), mapParams);
    }

    private void setNotifySchool(StringBuilder queryStr, Map<String, Object> mapParams, SearchNotifySchoolRequest request) {
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", SchoolUtils.getIdSchool());
        if (request.getActive() != null) {
            queryStr.append("and active=:active ");
            mapParams.put("active", request.getActive());
        }
        if (StringUtils.isNotBlank(request.getTitle())) {
            queryStr.append("and title=:title ");
            mapParams.put("title", request.getTitle().trim());
        }
        if (StringUtils.isNotBlank(request.getContent())) {
            queryStr.append("and content=:content ");
            mapParams.put("content", request.getContent().trim());
        }
        if (CollectionUtils.isNotEmpty(request.getDateList())) {
            queryStr.append("and date(created_date)>=:startDate and date(created_date)<=:endDate ");
            mapParams.put("startDate", request.getDateList().get(0));
            mapParams.put("endDate", request.getDateList().get(1));
        }
    }

    @Override
    public List<NotifySchool> searchNotifySchoolParentMobile(Long idSchool, PageNumberRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("and active=:active ");
        mapParams.put("active", AppConstant.APP_TRUE);
        queryStr.append("order by created_date desc");
        return findAllMobilePaging(queryStr.toString(), mapParams, request.getPageNumber());
    }

    @Override
    public List<NotifySchool> searchNotifySchoolTeacherMobile(Long idSchool, PageNumberRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("and active=:active ");
        mapParams.put("active", AppConstant.APP_TRUE);
        queryStr.append("order by created_date desc");
        return findAllMobilePaging(queryStr.toString(), mapParams, request.getPageNumber());
    }

    @Override
    public List<NotifySchool> searchNotifySchoolPlusMobile(Long idSchool, SearchNotifySchoolPlusRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        if (request.getActive() != null) {
            queryStr.append("and active=:active ");
            mapParams.put("active", request.getActive());
        }
        if (request.getStartDate() != null) {
            queryStr.append("and date(created_date)>=:startDate ");
            mapParams.put("startDate", request.getStartDate());
        }
        if (request.getEndDate() != null) {
            queryStr.append("and date(created_date)<=:endDate ");
            mapParams.put("endDate", request.getEndDate());
        }
        queryStr.append("order by created_date desc");
        return findAllMobilePaging(queryStr.toString(), mapParams, request.getPageNumber());
    }
}
