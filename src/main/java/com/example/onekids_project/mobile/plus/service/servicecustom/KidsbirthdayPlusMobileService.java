package com.example.onekids_project.mobile.plus.service.servicecustom;

import com.example.onekids_project.mobile.plus.request.SearchMessagePlusRequest;
import com.example.onekids_project.mobile.plus.request.UpdatePlusRevokeRequest;
import com.example.onekids_project.mobile.plus.request.UpdatePlusSendReplyRequest;
import com.example.onekids_project.mobile.plus.request.birthday.BirthdayPlusRequest;
import com.example.onekids_project.mobile.plus.request.birthday.SearchKidsBirthdayPlusRequest;
import com.example.onekids_project.mobile.plus.response.*;
import com.example.onekids_project.mobile.plus.response.birthday.CoutbirthDayPlusResponse;
import com.example.onekids_project.mobile.plus.response.birthday.ListKidsBirthdayPlusResponse;
import com.example.onekids_project.mobile.teacher.response.birthday.SumBirthdayTeacherResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.google.firebase.messaging.FirebaseMessagingException;

import java.time.LocalDateTime;

public interface KidsbirthdayPlusMobileService {


    ListKidsBirthdayPlusResponse searchKidsBirthdayPlus(UserPrincipal principal, SearchKidsBirthdayPlusRequest request);

    ListKidsBirthdayPlusResponse findBirthWeekList(UserPrincipal principal, SearchKidsBirthdayPlusRequest request, LocalDateTime datatime);

    ListKidsBirthdayPlusResponse searchMonthBirthdayPlus(UserPrincipal principal, SearchKidsBirthdayPlusRequest request);

    CoutbirthDayPlusResponse coutbirthday(UserPrincipal principal);

    boolean sendKidsBirthday(UserPrincipal principal, BirthdayPlusRequest request) throws FirebaseMessagingException;
}
