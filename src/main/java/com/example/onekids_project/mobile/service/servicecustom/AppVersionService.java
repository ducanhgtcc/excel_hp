package com.example.onekids_project.mobile.service.servicecustom;

import com.example.onekids_project.mobile.response.AppVersionResponse;

import java.util.List;

public interface AppVersionService {
    /**
     * lấy tất cả các thông tin liên quan đến các hệ điều hành và apptype
     *
     * @return
     */
    List<AppVersionResponse> findAllAppVersion();

    boolean createAppVersion();
}
