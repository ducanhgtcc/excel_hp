package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.request.system.IconParentConfigRequest;
import com.example.onekids_project.response.school.ListAppIconResponse;
import com.example.onekids_project.response.system.IconParentConfigResponse;

public interface AppIconParentService {
    /**
     * tìm kiếm icon parent
     *
     * @param idSchool
     * @return
     */
    ListAppIconResponse getAppIconParent(Long idSchool);

    /**
     * tạo appIconParent
     *
     * @param idSchool
     * @return
     */
    boolean createAppIconParent(Long idSchool);

    /**
     * find icon by id school
     *
     * @param idSchool
     * @return
     */
    IconParentConfigResponse findIconByIdSchoolConfig(Long idSchool);

    /**
     * update icon parent
     *
     * @param id
     * @return
     */
    IconParentConfigResponse updateIconParentShool(Long id, IconParentConfigRequest iconParentConfigRequest);
}
