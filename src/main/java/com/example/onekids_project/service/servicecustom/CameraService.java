package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.request.base.IdObjectRequest;
import com.example.onekids_project.request.schoolconfig.CameraActiveRequest;
import com.example.onekids_project.request.schoolconfig.CameraCreateRequest;
import com.example.onekids_project.request.schoolconfig.CameraUpdateRequest;
import com.example.onekids_project.request.schoolconfig.MediaSettingSearchRequest;
import com.example.onekids_project.response.schoolconfig.CameraForClassResponse;
import com.example.onekids_project.response.schoolconfig.CameraResponse;
import com.example.onekids_project.response.schoolconfig.CameraSettingResponse;

import java.util.List;

public interface CameraService {
    /**
     * tìm kiếm tất cả camera
     *
     * @param idSchool
     * @return
     */
    List<CameraResponse> findAllCamera(Long idSchool);

    /**
     * tạo camera
     *
     * @param idSchool
     * @param cameraCreateRequest
     * @return
     */
    CameraResponse createCamera(Long idSchool, CameraCreateRequest cameraCreateRequest);

    /**
     * cập nhật camera
     *
     * @param idSchool
     * @param cameraUpdateRequest
     * @return
     */
    CameraResponse updateCamera(Long idSchool, CameraUpdateRequest cameraUpdateRequest);

    /**
     * xóa camera theo id
     *
     * @param id
     * @return
     */
    boolean deleteCameraOne(Long id);

    /**
     * xóa nhiều camera
     *
     * @param idObjectRequestList
     * @return
     */
    boolean deleteMediaMany(List<IdObjectRequest> idObjectRequestList);

    /**
     * cập nhật kích hoạt camera
     *
     * @param cameraActiveRequest
     * @return
     */
    boolean checkActiveCamera(CameraActiveRequest cameraActiveRequest);

    /**
     * tìm kiếm media setting
     *
     * @param idSchool
     * @param mediaSettingSearchRequest
     * @return
     */
    List<CameraSettingResponse> findAllCameraSetting(Long idSchool, MediaSettingSearchRequest mediaSettingSearchRequest);

    /**
     * tìm kiếm camera cho lớp
     *
     * @param idSchool
     * @param idClass
     * @return
     */
    List<CameraForClassResponse> findCameraForClass(Long idSchool, Long idClass);

    /**
     * cập nhật camera
     *
     * @param idClass
     * @param idObjectRequestList
     * @return
     */
    boolean updateCameraForClass(Long idClass, List<IdObjectRequest> idObjectRequestList);
}
