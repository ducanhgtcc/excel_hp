package com.example.onekids_project.mobile.plus.controller;

import com.example.onekids_project.common.MessageConstant;
import com.example.onekids_project.mobile.plus.request.album.*;
import com.example.onekids_project.mobile.plus.response.album.AlbumDetailPlusMobileResponse;
import com.example.onekids_project.mobile.plus.response.album.DeleteMultialbumPlusRequest;
import com.example.onekids_project.mobile.plus.response.album.ListAlbumPlusMobileResponse;
import com.example.onekids_project.mobile.plus.service.servicecustom.AlbumPlusMobileService;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.RequestUtils;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/mob/plus/album")
public class AlbumPlusController {

    @Autowired
    private AlbumPlusMobileService albumPlusMobileService;

    /**
     * lấy danh sách album
     *
     * @param principal
     * @param searchAlbumPlusRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity searchalbumTeacher(@CurrentUser UserPrincipal principal, @Valid SearchAlbumPlusRequest searchAlbumPlusRequest) {
        RequestUtils.getFirstRequestPlus(principal, searchAlbumPlusRequest);
        ListAlbumPlusMobileResponse listAlbumPlusMobileResponse = albumPlusMobileService.findAllAlbumForPlus(principal, searchAlbumPlusRequest);
        return NewDataResponse.setDataSearch(listAlbumPlusMobileResponse);
    }

    /**
     * xem chi tiết album plus
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/detail/{id}")
    public ResponseEntity findAlbumDetailt(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequestPlus(principal, id);
        AlbumDetailPlusMobileResponse albumDetailPlusMobileResponse = albumPlusMobileService.findDetailalbumplus(principal, id);
        return NewDataResponse.setDataSearch(albumDetailPlusMobileResponse);
    }

    /**
     * duyệt album plus
     *
     * @param principal
     * @param request
     * @return
     * @throws FirebaseMessagingException
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/approvealbum")
    public ResponseEntity updateMultiApproveAlbum(@CurrentUser UserPrincipal principal, @RequestBody @Valid UpdateApproveMultialbumPlusRequest request) throws FirebaseMessagingException {
        RequestUtils.getFirstRequestPlus(principal, request);
        boolean check = albumPlusMobileService.updateApprovedAlbum(principal, request);
        return NewDataResponse.setDataCustom(check, MessageConstant.APPROVE_ALBUM);
    }

    /**
     * Xóa album plus
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/delete")
    public ResponseEntity deleteMultiAlbum(@CurrentUser UserPrincipal principal, @RequestBody @Valid DeleteMultialbumPlusRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        boolean check = albumPlusMobileService.deleteMultiAlbum(principal, request);
        return NewDataResponse.setDataCustom(check, MessageConstant.DELETE_ALBUM);
    }

    /**
     * Xóa ảnh album plus
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/deletepicture")
    public ResponseEntity deleteMultiPicture(@CurrentUser UserPrincipal principal, @RequestBody @Valid DeleteMultpicturePlusRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        boolean check = albumPlusMobileService.deleteMultiPicture(principal, request);
        return NewDataResponse.setDataCustom(check, MessageConstant.DELETE_PICTURES);
    }

    /**
     * Duyệt ảnh
     *
     * @param principal
     * @param request
     * @return
     * @throws FirebaseMessagingException
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/approvepicture")
    public ResponseEntity apporveMultiPicture(@CurrentUser UserPrincipal principal, @RequestBody @Valid ApproveMultpicturePlusRequest request) throws FirebaseMessagingException {
        RequestUtils.getFirstRequestPlus(principal, request);
        boolean check = albumPlusMobileService.approvePicture(principal, request);
        return NewDataResponse.setDataCustom(check, MessageConstant.APPROVE_PICTURE);
    }

    /**
     * Tạo album
     *
     * @param principal
     * @param request
     * @return
     * @throws IOException
     * @throws FirebaseMessagingException
     */
    @RequestMapping(method = RequestMethod.POST, value = "/create")
    public ResponseEntity createAlbum(@CurrentUser UserPrincipal principal, @ModelAttribute @Valid CreateAlbumPlusRequest request) throws IOException, FirebaseMessagingException {
        RequestUtils.getFirstRequestPlus(principal, request);
        boolean check = albumPlusMobileService.createAlbumPlus(principal, request);
        return NewDataResponse.setDataCustom(check, MessageConstant.CREATE_ALBUM);
    }

    /**
     * Cập nhật album
     *
     * @param principal
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping(method = RequestMethod.POST, value = "/update")
    public ResponseEntity upadteAlbum(@CurrentUser UserPrincipal principal, @ModelAttribute @Valid UpdateAlbumPlusRequest request) throws IOException {
        RequestUtils.getFirstRequestPlus(principal, request);
        boolean check = albumPlusMobileService.updateAlbum(principal, request);
        return NewDataResponse.setDataCustom(check, MessageConstant.UPDATE_PICTURE);
    }

}
