package com.example.onekids_project.mobile.teacher.service.servicecustom;

import com.example.onekids_project.mobile.teacher.response.CameraTeacherResponse;
import com.example.onekids_project.security.model.UserPrincipal;

import java.util.List;

public interface CameraTeacherService {
    List<CameraTeacherResponse> findCameraTeacher(UserPrincipal principal);
}
