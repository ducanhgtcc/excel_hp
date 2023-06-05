package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.entity.system.WebSystemTitle;
import com.example.onekids_project.request.system.WebSystemTitleConfigRequest;
import com.example.onekids_project.response.system.WebSystemTitleConfigResponse;

import java.util.List;
import java.util.Optional;

public interface WebSystemTitleService {
    /**
     * tìm kiếm tất cả
     *
     * @return
     */
    List<WebSystemTitleConfigResponse> findAllWebSystemTitle();


    Optional<WebSystemTitle> findById(Long id);

    /**
     * cập nhật data
     *
     * @param webSystemTitleConfigRequestList
     * @return
     */
    boolean updateWebSystemTitle(List<WebSystemTitleConfigRequest> webSystemTitleConfigRequestList);


    /**
     * ceate data default
     */
    void createWebSystemTitle();
}
