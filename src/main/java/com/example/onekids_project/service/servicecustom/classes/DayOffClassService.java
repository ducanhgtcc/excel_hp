package com.example.onekids_project.service.servicecustom.classes;

import com.example.onekids_project.request.classes.DayOffClassManyRequest;
import com.example.onekids_project.request.classes.DayOffClassRequest;
import com.example.onekids_project.request.classes.DayOffClassUpdateRequest;
import com.example.onekids_project.request.classes.SearchDayOffClassRequest;
import com.example.onekids_project.response.classes.DayOffClassResponse;
import com.example.onekids_project.security.model.UserPrincipal;

import java.util.List;

/**
 * date 2021-05-05 14:03
 *
 * @author lavanviet
 */
public interface DayOffClassService {
    void createDayOffClass(DayOffClassRequest request);

    void createDayOffClassMany(DayOffClassManyRequest request);

    List<DayOffClassResponse> getDayOffClassYear(Long idClass, SearchDayOffClassRequest request);

    void updateDayOffClassYear(UserPrincipal principal, DayOffClassUpdateRequest request);

    void deleteDayOffClassYear(UserPrincipal principal, Long id);
    void deleteDayOffClassYearList(UserPrincipal principal, List<Long> idList);

    List<DayOffClassResponse> getDayOffClassView(UserPrincipal principal, Long idClass);


}
