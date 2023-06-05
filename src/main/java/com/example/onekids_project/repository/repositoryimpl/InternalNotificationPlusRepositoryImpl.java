package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.school.InternalNotificationPlus;
import com.example.onekids_project.master.request.notify.SearchInternalNotificationPlus;
import com.example.onekids_project.repository.repositorycustom.InternalNotificationPlusCustomRepository;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * date 2021-08-12 2:34 PM
 *
 * @author nguyễn văn thụ
 */
public class InternalNotificationPlusRepositoryImpl extends BaseRepositoryimpl<InternalNotificationPlus> implements InternalNotificationPlusCustomRepository{
    @Override
    public List<InternalNotificationPlus> findInternalPlus(SearchInternalNotificationPlus request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        if (StringUtils.isNotBlank(request.getTitle())){
            queryStr.append("and title like :title ");
            mapParams.put("title", "%" + request.getTitle() + "%");
        }
        queryStr.append("order by title asc ");
        return findAllWebPaging(queryStr.toString(), mapParams, request.getPageNumber(), request.getMaxPageItem());
    }

    @Override
    public long countInternalPlus(SearchInternalNotificationPlus request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        if (StringUtils.isNotBlank(request.getTitle())){
            queryStr.append("and title like :title ");
            mapParams.put("title", "%" + request.getTitle() + "%");
        }
        queryStr.append("order by title asc ");
        return countAll(queryStr.toString(), mapParams);
    }
}
