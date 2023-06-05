package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.request.system.IconTeacherConfigRequest;
import com.example.onekids_project.response.school.ListAppIconTeacherResponse;
import com.example.onekids_project.response.system.IconTeacherConfigResponse;

public interface AppIconTeacherService {
    /**
     * tìm kiếm icon teacher
     *
     * @param idSchool
     * @return
     */
    ListAppIconTeacherResponse getAppIconTeacher(Long idSchool);

    /**
     * tạo appIconTeacher
     *
     * @param idSchool
     * @return
     */
    boolean createAppIconTeacher(Long idSchool);

    /**
     * find icon by id school
     *
     * @param idSchool
     * @return
     */
    IconTeacherConfigResponse findIconByIdSchoolConfig(Long idSchool);

    /**
     * update icon parent
     *
     * @param id
     * @return
     */
    IconTeacherConfigResponse updateIconSchool(Long id, IconTeacherConfigRequest iconTeacherConfigRequest);
}
