package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.user.SmsSendCustom;
import com.example.onekids_project.mobile.plus.request.historyNotifiRequest.SearchSmsPlusRequest;
import com.example.onekids_project.repository.repositorycustom.SmsSendCustomRepositoryCustom;
import com.example.onekids_project.request.notifihistory.SearchHistorySmsSendNewtRequest;
import com.example.onekids_project.request.notifihistory.SearchSmsSendCustomRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SmsSendCustomRepositoryImpl extends BaseRepositoryimpl<SmsSendCustom> implements SmsSendCustomRepositoryCustom {


    @Override
    public List<SmsSendCustom> searchSmsCustom(Long idSchool, SearchSmsSendCustomRequest request) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchSmsCustom(idSchool, request, queryStr, mapParams);
        return findAllWebPaging(queryStr.toString(), mapParams, request.getPageNumber(), request.getMaxPageItem());
    }

    @Override
    public long coutSearchSmsCustom(Long idSchool, SearchSmsSendCustomRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchSmsCustom(idSchool, request, queryStr, mapParams);
        return countAll(queryStr.toString(), mapParams);
    }

    @Override
    public List<SmsSendCustom> searchSmsSendCustom(Long idSchool, SearchSmsPlusRequest request) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        if (StringUtils.isNotBlank(request.getContent())) {
            queryStr.append("and send_title like :sendTitle ");
            mapParams.put("sendTitle", "%" + request.getContent() + "%");
        }
        queryStr.append("order by created_date desc");
        return findAllMobilePaging(queryStr.toString(), mapParams, request.getPageNumber());
    }

    private void setSearchSmsCustom(Long idSchool, SearchSmsSendCustomRequest request, StringBuilder queryStr, Map<String, Object> mapParams) {
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        if (!CollectionUtils.isEmpty(request.getDateStartEnd())) {
            queryStr.append("and created_date>=:dateStart and created_date<=:dateEnd ");
            mapParams.put("dateStart", request.getDateStartEnd().get(0));
            mapParams.put("dateEnd", request.getDateStartEnd().get(1));
        }
        queryStr.append("order by created_date desc");
    }
}
