package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.request.base.IdObjectRequest;
import com.example.onekids_project.request.schoolconfig.MediaCreateRequest;
import com.example.onekids_project.request.schoolconfig.MediaSettingSearchRequest;
import com.example.onekids_project.request.schoolconfig.MediaUpdateRequest;
import com.example.onekids_project.response.schoolconfig.MediaConfigResponse;
import com.example.onekids_project.response.schoolconfig.MediaForClassResponse;
import com.example.onekids_project.response.schoolconfig.MediaSettingResponse;

import java.util.List;

public interface MediaService {
    /**
     * tìm kiếm tất cả các media
     *
     * @param idSchool
     * @return
     */
    List<MediaConfigResponse> findAllMedia(Long idSchool);

    /**
     * cập nhật active media
     *
     * @param id
     * @param active
     * @return
     */
    boolean checkActiveMedia(Long id, boolean active);

    /**
     * cập nhật media
     *
     * @param mediaUpdateRequest
     * @return
     */
    MediaConfigResponse updateMedia(MediaUpdateRequest mediaUpdateRequest);

    /**
     * xóa 1 media
     *
     * @param id
     * @return
     */
    boolean deleteMediaOne(Long id);

    /**
     * xóa nhiều media
     *
     * @param mediaUpdateRequestList
     * @return
     */
    boolean deleteMediaMany(List<MediaUpdateRequest> mediaUpdateRequestList);

    /**
     * create media
     *
     * @param idSchool
     * @param mediaCreateRequest
     * @return
     */
    MediaConfigResponse createMedia(Long idSchool, MediaCreateRequest mediaCreateRequest);

    /**
     * tìm kiếm media setting
     *
     * @param idSchool
     * @param mediaSettingSearchRequest
     * @return
     */
    List<MediaSettingResponse> findAllMediaSetting(Long idSchool, MediaSettingSearchRequest mediaSettingSearchRequest);

    /**
     * tìm kiếm media cho lớp
     *
     * @param idSchool
     * @param idClass
     * @return
     */
    List<MediaForClassResponse> findMediaForClass(Long idSchool, Long idClass);

    /**
     * cập nhật dvrcamera
     *
     * @param idClass
     * @param idObjectRequestList
     * @return
     */
    boolean updateMediaForClass(Long idClass, List<IdObjectRequest> idObjectRequestList);

}
