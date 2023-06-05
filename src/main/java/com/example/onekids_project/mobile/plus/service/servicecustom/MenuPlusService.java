package com.example.onekids_project.mobile.plus.service.servicecustom;

import com.example.onekids_project.mobile.plus.request.menu.MenuDatePlusRequest;
import com.example.onekids_project.mobile.plus.response.menu.MenuClassResponse;
import com.example.onekids_project.mobile.plus.response.menu.MenuClassWeekResponse;
import com.example.onekids_project.mobile.plus.response.menu.MenuDatePlusResponse;
import com.example.onekids_project.mobile.plus.response.menu.MenuWeekPlusResponse;
import com.example.onekids_project.mobile.request.MenuFileRequest;
import com.example.onekids_project.mobile.response.FeatureClassResponse;
import com.example.onekids_project.mobile.response.ImageWeekResponse;
import com.example.onekids_project.mobile.response.ListFileWeekResponse;
import com.example.onekids_project.security.model.UserPrincipal;

import java.time.LocalDate;
import java.util.List;

public interface MenuPlusService {

    List<MenuClassResponse> searchMenuClass(UserPrincipal principal, LocalDate localDate);

    List<MenuDatePlusResponse> searchMenuDate(UserPrincipal principal, MenuDatePlusRequest request);

    List<Integer> searchMenuMonth(UserPrincipal principal, MenuDatePlusRequest request);

    List<MenuClassWeekResponse> searchMenuClassWeek(UserPrincipal principal, LocalDate localDate);

    List<MenuWeekPlusResponse> searchMenuWeek(UserPrincipal principal, MenuDatePlusRequest request);

    List<FeatureClassResponse> searchMenuFileClass(UserPrincipal principal);

    ImageWeekResponse searchMenuImage(UserPrincipal principal, MenuDatePlusRequest request);

    ListFileWeekResponse searchMenuFile(UserPrincipal principal, MenuFileRequest request);
}
