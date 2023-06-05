package com.example.onekids_project.mobile.plus.controller;

import com.example.onekids_project.common.MessageConstant;
import com.example.onekids_project.mobile.plus.request.historyNotifiRequest.*;
import com.example.onekids_project.mobile.plus.response.historyNotifiResponse.*;
import com.example.onekids_project.mobile.plus.service.servicecustom.HistoryNotifPlusService;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/mob/plus/historynotifi")
public class HistoryNotifiPlusController {

    @Autowired
    private HistoryNotifPlusService historyNotifPlusService;

    /**
     * Danh sách thông báo app
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public ResponseEntity searchMessagePlus(@CurrentUser UserPrincipal principal, @Valid SearchHistoryNotifiPlusRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        ListHistoryNotifiPlusResponse listHistoryNotifiPlusResponse = historyNotifPlusService.searchHistoryNotifi(principal, request);
        return NewDataResponse.setDataSearch(listHistoryNotifiPlusResponse);
    }


    /**
     * Xóa thông báo app
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/delete/{id}")
    public ResponseEntity revokeMessageParent(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequestPlus(principal, id);
        boolean checkUpdate = historyNotifPlusService.deleteNotifi(id);
        return NewDataResponse.setDataCustom(checkUpdate, MessageConstant.DELETE_NOTIFI);
    }

    /**
     * Danh sách thông báo sms
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/sms")
    public ResponseEntity searchSmsPlus(@CurrentUser UserPrincipal principal, @Valid SearchSmsPlusRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        ListSmsPlusResponse listSmsPlusResponse = historyNotifPlusService.searchSmsPlus(principal, request);
        return NewDataResponse.setDataSearch(listSmsPlusResponse);
    }

    /**
     * Chi tiết thông báo app
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/detail/{id}")
    public ResponseEntity findDetailAppsend(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequestPlus(principal, id);
        DetailHistoryNotifiAppResponse detailHistoryNotifiAppResponse = historyNotifPlusService.findDeTailNotifi(principal, id);
        return NewDataResponse.setDataSearch(detailHistoryNotifiAppResponse);
    }

    /**
     * Chi tiết thông báo sms
     *
     * @param principal
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/detailsms/{id}")
    public ResponseEntity findDetailSms(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id, @Valid DetailSmsRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        DetailSmsResponse detailSmsResponse = historyNotifPlusService.findDeTailSms(principal, id, request);
        return NewDataResponse.setDataSearch(detailSmsResponse);
    }

    /**
     * Chi tiết nội dung tin nhắn tùy chỉnh
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/detail-sms-custom/{id}")
    public ResponseEntity findDetailSmsCustom(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequestPlus(principal, id);
        DetailSmsCustomResponse response = historyNotifPlusService.findDeTailSmsCustom(principal, id);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * Danh sách người nhận
     *
     * @param principal
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/detailuser/{id}")
    public ResponseEntity findHistoryuserNotifiDetail(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id, @Valid DetailUserRequest request) {
        RequestUtils.getFirstRequestPlus(principal, id);
        ListViewDetailUserNotifiPlusResponse response = historyNotifPlusService.viewDetailNotifiUserPlus(principal, id, request);
        return NewDataResponse.setDataCustom(response, MessageConstant.VIEW_DETAIL_HISTORYNOTIFI);
    }

    /**
     * Xóa thông báo theo id người nhận
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/deletebyid")
    public ResponseEntity deleteMultiPicture(@CurrentUser UserPrincipal principal, @RequestBody @Valid DeleteNotifiRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        boolean check = historyNotifPlusService.deletenotifi(principal, request);
        return NewDataResponse.setDataCustom(check, MessageConstant.DELETE_NOTIFI);
    }

    /* duyệt thông báo theo id người nhận
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/approve")
    public ResponseEntity apporveMultiPicture(@CurrentUser UserPrincipal principal, @RequestBody @Valid ApproveNotifiRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        boolean check = historyNotifPlusService.approveMultiNotifi(principal, request);
        return NewDataResponse.setDataCustom(check, MessageConstant.APPOVE_NOTIFI);
    }

    /**
     * Thu hồi thông báo theo id người nhận
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/revoke")
    public ResponseEntity sendDelMultiPicture(@CurrentUser UserPrincipal principal, @RequestBody @Valid DeleteNotifiRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        boolean check = historyNotifPlusService.revokeMultiNotifi(principal, request);
        return NewDataResponse.setDataCustom(check, MessageConstant.SENDEL_NOTIFI);
    }

}
