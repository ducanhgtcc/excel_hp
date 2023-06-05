package com.example.onekids_project.mobile.teacher.service.servicecustom;

import com.example.onekids_project.mobile.teacher.request.album.*;
import com.example.onekids_project.mobile.teacher.response.album.AlbumDetailTeacherMobileResponse;
import com.example.onekids_project.mobile.teacher.response.album.ListAlbumTeacherMobileResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.google.firebase.messaging.FirebaseMessagingException;

import java.io.IOException;

public interface AlbumTeacherMobileService {


    boolean deleteMultiAlbumTeacher(UserPrincipal userPrincipal, Long idClassLogin, DeleteMultialbumTeacherRequest deleteMultialbumTeacherRequest);

    AlbumDetailTeacherMobileResponse findAlbummobdetailTeacher(UserPrincipal principal, Long id);

    boolean createAlbum(Long idSchoolLogin, UserPrincipal principal, CreateAlbumTeacherRequest createAlbumTeacherRequest) throws FirebaseMessagingException, IOException;

    ListAlbumTeacherMobileResponse findAllAlbumForTeachers(UserPrincipal principal, AlbumTeacherRequest albumTeacherRequest);

    ListAlbumTeacherMobileResponse findAlbumSchoolteacher(UserPrincipal principal, AlbumTeacherRequest albumTeacherRequest);

    ListAlbumTeacherMobileResponse findAlbumClassteacher(UserPrincipal principal, AlbumTeacherRequest albumTeacherRequest);

    int deleteMultiPictureTeacher(UserPrincipal principal, Long idClassLogin, DeleteMultpictureTeacherRequest deleteMultpictureTeacherRequest);

    int updateAlbumTeacher(UserPrincipal principal, Long idClassLogin, UpdateAlbumTeacherRequest updateAlbumTeacherRequest) throws IOException;
}
