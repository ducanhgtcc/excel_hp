package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.request.kids.AppIconParentRequest;
import com.example.onekids_project.response.school.ListAppIconResponse;

import java.util.List;

public interface AppIconParentAddSerivce {


    ListAppIconResponse findAppIconParentAddCreate(Long idSchool);

    /**
     * tìm kiếm icon parent
     *
     * @param idSchool
     * @param idKids
     * @return
     */
    ListAppIconResponse findAppIconParentAddUpdate(Long idSchool, Long idKids);

    /**
     * tạo icon parent cho 1 học sinh
     *
     * @param idSchool
     * @param kid
     * @param appIconParentRequestList
     * @return
     */
    boolean createAppIconParentAdd(Long idSchool, Kids kid, List<AppIconParentRequest> appIconParentRequestList);

    /**
     * cập nhật icon parent
     *
     * @param idSchool
     * @param kid
     * @param appIconParentRequestList
     * @return
     */
    boolean updateAppIconParentAdd(Long idSchool, Kids kid, List<AppIconParentRequest> appIconParentRequestList);

}
