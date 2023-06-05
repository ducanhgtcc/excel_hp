package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.request.kids.AppIconTeacherRequest;
import com.example.onekids_project.response.school.ListAppIconTeacherResponse;

import java.util.List;

public interface AppIconTeacherAddSerivce {

    ListAppIconTeacherResponse findAppIconTeacherAddCreate(Long idSchool);

    /**
     * tìm kiếm icon teacher
     *
     * @param idSchool
     * @param infoEmployeeSchool
     * @return
     */
    ListAppIconTeacherResponse findAppIconTeacherAddUpdate(Long idSchool, Long infoEmployeeSchool);

    /**
     * tạo icon teacher cho 1 nhân viên
     *
     * @param idSchool
     * @param infoEmployeeSchool
     * @param appIconTeacherRequestList
     * @return
     */
    boolean createAppIconTeacherAdd(Long idSchool, InfoEmployeeSchool infoEmployeeSchool, List<AppIconTeacherRequest> appIconTeacherRequestList);

    /**
     * cập nhật icon nhân viên
     *
     * @param idSchool
     * @param infoEmployeeSchool
     * @param appIconTeacherRequestList
     * @return
     */
    boolean updateAppIconTeacherAdd(Long idSchool, InfoEmployeeSchool infoEmployeeSchool, List<AppIconTeacherRequest> appIconTeacherRequestList);

}
