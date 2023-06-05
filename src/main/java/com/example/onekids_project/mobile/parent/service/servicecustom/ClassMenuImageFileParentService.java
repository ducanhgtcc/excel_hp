package com.example.onekids_project.mobile.parent.service.servicecustom;

import com.example.onekids_project.mobile.parent.response.menuclass.ListMenuFileParentResponse;
import com.example.onekids_project.mobile.parent.response.menuclass.MenuFileParentResponse;
import com.example.onekids_project.mobile.parent.response.menuclass.MenuImageWeekParentResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface ClassMenuImageFileParentService {
    MenuImageWeekParentResponse findImageWeek(UserPrincipal userPrincipal, LocalDate localDate);

    ListMenuFileParentResponse findFileAllWeek(UserPrincipal userPrincipal, Pageable pageable, LocalDate localDate);
}
