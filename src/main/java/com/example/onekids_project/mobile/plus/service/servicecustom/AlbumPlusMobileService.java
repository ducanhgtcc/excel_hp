package com.example.onekids_project.mobile.plus.service.servicecustom;

import com.example.onekids_project.mobile.plus.request.album.*;
import com.example.onekids_project.mobile.plus.response.album.AlbumDetailPlusMobileResponse;
import com.example.onekids_project.mobile.plus.response.album.DeleteMultialbumPlusRequest;
import com.example.onekids_project.mobile.plus.response.album.ListAlbumPlusMobileResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.google.firebase.messaging.FirebaseMessagingException;

import java.io.IOException;

public interface AlbumPlusMobileService {


    ListAlbumPlusMobileResponse findAllAlbumForPlus(UserPrincipal principal, SearchAlbumPlusRequest searchAlbumPlusRequest);

    boolean deleteMultiAlbumPlus(UserPrincipal principal, Long idClassLogin, UpdateApproveMultialbumPlusRequest deleteMultialbumPlusRequest);

    boolean updateApprovedAlbum(UserPrincipal principal, UpdateApproveMultialbumPlusRequest request) throws FirebaseMessagingException;

    boolean deleteMultiAlbum(UserPrincipal principal, DeleteMultialbumPlusRequest request);

    AlbumDetailPlusMobileResponse findDetailalbumplus(UserPrincipal principal, Long id);

    boolean deleteMultiPicture(UserPrincipal principal, DeleteMultpicturePlusRequest request);

    boolean approvePicture(UserPrincipal principal, ApproveMultpicturePlusRequest request) throws FirebaseMessagingException;

    boolean createAlbumPlus(UserPrincipal principal, CreateAlbumPlusRequest request) throws IOException, FirebaseMessagingException;

    boolean updateAlbum(UserPrincipal principal, UpdateAlbumPlusRequest request) throws IOException;
}
