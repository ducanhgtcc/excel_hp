package com.example.onekids_project.mobile.parent.service.servicecustom;

import com.example.onekids_project.mobile.parent.response.menuclass.MenuWeekParentResponse;
import com.example.onekids_project.security.model.UserPrincipal;

import java.time.LocalDate;
import java.util.List;

public interface ClassMenuWeekParentService {
    List<MenuWeekParentResponse> findWeekMenu(UserPrincipal userPrincipal, LocalDate localDate);
}
