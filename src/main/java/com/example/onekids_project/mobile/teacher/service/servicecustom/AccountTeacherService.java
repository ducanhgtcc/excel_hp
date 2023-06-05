package com.example.onekids_project.mobile.teacher.service.servicecustom;

import com.example.onekids_project.mobile.teacher.response.account.AccountTeacherResponse;
import com.example.onekids_project.master.response.ProfileMobileResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AccountTeacherService {
    /**
     * lấy thông tin tài khoản
     * @param principal
     * @return
     */
    AccountTeacherResponse findInforAccount(UserPrincipal principal);

    boolean saveAvatar(UserPrincipal principal, MultipartFile multipartFile) throws IOException;

}
