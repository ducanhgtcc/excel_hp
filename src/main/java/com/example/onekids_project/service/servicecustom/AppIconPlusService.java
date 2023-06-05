package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.request.system.IconPlusConfigRequest;
import com.example.onekids_project.response.school.ListAppIconPlusNotifyResponse;
import com.example.onekids_project.response.school.ListAppIconPlusResponse;
import com.example.onekids_project.response.system.IconPlusConfigResponse;

public interface AppIconPlusService {

    /**
     * tạo appIconPlus
     *
     * @param idSchool
     * @return
     */
    boolean createAppIconPlus(Long idSchool);

    /**
     * find icon by id school
     *
     * @param idSchool
     * @return
     */
    IconPlusConfigResponse findIconByIdSchoolConfig(Long idSchool);

    /**
     * update icon
     *
     * @param id
     * @return
     */
    IconPlusConfigResponse updateIconSchool(Long id, IconPlusConfigRequest iconPlusConfigRequest);

    /**
     * create icon notify
     *
     * @param
     * @return
     */

    ListAppIconPlusNotifyResponse getAppIconPlusNotify(Long idSchoolLogin);
}
