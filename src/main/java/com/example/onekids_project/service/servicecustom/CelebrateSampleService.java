package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.request.celebrate.CelebrateSampleActiveRequest;
import com.example.onekids_project.request.celebrate.CelebrateSampleCreateRequest;
import com.example.onekids_project.request.celebrate.CelebrateSampleUpdateRequest;
import com.example.onekids_project.response.celebrate.CelebrateSampleResponse;

import java.io.IOException;
import java.util.List;

public interface CelebrateSampleService {

    /**
     * find all celebrate for system
     *
     * @return
     */
    List<CelebrateSampleResponse> findAllCelebrateSystem(Long idSchool);

    /**
     * cập nhật các loại kích hoạt
     *
     * @param celebrateSampleActiveRequest
     * @return
     */
    CelebrateSampleResponse updateCelebrateSampleActive(CelebrateSampleActiveRequest celebrateSampleActiveRequest);

    /**
     * tạo mẫu celebrate sample
     *
     * @param wishesSampleCreateRequest
     * @return
     */
    CelebrateSampleResponse createCelebrateSample(Long idSchool, CelebrateSampleCreateRequest wishesSampleCreateRequest) throws IOException;

    /**
     * update celebrate sample
     *
     * @param celebrateSampleUpdateRequest
     * @return
     */
    CelebrateSampleResponse updateCelebrateSample(Long idSchool, CelebrateSampleUpdateRequest celebrateSampleUpdateRequest) throws IOException;

    /**
     * delete wishes sample
     *
     * @param id
     * @return
     */
    boolean deleteCelebrateSample(Long id);
}
