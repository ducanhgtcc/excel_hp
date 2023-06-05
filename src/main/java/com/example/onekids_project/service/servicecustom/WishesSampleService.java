package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.request.system.WishesSampleCreateRequest;
import com.example.onekids_project.request.system.WishesSampleUpdateRequest;
import com.example.onekids_project.response.wishes.WishesSampleResponse;
import com.example.onekids_project.security.model.UserPrincipal;

import java.io.IOException;
import java.util.List;

public interface WishesSampleService {
    /**
     * find all for sysconfig
     *
     * @return
     */
    List<WishesSampleResponse> findAllSystemConfig();

    /**
     * find all for sysconfig
     *
     * @return
     */
    List<WishesSampleResponse> findAllSystemConfigSchool(Long idSchool, UserPrincipal principal);

    /**
     * tạo mẫu wishes sample
     *
     * @param wishesSampleCreateRequest
     * @return
     */
    WishesSampleResponse createWishesSample(Long idSchool, WishesSampleCreateRequest wishesSampleCreateRequest) throws IOException;

    /**
     * update wish sample
     *
     * @param wishesSampleUpdateRequest
     * @return
     */
    WishesSampleResponse updateWishesSample(Long idSchool, WishesSampleUpdateRequest wishesSampleUpdateRequest) throws IOException;

    /**
     * delete wishes sample
     *
     * @param id
     * @return
     */
    boolean deleteWishesSample(Long id);

}
