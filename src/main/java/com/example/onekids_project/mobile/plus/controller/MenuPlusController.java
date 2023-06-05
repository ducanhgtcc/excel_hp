package com.example.onekids_project.mobile.plus.controller;

import com.example.onekids_project.mobile.plus.request.menu.MenuDatePlusRequest;
import com.example.onekids_project.mobile.plus.response.menu.MenuClassResponse;
import com.example.onekids_project.mobile.plus.response.menu.MenuClassWeekResponse;
import com.example.onekids_project.mobile.plus.response.menu.MenuDatePlusResponse;
import com.example.onekids_project.mobile.plus.response.menu.MenuWeekPlusResponse;
import com.example.onekids_project.mobile.plus.service.servicecustom.MenuPlusService;
import com.example.onekids_project.mobile.request.MenuFileRequest;
import com.example.onekids_project.mobile.response.FeatureClassResponse;
import com.example.onekids_project.mobile.response.ImageWeekResponse;
import com.example.onekids_project.mobile.response.ListFileWeekResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/mob/plus/menu")
public class MenuPlusController {

    @Autowired
    private MenuPlusService menuPlusService;


    /**
     * thống kê dữ liệu các lớp
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/statistic-class")
    public ResponseEntity searchMenuClass(@CurrentUser UserPrincipal principal, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate localDate) {
        RequestUtils.getFirstRequestPlus(principal, localDate);
        List<MenuClassResponse> dataList = menuPlusService.searchMenuClass(principal, localDate);
        return NewDataResponse.setDataSearch(dataList);

    }

    /**
     * thống kê dữ liệu thời khóa biểu trong ngày
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/statistic-menu-date")
    public ResponseEntity searchMenuDate(@CurrentUser UserPrincipal principal, @Valid MenuDatePlusRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        List<MenuDatePlusResponse> dataList = menuPlusService.searchMenuDate(principal, request);
        return NewDataResponse.setDataSearch(dataList);

    }

    /**
     * thống kê dữ liệu TKB tháng
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/statistic-menu-month")
    public ResponseEntity searchMenuMonth(@CurrentUser UserPrincipal principal, @Valid MenuDatePlusRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        List<Integer> dataList = menuPlusService.searchMenuMonth(principal, request);
        return NewDataResponse.setDataSearch(dataList);

    }

    /**
     * thống kê dữ liệu TKB tuần các lớp
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/statistic-class-week")
    public ResponseEntity searchMenuClassWeek(@CurrentUser UserPrincipal principal, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate localDate) {
        RequestUtils.getFirstRequestPlus(principal, localDate);
        List<MenuClassWeekResponse> dataList = menuPlusService.searchMenuClassWeek(principal, localDate);
        return NewDataResponse.setDataSearch(dataList);

    }

    /**
     * thống kê dữ liệu menu của 1 lớp
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/statistic-menu-week")
    public ResponseEntity searchMenuWeek(@CurrentUser UserPrincipal principal, @Valid MenuDatePlusRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        List<MenuWeekPlusResponse> menuWeekTeacherResponseList = menuPlusService.searchMenuWeek(principal, request);
        return NewDataResponse.setDataSearch(menuWeekTeacherResponseList);
    }

    /**
     * thống kê dữ liệu chi tiết tuần
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/statistic-class-file")
    public ResponseEntity searchMenuFileClass(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequestPlus(principal);
        List<FeatureClassResponse> dataList = menuPlusService.searchMenuFileClass(principal);
        return NewDataResponse.setDataSearch(dataList);
    }

    /**
     * thống kê dữ liệu ảnh tuần được chọn
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/statistic-menu-image")
    public ResponseEntity searchScheduleImage(@CurrentUser UserPrincipal principal, @Valid MenuDatePlusRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        ImageWeekResponse dataList = menuPlusService.searchMenuImage(principal, request);
        return NewDataResponse.setDataSearch(dataList);
    }

    /**
     * thống kê dữ liệu file tất cả các tuần
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/statistic-menu-file")
    public ResponseEntity searchScheduleFile(@CurrentUser UserPrincipal principal, @Valid MenuFileRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        ListFileWeekResponse dataList = menuPlusService.searchMenuFile(principal, request);
        return NewDataResponse.setDataSearch(dataList);
    }

}
