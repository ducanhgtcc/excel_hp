package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.classes.DayOffClass;
import com.example.onekids_project.repository.repositorycustom.DayOffClassRepositoryCustom;
import com.example.onekids_project.request.classes.SearchDayOffClassRequest;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * date 2021-05-05 13:58
 *
 * @author lavanviet
 */
public class DayOffClassRepositoryImpl extends BaseRepositoryimpl<DayOffClass> implements DayOffClassRepositoryCustom {
    @Override
    public List<DayOffClass> getDayOffClassYear(Long idClass, SearchDayOffClassRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_class=:idClass ");
        mapParams.put("idClass", idClass);
        if (request.getYear() != null) {
            queryStr.append("and year(date)=:year ");
            mapParams.put("year", request.getYear());
        }
        if (request.getDate() != null) {
            queryStr.append("and date=:date ");
            mapParams.put("date", request.getDate());
        }
        if (StringUtils.isNotBlank(request.getNote())) {
            queryStr.append("and note like :note ");
            mapParams.put("note", "%" + request.getNote() + "%");
        }
        queryStr.append("order by date desc ");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }
}
