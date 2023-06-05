package com.example.onekids_project.mobile.teacher.service.servicecustom;

import com.example.onekids_project.mobile.teacher.response.VideoTeacherResponse;
import com.example.onekids_project.security.model.UserPrincipal;

public interface VideoTeacherService {
    VideoTeacherResponse findVideoTeacher(UserPrincipal principal);
}
