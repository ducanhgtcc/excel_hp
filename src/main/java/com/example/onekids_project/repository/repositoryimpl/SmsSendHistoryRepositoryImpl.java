package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.user.SmsSend;
import com.example.onekids_project.repository.repositorycustom.SmsSendHistoryRepositoryCustom;
import com.example.onekids_project.request.notifihistory.SearchSmsSendHistoryRequest;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SmsSendHistoryRepositoryImpl extends BaseRepositoryimpl<SmsSend> implements SmsSendHistoryRepositoryCustom {



    @Override
    public List<SmsSend> searchSmsSendCustom(SearchSmsSendHistoryRequest searchSmsSendHistoryRequest) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (searchSmsSendHistoryRequest != null) {
            if (!CollectionUtils.isEmpty(searchSmsSendHistoryRequest.getDateStartEnd())) {
                queryStr.append("and time_alarm>=:dateStart and time_alarm<=:dateEnd ");
                mapParams.put("dateStart", searchSmsSendHistoryRequest.getDateStartEnd().get(0));
                mapParams.put("dateEnd", searchSmsSendHistoryRequest.getDateStartEnd().get(1));
            }
        }
        return findAllNoPaging(queryStr.toString(), mapParams);
    }
}
