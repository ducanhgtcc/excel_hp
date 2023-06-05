package com.example.onekids_project.mobile.parent.service.servicecustom;

import com.example.onekids_project.mobile.parent.response.menuclass.MenuDateParentResponse;
import com.example.onekids_project.security.model.UserPrincipal;

import java.time.LocalDate;
import java.util.List;

public interface ClassMenuDateParentService {
    List<MenuDateParentResponse> findDateMenu(UserPrincipal userPrincipal, LocalDate localDate);
    List<Integer> findClassMenuMonthList(UserPrincipal principal, LocalDate localDate);
}
