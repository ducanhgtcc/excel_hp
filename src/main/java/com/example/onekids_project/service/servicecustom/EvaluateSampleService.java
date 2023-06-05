package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.request.schoolconfig.EvaluateSampleCreateRequest;
import com.example.onekids_project.request.schoolconfig.EvaluateSampleUpdateRequest;
import com.example.onekids_project.response.schoolconfig.EvaluateSampleConfigResponse;
import com.example.onekids_project.security.model.UserPrincipal;

import java.util.List;

public interface EvaluateSampleService {

    /**
     * tìm kiếm mẫu nhận xét mặc định
     *
     * @param idSchool
     * @return
     */
    List<EvaluateSampleConfigResponse> findAllEvaluateSample(Long idSchool, UserPrincipal principal);

    /**
     * tạo mẫu nhận xét
     *
     * @param idSchool
     * @param evaluateSampleCreateRequest
     * @return
     */
    EvaluateSampleConfigResponse createEvaluateSample(Long idSchool, EvaluateSampleCreateRequest evaluateSampleCreateRequest);

    /**
     * cập nhật mẫu nhận xét mặc định
     *
     * @param idSchool
     * @param evaluateSampleUpdateRequest
     * @return
     */
    EvaluateSampleConfigResponse updateEvaluateSample(Long idSchool, EvaluateSampleUpdateRequest evaluateSampleUpdateRequest);

    /**
     * xóa mẫu nhận xét mặc định
     *
     * @param idSchool
     * @param id
     * @return
     */
    boolean deleteEvaluateSample(Long idSchool, Long id);

//    --------------system config-------------------

    /**
     * tìm kiếm mẫu nhận xét mặc định
     *
     * @return
     */
    List<EvaluateSampleConfigResponse> findAllEvaluateSampleSystem();

    /**
     * tạo mẫu nhận xét
     *
     * @param evaluateSampleCreateRequest
     * @return
     */
    EvaluateSampleConfigResponse createEvaluateSampleSystem(EvaluateSampleCreateRequest evaluateSampleCreateRequest);

    /**
     * cập nhật mẫu nhận xét mặc định
     *
     * @param evaluateSampleUpdateRequest
     * @return
     */
    EvaluateSampleConfigResponse updateEvaluateSampleSystem(EvaluateSampleUpdateRequest evaluateSampleUpdateRequest);

    /**
     * xóa mẫu nhận xét mặc định
     *
     * @param id
     * @return
     */
    boolean deleteEvaluateSampleSystem(Long id);
}
