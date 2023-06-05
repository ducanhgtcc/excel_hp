package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.request.base.IdObjectRequest;
import com.example.onekids_project.request.schoolconfig.DvrCameraCreateRequest;
import com.example.onekids_project.request.schoolconfig.DvrCameraUpdateRequest;
import com.example.onekids_project.request.schoolconfig.DvrcameraActiveRequest;
import com.example.onekids_project.response.schoolconfig.DvrCameraResponse;

import java.util.List;

public interface DvrCameraService {
    /**
     * tìm kiếm tất cả dvrcamera
     *
     * @param idSchool
     * @return
     */
    List<DvrCameraResponse> findAllDvrCamera(Long idSchool);

    /**
     * thêm mới dvrcamera
     *
     * @param idSchool
     * @param dvrCameraCreateRequest
     * @return
     */
    DvrCameraResponse createDvrCamera(Long idSchool, DvrCameraCreateRequest dvrCameraCreateRequest);

    /**
     * cập nhật dvrcamera
     *
     * @param idSchool
     * @param dvrCameraUpdateRequest
     * @return
     */
    DvrCameraResponse updateDvrCamera(Long idSchool, DvrCameraUpdateRequest dvrCameraUpdateRequest);


    /**
     * xóa dvrcamera theo id
     *
     * @param id
     * @return
     */
    boolean deleteDvrcameraOne(Long id);

    /**
     * xóa nhiều dvrcamera
     *
     * @param idObjectRequestList
     * @return
     */
    boolean deleteMediaMany(List<IdObjectRequest> idObjectRequestList);

    /**
     * cập nhật kích hoạt dvrcamera
     *
     * @param dvrcameraActiveRequest
     * @return
     */
    boolean checkActiveDvrcamera(DvrcameraActiveRequest dvrcameraActiveRequest);

}
