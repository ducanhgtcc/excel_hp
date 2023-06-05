package com.example.onekids_project.mobile.teacher.service.servicecustom;

import com.example.onekids_project.mobile.teacher.response.menuclass.MenuWeekTeacherResponse;
import com.example.onekids_project.security.model.UserPrincipal;

import java.time.LocalDate;
import java.util.List;

public interface ClassMenuWeekTeacherService {
    List<MenuWeekTeacherResponse> findWeekMenu(UserPrincipal userPrincipal, LocalDate localDate);
}
