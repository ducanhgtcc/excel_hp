package com.example.onekids_project.mobile.service.servicecustom;

import com.example.onekids_project.mobile.response.MobileMaUserResponse;
import com.example.onekids_project.security.model.UserPrincipal;

public interface MobileMaUserService {
    /**
     * tìm kiếm thông tin người dùng đang đăng nhập
     * @param principal
     * @return
     */
    MobileMaUserResponse getMaUserByIdOfFather(UserPrincipal principal);
}
