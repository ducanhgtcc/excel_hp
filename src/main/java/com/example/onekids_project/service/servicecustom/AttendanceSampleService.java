package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.request.schoolconfig.AttendanceSampleCreateRequest;
import com.example.onekids_project.request.schoolconfig.AttendanceSampleUpdateRequest;
import com.example.onekids_project.response.schoolconfig.AttendanceSampleConfigResponse;
import com.example.onekids_project.security.model.UserPrincipal;

import java.util.List;

public interface AttendanceSampleService {
    /**
     * tìm kiếm mẫu điểm danh mặc định
     *
     * @param idSchool
     * @return
     */
    List<AttendanceSampleConfigResponse> findAllAttendanceSample(Long idSchool, UserPrincipal principal);

    /**
     * tạo mẫu điểm danh
     *
     * @param idSchool
     * @param attendanceSampleCreateRequest
     * @return
     */
    AttendanceSampleConfigResponse createAttendanceSample(Long idSchool, AttendanceSampleCreateRequest attendanceSampleCreateRequest);

    /**
     * cập nhật mẫu điểm danh
     *
     * @param idSchool
     * @param attendanceSampleUpdateRequest
     * @return
     */
    AttendanceSampleConfigResponse updateAttendanceSample(Long idSchool, AttendanceSampleUpdateRequest attendanceSampleUpdateRequest);

    /**
     * xóa mẫu điểm danh mặc định
     *
     * @param idSchool
     * @param id
     * @return
     */
    boolean deleteAttendanceSample(Long idSchool, Long id);


//    ---------------- các method trong phần master-------------

    /**
     * tìm kiếm mẫu điểm danh mặc định
     *
     * @return
     */
    List<AttendanceSampleConfigResponse> findAllAttendanceSampleSytem();

    /**
     * tạo mẫu điểm danh
     *
     * @param attendanceSampleCreateRequest
     * @return
     */
    AttendanceSampleConfigResponse createAttendanceSampleSystem(AttendanceSampleCreateRequest attendanceSampleCreateRequest);

    /**
     * cập nhật mẫu điểm danh
     *
     * @param attendanceSampleUpdateRequest
     * @return
     */
    AttendanceSampleConfigResponse updateAttendanceSampleSystem(AttendanceSampleUpdateRequest attendanceSampleUpdateRequest);

    /**
     * xóa mẫu điểm danh mặc định
     *
     * @param id
     * @return
     */
    boolean deleteAttendanceSampleSystem(Long id);
}
