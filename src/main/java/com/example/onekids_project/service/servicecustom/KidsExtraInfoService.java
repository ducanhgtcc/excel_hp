package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.request.kids.KidsExtraInfoRequest;
import com.example.onekids_project.response.kids.KidsExtraInfoResponse;

import java.util.Optional;

public interface KidsExtraInfoService {

    /**
     * tìm kiếm thông tin mở rộng của học sinh
     *
     * @param idSchool
     * @param id
     * @return
     */
    Optional<KidsExtraInfoResponse> findByIdKidsExtraInfo(Long idSchool, Long id);

    /**
     * tạo thông tin mở rộng của học sinh
     *
     * @param idSchool
     * @param kidsExtraInfoRequest
     * @return
     */
    KidsExtraInfoResponse createKidsExtraInfo(Long idSchool, Kids kid, KidsExtraInfoRequest kidsExtraInfoRequest);

    /**
     * cập nhật thông tin mở rộng của học sinh
     *
     * @param idSchool
     * @param kid
     * @param kidsExtraInfoRequest
     * @return
     */
    KidsExtraInfoResponse updateKidsExtraInfo(Long idSchool, Kids kid, KidsExtraInfoRequest kidsExtraInfoRequest);
}
