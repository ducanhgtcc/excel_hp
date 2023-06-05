package com.example.onekids_project.mobile.parent.service.servicecustom;

import com.example.onekids_project.mobile.parent.request.absent.AbsentMobileRequest;
import com.example.onekids_project.mobile.parent.response.absentletter.AbsentLetterDetailMobileResponse;
import com.example.onekids_project.mobile.parent.response.absentletter.ListAbsentLetterMobileResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface AbsentLetterMobileService {

    ListAbsentLetterMobileResponse findAbsentMoblie(UserPrincipal principal, Pageable pageable, LocalDateTime dateTime);

    boolean absentRevoke(Long id);

    AbsentLetterDetailMobileResponse findAbsentDetailMobile(UserPrincipal principal, Long id);

    boolean createAbsent(UserPrincipal principal, AbsentMobileRequest absentMobileRequest) throws FirebaseMessagingException;

    AbsentLetterDetailMobileResponse findDetailMessageTeacher(UserPrincipal principal, Long id);
}
