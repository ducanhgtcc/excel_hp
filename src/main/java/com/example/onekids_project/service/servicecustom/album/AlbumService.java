package com.example.onekids_project.service.servicecustom.album;

import com.example.onekids_project.request.album.*;
import com.example.onekids_project.response.album.AlbumDetailResponse;
import com.example.onekids_project.response.album.ListAlbumNewResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.google.firebase.messaging.FirebaseMessagingException;

import java.io.IOException;
import java.util.List;

public interface AlbumService {

    boolean createAlbum(UserPrincipal principal, AlbumCreateRequest request) throws IOException, FirebaseMessagingException;

    boolean updateAlbum(Long idSchool, UserPrincipal principal, UpdateAlbumRequest updateAlbumRequest) throws IOException;

    AlbumDetailResponse findByIdAlbum(UserPrincipal principal, Long idSchool, Long idAlbum);

    Long countAllAlbum(UserPrincipal principal, Long idSchool, SearchAlbumRequest searchAlbumRequest);

    boolean deletePicture(UserPrincipal principal, Long idSchool, Long idPicture);

    boolean deleteAlbum(UserPrincipal principal, Long idSchool, Long idAlbum);

    boolean deleteMultiAlbum(UserPrincipal principal, Long idSchool, List<Long> idAlbumList);

    boolean updatePictureApprove(Long id, Long idAlbum, Boolean checkApprove, UserPrincipal principal);

    boolean updateAllPictureApprove(Long idSchool, UserPrincipal principal) throws FirebaseMessagingException;

    boolean updateMultiApproveAlbum(UserPrincipal principal, List<Long> idAlbumList) throws FirebaseMessagingException;

    boolean updateMultiUnApproveAlbum(UserPrincipal principal, Long idSchoolLogin, List<Long> idAlbumList, Boolean status);

    ListAlbumNewResponse searchAlbumNew(UserPrincipal principal, SearchAlbumNewRequest request);

    String downloadAlbum(UserPrincipal principal, Long id) throws IOException;
}
