package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.user.SmsSend;
import com.example.onekids_project.mobile.plus.request.historyNotifiRequest.SearchSmsPlusRequest;
import com.example.onekids_project.repository.repositorycustom.SmsSendRepositoryCustom;
import com.example.onekids_project.request.notifihistory.SearchHistorySmsSendNewtRequest;
import com.example.onekids_project.request.notifihistory.SearchSmsSendRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SmsSendRepositoryImpl extends BaseRepositoryimpl<SmsSend> implements SmsSendRepositoryCustom {

    @Override
    public List<SmsSend> searchSmsSend(Long idSchool, SearchSmsSendRequest searchSmsSendRequest) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        if (searchSmsSendRequest != null) {
            if (!CollectionUtils.isEmpty(searchSmsSendRequest.getDateStartEnd())) {
                queryStr.append("and time_alarm>=:dateStart and time_alarm<=:dateEnd ");
                mapParams.put("dateStart", searchSmsSendRequest.getDateStartEnd().get(0));
                mapParams.put("dateEnd", searchSmsSendRequest.getDateStartEnd().get(1));
            }
        }
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<SmsSend> findByStatusSend() {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String dateTimeFt = LocalDateTime.now().format(formatter);
        LocalDateTime date = LocalDateTime.parse(dateTimeFt, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

//        LocalDateTime dat = ;
        queryStr.append("and sent =:statusSent ");
        mapParams.put("statusSent", false);

        queryStr.append("and time_alarm <=:dateTime ");
        mapParams.put("dateTime", date);


        return findAllNoPaging(queryStr.toString(), mapParams);
    }

//    @Override
//    public List<SmsSend> searchHistorySmsSend(Long idSchool, SearchHistorySmsSendNewtRequest request) {
//        return null;
//    }
//
//    @Override
//    public long coutSearchSmsSendHistory(Long idSchool, SearchHistorySmsSendNewtRequest request) {
//        return 0;
//    }

    @Override
    public List<SmsSend> searchHistorySmsSend(Long idSchool, SearchHistorySmsSendNewtRequest request) {
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

    @Override
    public List<SmsSend> searchSmsSendPlus(Long idSchool, SearchSmsPlusRequest request) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        if (StringUtils.isNotBlank(request.getContent())) {
            queryStr.append("and send_content like :sendContent ");
            mapParams.put("sendContent", "%" + request.getContent() + "%");
        }
        queryStr.append("and sent =:statusSent ");
        mapParams.put("statusSent", AppConstant.APP_TRUE);
        queryStr.append("order by created_date desc");
        return findAllMobilePaging(queryStr.toString(), mapParams, request.getPageNumber());
    }


    private void setSearchMessageParent(Long idSchool, SearchHistorySmsSendNewtRequest request, StringBuilder queryStr, Map<String, Object> mapParams) {
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("and sent =:statusSent ");
        mapParams.put("statusSent", true);
        if (!CollectionUtils.isEmpty(request.getDateStartEnd())) {
            queryStr.append("and created_date>=:dateStart and created_date<=:dateEnd ");
            mapParams.put("dateStart", request.getDateStartEnd().get(0));
            mapParams.put("dateEnd", request.getDateStartEnd().get(1));
        }
        queryStr.append("order by created_date desc");
    }

}
