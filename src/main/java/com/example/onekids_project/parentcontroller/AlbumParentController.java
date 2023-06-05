package com.example.onekids_project.parentcontroller;

import com.example.onekids_project.mobile.parent.response.album.AlbumDetailMobileResponse;
import com.example.onekids_project.mobile.parent.response.album.ExAlbumKidsResponse;
import com.example.onekids_project.mobile.parent.response.album.ListExAlbumKidsMobileResponse;
import com.example.onekids_project.mobile.parent.service.servicecustom.AlbumMobileService;
import com.example.onekids_project.mobile.request.DateNotNullRequest;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.album.AlbumService;
import com.example.onekids_project.util.RequestUtils;
import com.example.onekids_project.validate.CommonValidate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("web/parent/album")
public class AlbumParentController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AlbumMobileService albumMobileService;
    @Autowired
    private AlbumService albumService;

    /**
     * tìm kiếm danh sách album
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity findAllAlbum(@CurrentUser UserPrincipal principal, @RequestParam String albumType, DateNotNullRequest date) {
        RequestUtils.getFirstRequest(principal, albumType);
        CommonValidate.checkDataParent(principal);
        Pageable pageable = PageRequest.of(1, 100);
        ListExAlbumKidsMobileResponse listExAlbumKidsMobileResponse = null;
        if (StringUtils.isBlank(albumType)) {
            listExAlbumKidsMobileResponse = albumMobileService.findAlbumAll(principal, pageable, null);
        } else if (StringUtils.equals("Trường", albumType)) {
            listExAlbumKidsMobileResponse = albumMobileService.findAlbumforSchool(principal, pageable, null);
        } else if (StringUtils.equals("Lớp", albumType)) {
            listExAlbumKidsMobileResponse = albumMobileService.findAlbumforClassmob(principal, pageable, null);
        }
        if (date.getDate() != null) {
            List<ExAlbumKidsResponse> listFiler = listExAlbumKidsMobileResponse.getListAllAlbum().stream().filter(x -> x.getCreatedDate().toLocalDate().isEqual(date.getDate())).collect(Collectors.toList());
            listExAlbumKidsMobileResponse.setListAllAlbum(listFiler);
        }

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
        RequestUtils.getFirstRequest(principal, id, "Xem chi tiết album");
        CommonValidate.checkDataParent(principal);
        AlbumDetailMobileResponse albumDetailMobileResponse = albumMobileService.findAlbummobdetail(principal, id);
        return NewDataResponse.setDataSearch(albumDetailMobileResponse);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/download/{id}")
    public ResponseEntity dowloadAlbum(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) throws IOException {
        RequestUtils.getFirstRequest(principal, id);
        String link = albumService.downloadAlbum(principal, id);
        return NewDataResponse.setDataCustom(link, "Tải xuống album thành công");
    }

}
