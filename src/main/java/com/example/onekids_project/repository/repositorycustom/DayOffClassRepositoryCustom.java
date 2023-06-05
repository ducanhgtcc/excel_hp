package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.classes.DayOffClass;
import com.example.onekids_project.request.classes.SearchDayOffClassRequest;

import java.util.List;

/**
 * date 2021-05-05 14:44
 *
 * @author lavanviet
 */
public interface DayOffClassRepositoryCustom {
    List<DayOffClass> getDayOffClassYear(Long idClass, SearchDayOffClassRequest request);
}
