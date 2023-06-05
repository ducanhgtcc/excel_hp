package com.example.onekids_project.mobile.plus.controller;

import com.example.onekids_project.mobile.plus.request.historyNotifiRequest.NotifiSysRequest;
import com.example.onekids_project.mobile.plus.response.historyNotifiResponse.ListNotifiSysResponse;
import com.example.onekids_project.mobile.plus.response.historyNotifiResponse.NotifiSysDetailResponse;
import com.example.onekids_project.mobile.plus.service.servicecustom.NotifsysPlusService;
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
@RequestMapping("/mob/plus/notifisys")
public class NotifiSystemController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private NotifsysPlusService notifsysPlusService;

    /**
     * Lấy dách sách thông báo hệ thông
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public ResponseEntity searchnotifiSysPlus(@CurrentUser UserPrincipal principal, @Valid NotifiSysRequest request) {
        RequestUtils.getFirstRequestPlus(principal,request);
        ListNotifiSysResponse listHistoryNotifiPlusResponse = notifsysPlusService.searchNitifiSys(principal, request);
        return NewDataResponse.setDataSearch(listHistoryNotifiPlusResponse);
    }

    /**
     * Xem chi tiết thông báo hệ thống
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/detail/{id}")
    public ResponseEntity findNotifisysDetail(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequestPlus(principal,id);
        NotifiSysDetailResponse notifiSysDetailResponse = notifsysPlusService.findDetailNotifiSys(principal, id);
        return NewDataResponse.setDataSearch(notifiSysDetailResponse);
    }


}
