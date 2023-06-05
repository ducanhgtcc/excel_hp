package com.example.onekids_project.controller;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.request.album.AlbumCreateRequest;
import com.example.onekids_project.request.album.SearchAlbumNewRequest;
import com.example.onekids_project.request.album.SearchAlbumRequest;
import com.example.onekids_project.request.album.UpdateAlbumRequest;
import com.example.onekids_project.response.album.AlbumDetailResponse;
import com.example.onekids_project.response.album.ListAlbumNewResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.album.AlbumService;
import com.example.onekids_project.util.RequestUtils;
import com.example.onekids_project.validate.CommonValidate;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/web/album")
public class AlbumController {

    private static final Logger logger = LoggerFactory.getLogger(AlbumController.class);
    @Autowired
    private AlbumService albumService;

    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public ResponseEntity searchAlbumWeb(@CurrentUser UserPrincipal principal, @Valid SearchAlbumNewRequest request) {
        RequestUtils.getFirstRequest(principal, request, "/album/search");
        ListAlbumNewResponse response = albumService.searchAlbumNew(principal, request);
        return NewDataResponse.setDataSearch(response);
    }

    @PostMapping
    public ResponseEntity createAlbum(@CurrentUser UserPrincipal principal, @ModelAttribute @Valid AlbumCreateRequest request) throws IOException, FirebaseMessagingException {
        CommonValidate.checkPlusOrTeacher(principal);
        RequestUtils.getFirstRequest(principal, request);
        boolean check = albumService.createAlbum( principal, request);
        return NewDataResponse.setDataCreate(check);
    }

    @PostMapping("/edit")
    public ResponseEntity updateAlbum(@CurrentUser UserPrincipal principal, @ModelAttribute UpdateAlbumRequest updateAlbumRequest) throws IOException {
        RequestUtils.getFirstRequest(principal, updateAlbumRequest, "Cập nhật album");
        CommonValidate.checkPlusOrTeacher(principal);
        boolean check = albumService.updateAlbum(principal.getIdSchoolLogin(), principal, updateAlbumRequest);
        return NewDataResponse.setDataUpdate(check);
    }

    @GetMapping("/{id}")
    public ResponseEntity getByIdAlbum(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkPlusOrTeacher(principal);
        AlbumDetailResponse albumDetailResponse = albumService.findByIdAlbum(principal, principal.getIdSchoolLogin(), id);
        return NewDataResponse.setDataSearch(albumDetailResponse);
    }

    @GetMapping("/total-rows")
    public ResponseEntity getTotalRow(@CurrentUser UserPrincipal principal, SearchAlbumRequest searchAlbumRequest) {
        RequestUtils.getFirstRequest(principal, searchAlbumRequest);
        CommonValidate.checkPlusOrTeacher(principal);
        Long totalRows = albumService.countAllAlbum(principal, principal.getIdSchoolLogin(), searchAlbumRequest);
        return NewDataResponse.setDataSearch(totalRows);

    }

    @DeleteMapping("/picture/{id}")
    public ResponseEntity deleteByIdPicture(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal, id, "Xóa ảnh trong album");
        boolean check = albumService.deletePicture(principal, principal.getIdSchoolLogin(), id);
        return NewDataResponse.setDataDelete(check);
    }

    @DeleteMapping("/album/{id}")
    public ResponseEntity deleteByIdAlbum(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal, id, "Xóa album");
        CommonValidate.checkPlusOrTeacher(principal);
        boolean check = albumService.deleteAlbum(principal, principal.getIdSchoolLogin(), id);
        return NewDataResponse.setDataDelete(check);

    }

    @DeleteMapping("/multi-album")
    public ResponseEntity deleteMultiAlbum(@CurrentUser UserPrincipal principal, @RequestBody List<Long> idAlbumList) {
        RequestUtils.getFirstRequest(principal, idAlbumList, "Xóa nhiều album");
        CommonValidate.checkPlusOrTeacher(principal);
        boolean check = albumService.deleteMultiAlbum(principal, principal.getIdSchoolLogin(), idAlbumList);
        return NewDataResponse.setDataUpdate(check);

    }

    @PutMapping("/approve/{id}")
    public ResponseEntity updateAlbum(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id, Long idAlbum, @RequestBody Boolean checkApprove) throws FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal, id, "Duyệt ảnh album");
        CommonValidate.checkPlusOrTeacher(principal);
        boolean check = albumService.updatePictureApprove(id, idAlbum, checkApprove, principal);
        return NewDataResponse.setDataUpdate(check);
    }

    @PutMapping("/approve-all/{id}")
    public ResponseEntity updateAllPicture(@CurrentUser UserPrincipal principal, @PathVariable("id") Long idAlbum) throws FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal, idAlbum);
        CommonValidate.checkPlusOrTeacher(principal);
        boolean check = albumService.updateAllPictureApprove(idAlbum, principal);
        return NewDataResponse.setDataCustom(check,"Duyệt ảnh thành công");

    }

    @PutMapping("/approve-multi-album")
    public ResponseEntity updateMultiApproveAlbum(@CurrentUser UserPrincipal principal, @RequestBody List<Long> idAlbumList) throws FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal, idAlbumList);
        CommonValidate.checkPlusOrTeacher(principal);
        boolean check = albumService.updateMultiApproveAlbum(principal, idAlbumList);
        return NewDataResponse.setDataCustom(check,"Duyệt tất cả ảnh thành công");

    }

    @PutMapping("/unapprove-multi-album")
    public ResponseEntity updateMultiUnApproveAlbum(@CurrentUser UserPrincipal principal, @RequestBody List<Long> idAlbumList) {
        RequestUtils.getFirstRequest(principal, idAlbumList);
        CommonValidate.checkPlusOrTeacher(principal);
        boolean check = albumService.updateMultiUnApproveAlbum(principal, principal.getIdSchoolLogin(), idAlbumList, AppConstant.APP_FALSE);
        return NewDataResponse.setDataCustom(check,"Duyệt tất cả ảnh trong album thành công");

    }

    /**
     * Download album
     *
     * @param principal
     * @param id
     * @return
     * @throws IOException
     */
    @RequestMapping(method = RequestMethod.GET, value = "/download/{id}")
    public ResponseEntity dowloadAlbum(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) throws IOException {
        RequestUtils.getFirstRequest(principal, id, "Tải album");
        CommonValidate.checkPlusOrTeacher(principal);
        String link = albumService.downloadAlbum(principal, id);
        return NewDataResponse.setDataCustom(link, "Tải xuống album thành công");
    }
}
