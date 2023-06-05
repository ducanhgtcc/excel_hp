package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.school.NotificationManage;
import com.example.onekids_project.master.request.notify.SearchNotificationRequest;
import com.example.onekids_project.repository.repositorycustom.NotificationManageRepositoryCustom;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * date 2021-08-02 10:13 AM
 *
 * @author nguyễn văn thụ
 */
public class NotificationManageRepositoryImpl extends BaseRepositoryimpl<NotificationManage> implements NotificationManageRepositoryCustom {
    @Override
    public List<NotificationManage> findNotificationManage(Long idSchool, SearchNotificationRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        if (StringUtils.isNotBlank(request.getType())){
            queryStr.append("and type_receive =:type ");
            mapParams.put("type", request.getType());
        }
        queryStr.append("order by sort_number asc ");
        return findAllWebPaging(queryStr.toString(), mapParams, request.getPageNumber(), request.getMaxPageItem());
    }

    @Override
    public long countNotificationManage(Long idSchool, SearchNotificationRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        if (request.getType() != null){
            queryStr.append("and type_receive =:type ");
            mapParams.put("type", request.getType());
        }
        return countAll(queryStr.toString(), mapParams);
    }
}
