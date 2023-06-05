package com.example.onekids_project.mobile.teacher.controller;

import com.example.onekids_project.common.MessageConstant;
import com.example.onekids_project.mobile.teacher.request.historynotifi.SearchHistoryNotifiTeacherRequest;
import com.example.onekids_project.mobile.teacher.response.historynotifi.HistoryNotifiTeacherDetailResponse;
import com.example.onekids_project.mobile.teacher.response.historynotifi.ListHistoryNotifiTeacherResponse;
import com.example.onekids_project.mobile.teacher.response.historynotifi.ListViewDetailUserNotifiTeacherResponse;
import com.example.onekids_project.mobile.teacher.service.servicecustom.HistoryNotifiTeacherMobileService;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/mob/teacher/history-notifi")
public class HistoryNotifiTeacherController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private HistoryNotifiTeacherMobileService historyNotifiTeacherMobileService;

    /**
     * @param principal
     * @param searchHistoryNotifiTeacherRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public ResponseEntity searchHistoryNotifiTeacher(@CurrentUser UserPrincipal principal, @Valid SearchHistoryNotifiTeacherRequest searchHistoryNotifiTeacherRequest) {
        RequestUtils.getFirstRequest(principal,searchHistoryNotifiTeacherRequest);
        ListHistoryNotifiTeacherResponse listHistoryNotifiTeacherResponse = historyNotifiTeacherMobileService.searchHistoryNotifi(principal, searchHistoryNotifiTeacherRequest);
        return NewDataResponse.setDataCustom(listHistoryNotifiTeacherResponse, MessageConstant.SEARCH_HISTORY_NOTIFI);
    }

    /**
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/revoke/{id}")
    public ResponseEntity revokeTeacherReply(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal,id);
        boolean check = historyNotifiTeacherMobileService.historyTeacherReovke(principal, id);
        return NewDataResponse.setDataCustom(check, MessageConstant.HISTORY_NOTIFI_REVOKE);
    }

    /**
     * Xem chi tiết
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/detail/{id}")
    public ResponseEntity findHistoryNotifiDetail(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal,id);
        HistoryNotifiTeacherDetailResponse historyNotifiTeacherDetailResponse = historyNotifiTeacherMobileService.viewDetailNotifi(principal, id);
        return NewDataResponse.setDataCustom(historyNotifiTeacherDetailResponse,MessageConstant.VIEW_DETAIL_HISTORYNOTIFI);
    }

    /**
     * Danh sách người nhận
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/detailuser/{id}")
    public ResponseEntity findHistoryuserNotifiDetail(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal,id);
        ListViewDetailUserNotifiTeacherResponse historyNotifiUserResponse = historyNotifiTeacherMobileService.viewDetailNotifiUser(principal, id);
        return NewDataResponse.setDataCustom(historyNotifiUserResponse,MessageConstant.VIEW_DETAIL_HISTORYNOTIFI);
    }


    /**
     * Thu hồi
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/revokeuser/{id}")
    public ResponseEntity revokeUser(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal,id);
        boolean check = historyNotifiTeacherMobileService.revokeUserNotifi(principal, id);
        return NewDataResponse.setDataCustom(check, MessageConstant.HISTORY_NOTIFI_REVOKE);
    }

}
