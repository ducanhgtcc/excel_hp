package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.user.SmsReceiversCustom;
import com.example.onekids_project.mobile.plus.request.historyNotifiRequest.DetailSmsRequest;
import com.example.onekids_project.repository.repositorycustom.SmsReceiverCusstomRepositoryCustom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class smsReceiverCusstomRepositoryImpl extends BaseRepositoryimpl<SmsReceiversCustom> implements SmsReceiverCusstomRepositoryCustom {


    @Override
    public List<SmsReceiversCustom> searchSmsCustomnew(Long idSchool, Long idSmsSendCustom, DetailSmsRequest request) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("and id_send_custom=:idSmsSendCustom ");
        mapParams.put("idSmsSendCustom", idSmsSendCustom);
        queryStr.append("order by created_date desc");
        return findAllMobilePaging(queryStr.toString(), mapParams, request.getPageNumber());
    }
}

