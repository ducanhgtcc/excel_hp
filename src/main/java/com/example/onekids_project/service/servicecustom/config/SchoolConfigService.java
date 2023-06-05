package com.example.onekids_project.service.servicecustom.config;

import com.example.onekids_project.entity.school.SchoolConfig;
import com.example.onekids_project.request.school.ConfigAttendanceEmployeeSchoolRequest;
import com.example.onekids_project.request.school.ConfigTimeAttendanceEmployeeRequest;
import com.example.onekids_project.request.schoolconfig.ClassConfigRequest;
import com.example.onekids_project.request.schoolconfig.ClassConfigSearchRequest;
import com.example.onekids_project.response.school.ConfigAttendanceEmployeeSchoolResponse;
import com.example.onekids_project.response.school.ConfigTimeAttendanceEmployeeSchoolResponse;
import com.example.onekids_project.response.schoolconfig.ClassConfigResponse;
import com.example.onekids_project.security.model.UserPrincipal;

import java.util.List;

public interface SchoolConfigService {
    /**
     * tạo config của trường đó, được tạo khi tạo trường lần đầu
     *
     * @param idSchool
     * @return
     */
    SchoolConfig createSchoolConfig(Long idSchool);



    /**
     * tìm kiếm lớp cho config ngày nghỉ
     *
     * @param principal
     * @param classConfigSearchRequest
     * @return
     */
    List<ClassConfigResponse> searchMaclassConfig(UserPrincipal principal, ClassConfigSearchRequest classConfigSearchRequest);

    /**
     * cập nhật cấu hình ngày nghỉ cho các lớp
     *
     * @param principal
     * @param classConfigRequestList
     * @return
     */
    boolean updateConfigClassAbsent(UserPrincipal principal, List<ClassConfigRequest> classConfigRequestList);

    ConfigAttendanceEmployeeSchoolResponse getConfigAttendanceEmployeeSchool(UserPrincipal principal);

    boolean updateConfigAttendanceEmployeeSchool(UserPrincipal principal, ConfigAttendanceEmployeeSchoolRequest request);

    ConfigTimeAttendanceEmployeeSchoolResponse getConfigTimeAttendanceEmployeeSchool(UserPrincipal principal);

    boolean updateConfigTimeAttendanceEmployeeSchool(UserPrincipal principal, ConfigTimeAttendanceEmployeeRequest request);
}
