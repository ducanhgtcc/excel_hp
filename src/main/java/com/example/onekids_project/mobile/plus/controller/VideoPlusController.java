package com.example.onekids_project.mobile.plus.controller;

import com.example.onekids_project.mobile.plus.request.video.SearchVideoPlusRequest;
import com.example.onekids_project.mobile.plus.response.video.ListCameraClassDetailResponse;
import com.example.onekids_project.mobile.plus.response.video.ListCameraPlusResponse;
import com.example.onekids_project.mobile.plus.response.video.ListVideoPlusResponse;
import com.example.onekids_project.mobile.plus.service.servicecustom.VideoPlusService;
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
@RequestMapping("/mob/plus/")
public class VideoPlusController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    VideoPlusService videoPlusService;

    /**
     * Danh sách video
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/video")
    public ResponseEntity searchVideoPlus(@CurrentUser UserPrincipal principal, @Valid SearchVideoPlusRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        ListVideoPlusResponse listVideoPlusResponse = videoPlusService.findVideoPlus(principal, request);
        return NewDataResponse.setDataSearch(listVideoPlusResponse);
    }

    /**
     * Danh sách tất cả camera
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/findall")
    public ResponseEntity findAllCamera(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequestPlus(principal);
        ListCameraClassDetailResponse response = videoPlusService.findAllCamera(principal);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * Danh sách camera lớp
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/camera")
    public ResponseEntity searchcamera(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequestPlus(principal);
        ListCameraPlusResponse listCameraPlusResponse = videoPlusService.searchCameraPlus(principal);
        return NewDataResponse.setDataSearch(listCameraPlusResponse);
    }

    /**
     * Xem chi tiết
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/detail/{id}")
    public ResponseEntity findDetailCamera(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequestPlus(principal, id);
        ListCameraClassDetailResponse listCameraClassDetailResponse = videoPlusService.findDeTailCameraClass(principal, id);
        return NewDataResponse.setDataSearch(listCameraClassDetailResponse);
    }
}
