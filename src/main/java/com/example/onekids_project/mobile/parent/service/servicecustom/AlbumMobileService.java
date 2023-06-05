package com.example.onekids_project.mobile.parent.service.servicecustom;

import com.example.onekids_project.mobile.parent.response.album.AlbumDetailMobileResponse;
import com.example.onekids_project.mobile.parent.response.album.ListExAlbumKidsMobileResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface AlbumMobileService {

    AlbumDetailMobileResponse findAlbummobdetail(UserPrincipal principal, Long id);

    ListExAlbumKidsMobileResponse findAlbumforClassmob(UserPrincipal principal, Pageable pageable, LocalDateTime dateTime);

    ListExAlbumKidsMobileResponse findAlbumAll(UserPrincipal principal, Pageable pageable, LocalDateTime dateTime);

    ListExAlbumKidsMobileResponse findAlbumforSchool(UserPrincipal principal, Pageable pageable, LocalDateTime dateTime);
}
