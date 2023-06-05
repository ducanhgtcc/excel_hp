package com.example.onekids_project.mobile.teacher.controller;

import com.example.onekids_project.common.MessageConstant;
import com.example.onekids_project.master.request.device.DeviceWebRequest;
import com.example.onekids_project.mobile.response.ChangeTokenResponse;
import com.example.onekids_project.mobile.response.NewsMobileResponse;
import com.example.onekids_project.mobile.teacher.response.home.HomeTeacherResponse;
import com.example.onekids_project.mobile.teacher.service.servicecustom.HomeTeacherService;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.DeviceService;
import com.example.onekids_project.util.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/mob/teacher/home")
public class HomeTeacherController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private HomeTeacherService homeTeacherService;

    @Autowired
    private DeviceService deviceService;

    /**
     * tìm kiếm màn home
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getHomeTeacher(@CurrentUser UserPrincipal principal, DeviceWebRequest deviceWebRequest) {
        RequestUtils.getFirstRequest(principal);
        HomeTeacherResponse model = homeTeacherService.findHomeTeacher(principal);
        deviceService.forceLogoutDevice(deviceWebRequest.getIdDevice(), principal.getId());
        long checkExistClass = model.getClassList().stream().filter(x -> x.getIdClass().equals(principal.getIdClassLogin())).count();
        String message = checkExistClass == 0 ? MessageConstant.CLASS_NOT_FOUND : MessageConstant.FIND_HOME;
        return NewDataResponse.setDataCustom(model, message);
    }

    /**
     * đếm số thông báo chưa đọc
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/notifi-unread")
    public ResponseEntity getNewCountNotify(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        int count = homeTeacherService.countNotifyUnread(principal.getId());
        return NewDataResponse.setDataSearch(count);
    }

    /**
     * đếm số thông báo chưa đọc
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/change-class")
    public ResponseEntity getNewCountNotify(@CurrentUser UserPrincipal principal, @RequestParam Long idClass) {
        RequestUtils.getFirstRequest(principal);
        ChangeTokenResponse changeTokenResponse = homeTeacherService.changeClass(principal, idClass);
        return NewDataResponse.setDataCustom(changeTokenResponse, MessageConstant.CHANGE_CLASS);
    }

    /**
     * tìm kiếm tin tức
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/news")
    public ResponseEntity getNews(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        List<NewsMobileResponse> dataList = homeTeacherService.findNews(principal);
        return NewDataResponse.setDataCustom(dataList, MessageConstant.FIND_NEWS);
    }

}
