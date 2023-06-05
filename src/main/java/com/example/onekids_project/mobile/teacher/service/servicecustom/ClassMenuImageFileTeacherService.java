package com.example.onekids_project.mobile.teacher.service.servicecustom;

import com.example.onekids_project.mobile.teacher.response.menuclass.ListMenuFileTeacherResponse;
import com.example.onekids_project.mobile.teacher.response.menuclass.MenuImageWeekTeacherResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface ClassMenuImageFileTeacherService {
    MenuImageWeekTeacherResponse findImageWeek(UserPrincipal userPrincipal, LocalDate localDate);

    ListMenuFileTeacherResponse findFileAllWeek(UserPrincipal userPrincipal, Integer pageNumber);
}
