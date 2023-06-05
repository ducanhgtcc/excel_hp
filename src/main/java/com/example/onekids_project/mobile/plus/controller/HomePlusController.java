package com.example.onekids_project.mobile.plus.controller;

import com.example.onekids_project.common.MessageConstant;
import com.example.onekids_project.master.request.device.DeviceWebRequest;
import com.example.onekids_project.mobile.plus.response.home.HomeFirstPlusResponse;
import com.example.onekids_project.mobile.plus.response.home.HomePlusResponse;
import com.example.onekids_project.mobile.plus.service.servicecustom.HomePlusService;
import com.example.onekids_project.mobile.response.ChangeTokenResponse;
import com.example.onekids_project.mobile.response.NewsMobileResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.DeviceService;
import com.example.onekids_project.util.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/mob/plus/home")
public class HomePlusController {

    @Autowired
    private HomePlusService homePlusService;
    @Autowired
    private DeviceService deviceService;

    @RequestMapping(method = RequestMethod.GET,value = "/first")
    public ResponseEntity findHomeFirst(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequestPlus(principal);
        HomeFirstPlusResponse model = homePlusService.getHomeFirstPlus(principal);
        return NewDataResponse.setDataSearch(model);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity findHome(@CurrentUser UserPrincipal principal, DeviceWebRequest deviceWebRequest) {
        RequestUtils.getFirstRequestPlus(principal);
        HomePlusResponse model = homePlusService.getHomePlus(principal);
        deviceService.forceLogoutDevice(deviceWebRequest.getIdDevice(), principal.getId());
        return NewDataResponse.setDataSearch(model);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/change-school")
    public ResponseEntity changeSchool(@CurrentUser UserPrincipal principal, @RequestParam Long idSchool) {
        RequestUtils.getFirstRequestPlus(principal, idSchool);
        String token = homePlusService.changeSchool(principal, idSchool);
        return NewDataResponse.setDataCustom(token, MessageConstant.CHANGE_SCHOOL);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/news")
    public ResponseEntity getNews(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequestPlus(principal);
        List<NewsMobileResponse> dataList = homePlusService.findNews(principal);
        return NewDataResponse.setDataCustom(dataList, MessageConstant.FIND_NEWS);
    }

}
