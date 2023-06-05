package com.example.onekids_project.service.servicecustom.config;

import com.example.onekids_project.request.schoolconfig.SchoolConfigRequest;
import com.example.onekids_project.response.schoolconfig.SchoolConfigAttendanceResponse;
import com.example.onekids_project.response.schoolconfig.SchoolConfigResponse;
import com.example.onekids_project.security.model.UserPrincipal;

/**
 * date 2021-05-24 09:42
 *
 * @author lavanviet
 */
public interface SupperPlusConfigService {
    /**
     * tìm kiếm cấu hình nhà trường của người đang đang nhập
     *
     * @param principal
     * @return
     */
    SchoolConfigResponse findSchoolConfigByIdSchool(UserPrincipal principal);
    /**
     * tìm kiếm cấu hình nhà trường của người đang đang nhập cho điểm danh
     *
     * @param principal
     * @return
     */
    SchoolConfigAttendanceResponse findSchoolConfigByIdSchoolByAttendance(UserPrincipal principal);

    /**
     * cập nhật cấu hình chung cho một trường
     *
     * @param schoolConfigRequest
     * @return
     */
    void updateConfigCommon(UserPrincipal principal, SchoolConfigRequest schoolConfigRequest);

    void saveChangeAttendanceConfig(Long idSchool, SchoolConfigRequest schoolConfigRequest);
}
