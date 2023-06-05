package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.request.kids.AppIconPlusRequest;
import com.example.onekids_project.response.school.ListAppIconPlusResponse;

import java.util.List;

public interface AppIconPlusAddSerivce {

    ListAppIconPlusResponse findAppIconPlusAddCreate(Long idSchool);
    /**
     * tìm kiếm icon plus
     *
     * @param idSchool
     * @param infoEmployeeSchool
     * @return
     */
    ListAppIconPlusResponse findAppIconPlusAddUpdate(Long idSchool, Long infoEmployeeSchool);

    /**
     * tạo icon plus cho 1 nhân viên
     *
     * @param idSchool
     * @param infoEmployeeSchool
     * @param appIconPlusRequestList
     * @return
     */
    boolean createAppIconPlusAdd(Long idSchool, InfoEmployeeSchool infoEmployeeSchool, List<AppIconPlusRequest> appIconPlusRequestList);

    /**
     * cập nhật icon nhân viên
     *
     * @param idSchool
     * @param infoEmployeeSchool
     * @param appIconPlusRequestList
     * @return
     */
    boolean updateAppIconPlusAdd(Long idSchool, InfoEmployeeSchool infoEmployeeSchool, List<AppIconPlusRequest> appIconPlusRequestList);

}
