package com.example.onekids_project.mobile.teacher.service.servicecustom;

import com.example.onekids_project.mobile.response.ChangeTokenResponse;
import com.example.onekids_project.mobile.response.NewsMobileResponse;
import com.example.onekids_project.mobile.teacher.response.home.HomeFirstTeacherResponse;
import com.example.onekids_project.mobile.teacher.response.home.HomeTeacherResponse;
import com.example.onekids_project.security.model.UserPrincipal;

import java.util.List;

public interface HomeTeacherService {
    /**
     * get data first home
     * @param principal
     * @return
     */
    HomeFirstTeacherResponse findHomeFirstTeacher(UserPrincipal principal);

    /**
     * get home
     * @param principal
     * @return
     */
    HomeTeacherResponse findHomeTeacher(UserPrincipal principal);

    /**
     * đếm số thông báo chưa đọc
     * @param idClass
     * @return
     */
    int countNotifyUnread(Long idClass);

    /**
     * thay đổi lớp
     * @param principal
     * @param idClass
     * @return
     */
    ChangeTokenResponse changeClass(UserPrincipal principal, Long idClass);

    List<NewsMobileResponse> findNews(UserPrincipal principal);

}
