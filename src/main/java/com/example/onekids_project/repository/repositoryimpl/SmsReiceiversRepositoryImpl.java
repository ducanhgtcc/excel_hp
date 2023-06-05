package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.user.SmsReceivers;
import com.example.onekids_project.mobile.plus.request.historyNotifiRequest.DetailSmsRequest;
import com.example.onekids_project.repository.repositorycustom.SmsReiceriversRepositoryCustom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SmsReiceiversRepositoryImpl extends BaseRepositoryimpl<SmsReceivers> implements SmsReiceriversRepositoryCustom {

    @Override
    public List<SmsReceivers> findByIdSmsSendNew(Long idSchool,Long idSend, DetailSmsRequest request) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("and id_sms_send=:idSmsSend ");
        mapParams.put("idSmsSend", idSend);
        queryStr.append("order by created_date desc");
        return findAllMobilePaging(queryStr.toString(), mapParams, request.getPageNumber());
    }
}
