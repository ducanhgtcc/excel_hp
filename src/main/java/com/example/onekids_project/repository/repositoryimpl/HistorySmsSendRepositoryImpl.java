package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.user.SmsReceivers;
import com.example.onekids_project.repository.repositorycustom.HistorySmsSendRepositoryCustom;
import com.example.onekids_project.request.notifihistory.SearchHistorySmsSendNewtRequest;
import com.example.onekids_project.request.parentdiary.SearchMessageParentRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistorySmsSendRepositoryImpl extends BaseRepositoryimpl<SmsReceivers> implements HistorySmsSendRepositoryCustom {


    @Override
    public List<SmsReceivers> searchHistorySmsSend(Long idSchool, SearchHistorySmsSendNewtRequest request) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchMessageParent(idSchool, request, queryStr, mapParams);
        return findAllWebPaging(queryStr.toString(), mapParams, request.getPageNumber(), request.getMaxPageItem());
    }

    @Override
    public long coutSearchSmsSendHistory(Long idSchool, SearchHistorySmsSendNewtRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchMessageParent(idSchool, request, queryStr, mapParams);
        return countAll(queryStr.toString(), mapParams);
    }


    private void setSearchMessageParent(Long idSchool, SearchHistorySmsSendNewtRequest request, StringBuilder queryStr, Map<String, Object> mapParams){
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        if (request != null) {
            if (!CollectionUtils.isEmpty(request.getDateStartEnd())) {
                queryStr.append("and created_date>=:dateStart and created_date<=:dateEnd ");
                mapParams.put("dateStart", request.getDateStartEnd().get(0));
                mapParams.put("dateEnd", request.getDateStartEnd().get(1));
            }
        }
//        if (StringUtils.isNotBlank(request.getTypeSend())) {
//            queryStr.append("and send_type like :sendType ");
//            mapParams.put("sendType", "%" + request.getTypeSend() + "%");
//        }
        queryStr.append("order by created_date desc");
    }
}
