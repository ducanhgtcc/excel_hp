package com.example.onekids_project.mobile.teacher.controller;

import com.example.onekids_project.common.MessageConstant;
import com.example.onekids_project.mobile.teacher.request.album.*;
import com.example.onekids_project.mobile.teacher.response.album.AlbumDetailTeacherMobileResponse;
import com.example.onekids_project.mobile.teacher.response.album.ListAlbumTeacherMobileResponse;
import com.example.onekids_project.mobile.teacher.service.servicecustom.AlbumTeacherMobileService;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.RequestUtils;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/mob/teacher/album")
public class AlbumTeacherMobileController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AlbumTeacherMobileService albumTeacherMobileService;

    /**
     * Danh sách album
     *
     * @param principal
     * @param albumTeacherRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity searchalbumTeacher(@CurrentUser UserPrincipal principal, AlbumTeacherRequest albumTeacherRequest) {
        RequestUtils.getFirstRequest(principal, albumTeacherRequest);
        ListAlbumTeacherMobileResponse listAlbumTeacherMobileResponse = albumTeacherMobileService.findAllAlbumForTeachers(principal, albumTeacherRequest);
        return NewDataResponse.setDataSearch(listAlbumTeacherMobileResponse);
    }

    /**
     * album trường
     *
     * @param principal
     * @param albumTeacherRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/school")
    public ResponseEntity searchalbumSchoolTeacher(@CurrentUser UserPrincipal principal, AlbumTeacherRequest albumTeacherRequest) {
        RequestUtils.getFirstRequest(principal, albumTeacherRequest);
        ListAlbumTeacherMobileResponse listAlbumTeacherMobileResponse = albumTeacherMobileService.findAlbumSchoolteacher(principal, albumTeacherRequest);
        return NewDataResponse.setDataSearch(listAlbumTeacherMobileResponse);
    }

    /**
     * Danh sách album lớp
     *
     * @param principal
     * @param albumTeacherRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/class")
    public ResponseEntity searchalbumClassTeacher(@CurrentUser UserPrincipal principal, AlbumTeacherRequest albumTeacherRequest) {
        RequestUtils.getFirstRequest(principal, albumTeacherRequest);
        ListAlbumTeacherMobileResponse listAlbumTeacherMobileResponse = albumTeacherMobileService.findAlbumClassteacher(principal, albumTeacherRequest);
        return NewDataResponse.setDataSearch(listAlbumTeacherMobileResponse);
    }

    /**
     * Xóa album
     *
     * @param principal
     * @param deleteMultialbumTeacherRequest
     * @return
     */
    @PutMapping("/multi-album")
    public ResponseEntity deleteMultiAlbum(@CurrentUser UserPrincipal principal, @RequestBody @Valid DeleteMultialbumTeacherRequest deleteMultialbumTeacherRequest) {
        RequestUtils.getFirstRequest(principal, deleteMultialbumTeacherRequest);
        boolean check = albumTeacherMobileService.deleteMultiAlbumTeacher(principal, principal.getIdClassLogin(), deleteMultialbumTeacherRequest);
        return NewDataResponse.setDataCustom(check, MessageConstant.DELETE_ALBUM);
    }

    /**
     * Xóa ảnh
     *
     * @param principal
     * @param deleteMultpictureTeacherRequest
     * @return
     */
    @PutMapping("/delete-picture")
    public ResponseEntity deleteMultiPicture(@CurrentUser UserPrincipal principal, @RequestBody @Valid DeleteMultpictureTeacherRequest deleteMultpictureTeacherRequest) {
        RequestUtils.getFirstRequest(principal, deleteMultpictureTeacherRequest);
        int coutSuccess = albumTeacherMobileService.deleteMultiPictureTeacher(principal, principal.getIdClassLogin(), deleteMultpictureTeacherRequest);
        String message = "";
        if (coutSuccess == deleteMultpictureTeacherRequest.getIdPictureList().size()) {
            message = "Xóa ảnh đã chọn thành công";
            return NewDataResponse.setDataCustom(true, message);
        } else if (coutSuccess == 0) {
            message = "Bạn không thể xóa ảnh của người khác";
            return NewDataResponse.setDataCustom(false, message);
        } else {
            message = "Xóa thành công" + " " + "" + coutSuccess + "/" + deleteMultpictureTeacherRequest.getIdPictureList().size() + " " + "\n Bạn không thể xóa ảnh của giáo viên khác!" + "";
            return NewDataResponse.setDataCustom(false, message);
        }
    }

    /**
     * Xem chi tiết album
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/detail/{id}")
    public ResponseEntity findAlbumDetailt(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal, id);
        AlbumDetailTeacherMobileResponse albumDetailTeacherMobileResponse = albumTeacherMobileService.findAlbummobdetailTeacher(principal, id);
        return NewDataResponse.setDataSearch(albumDetailTeacherMobileResponse);
    }

    /**
     * Tạo album
     *
     * @param principal
     * @param createAlbumTeacherRequest
     * @return
     * @throws FirebaseMessagingException
     * @throws IOException
     */
    @RequestMapping(method = RequestMethod.POST, value = "/createalbum")
    public ResponseEntity createAlbum(@CurrentUser UserPrincipal principal, @ModelAttribute @Valid CreateAlbumTeacherRequest createAlbumTeacherRequest) throws FirebaseMessagingException, IOException {
        RequestUtils.getFirstRequest(principal, createAlbumTeacherRequest);
        boolean check = albumTeacherMobileService.createAlbum(principal.getIdSchoolLogin(), principal, createAlbumTeacherRequest);
        return NewDataResponse.setDataCustom(check, MessageConstant.CREATE_ALBUM);
    }

    /**
     * Cập nhật album
     *
     * @param principal
     * @param updateAlbumTeacherRequest
     * @return
     * @throws IOException
     */
    @PostMapping("/updatealbum")
    public ResponseEntity updateAlbum(@CurrentUser UserPrincipal principal, @ModelAttribute UpdateAlbumTeacherRequest updateAlbumTeacherRequest) throws IOException {
        RequestUtils.getFirstRequest(principal, updateAlbumTeacherRequest);
        int coutSuccess = albumTeacherMobileService.updateAlbumTeacher(principal, principal.getIdClassLogin(), updateAlbumTeacherRequest);
        String message = "";
        if (!CollectionUtils.isEmpty(updateAlbumTeacherRequest.getIdPictureList())) {
            if (coutSuccess == updateAlbumTeacherRequest.getIdPictureList().size()) {
                message = "Cập nhật album thành công";
                return NewDataResponse.setDataCustom(true, message);
            } else if (coutSuccess == 0) {
                message = "Bạn không thể xóa ảnh của người khác";
                return NewDataResponse.setDataCustom(false, message);
            } else {
                message = "Xóa thành công" + " " + "" + coutSuccess + "/" + updateAlbumTeacherRequest.getIdPictureList().size() + " " + "\n Bạn không thể xóa ảnh của giáo viên khác!" + "";
                return NewDataResponse.setDataCustom(false, message);
            }
        } else {
            message = "Cập nhật album thành công";
            return NewDataResponse.setDataCustom(true, message);
        }
    }

}
