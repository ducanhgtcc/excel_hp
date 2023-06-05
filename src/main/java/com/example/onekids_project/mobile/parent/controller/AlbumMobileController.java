package com.example.onekids_project.mobile.parent.controller;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.mobile.parent.response.album.AlbumDetailMobileResponse;
import com.example.onekids_project.mobile.parent.response.album.ListExAlbumKidsMobileResponse;
import com.example.onekids_project.mobile.parent.service.servicecustom.AlbumMobileService;
import com.example.onekids_project.mobile.request.DateTimeRequest;
import com.example.onekids_project.response.common.DataResponse;
import com.example.onekids_project.response.common.ErrorResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.RequestUtils;
import com.example.onekids_project.validate.CommonValidate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mob/parent/album")
public class AlbumMobileController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AlbumMobileService albumMobileService;

    /**
     * tìm kiếm danh sách album
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity findAllAlbum(@CurrentUser UserPrincipal principal, DateTimeRequest localDateTime) {
        RequestUtils.getFirstRequest(principal,"danh sách album");
        CommonValidate.checkDataParent(principal);
        Pageable pageable = PageRequest.of(1, AppConstant.MAX_PAGE_ITEM);
        ListExAlbumKidsMobileResponse listExAlbumKidsMobileResponse = albumMobileService.findAlbumAll(principal, pageable, localDateTime.getDateTime());
        return NewDataResponse.setDataSearch(listExAlbumKidsMobileResponse);

    }

    @RequestMapping(method = RequestMethod.GET, value = "/school")
    public ResponseEntity findAllAlbumschool(@CurrentUser UserPrincipal principal, DateTimeRequest localDateTime) {
        RequestUtils.getFirstRequest(principal, localDateTime);
        CommonValidate.checkDataParent(principal);
        Pageable pageable = PageRequest.of(1, AppConstant.MAX_PAGE_ITEM);
        ListExAlbumKidsMobileResponse listExAlbumKidsMobileResponse = albumMobileService.findAlbumforSchool(principal, pageable, localDateTime.getDateTime());
        return NewDataResponse.setDataSearch(listExAlbumKidsMobileResponse);

    }


    @RequestMapping(method = RequestMethod.GET, value = "/class")
    public ResponseEntity findAllAlbumclass(@CurrentUser UserPrincipal principal, DateTimeRequest localDateTime) {
        RequestUtils.getFirstRequest(principal, localDateTime,"Danh sách album lớp");
        CommonValidate.checkDataParent(principal);
        Pageable pageable = PageRequest.of(1, AppConstant.MAX_PAGE_ITEM);
        ListExAlbumKidsMobileResponse listExAlbumKidsMobileResponse = albumMobileService.findAlbumforClassmob(principal, pageable, localDateTime.getDateTime());
        return NewDataResponse.setDataSearch(listExAlbumKidsMobileResponse);

    }

    /**
     * xem chi tiết album
     *
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/detail/{id}")
    public ResponseEntity findAlbumDetail(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal, id,"Xem chi tiết album");
        CommonValidate.checkDataParent(principal);
        AlbumDetailMobileResponse albumDetailMobileResponse = albumMobileService.findAlbummobdetail(principal, id);
        return NewDataResponse.setDataSearch(albumDetailMobileResponse);
    }

}
