package com.example.onekids_project.mobile.plus.service.servicecustom;

import com.example.onekids_project.mobile.plus.request.KidsPageNumberPlusRequest;
import com.example.onekids_project.mobile.plus.request.KidsSearchPlusRequest;
import com.example.onekids_project.mobile.plus.request.evaluate.*;
import com.example.onekids_project.mobile.plus.response.evaluate.*;
import com.example.onekids_project.mobile.request.ContentAndIdMobileRequest;
import com.example.onekids_project.mobile.response.ReplyTypeEditObject;
import com.example.onekids_project.mobile.teacher.response.evaluate.EvaluateStatusResponse;
import com.example.onekids_project.request.evaluatekids.EvaluateClassDateRequest;
import com.example.onekids_project.security.model.UserPrincipal;
import com.google.firebase.messaging.FirebaseMessagingException;

import java.util.List;

public interface EvaluatePlusService {
    List<EvaluateDatePlusResponse> findEvaluateKisDate(UserPrincipal principal, EvaluateClassDateRequest request);

    List<Integer> findEvaluateDateOfMonth(UserPrincipal principal, EvaluateClassDateRequest request);

    ListEvaluateDateCreatePlusResponse createEvaluateDate(UserPrincipal principal, ListEvaluateDateCreatePlusRequest request) throws FirebaseMessagingException;

    List<EvaluatePlusResponse> findEvaluateKidsWeek(UserPrincipal principal, EvaluateClassDateRequest request);

    ListEvaluateCreatePlusResponse createEvaluateWeek(UserPrincipal principal, ListEvaluateCreatePlusRequest request) throws FirebaseMessagingException;

    List<EvaluatePlusResponse> findEvaluateKidsMonth(UserPrincipal principal, EvaluateClassDateRequest request);

    ListEvaluateCreatePlusResponse createEvaluateMonth(UserPrincipal principal, ListEvaluateCreatePlusRequest request) throws FirebaseMessagingException;

    List<EvaluatePeriodicPlusResponse> findEvaluateKidsPeriodic(UserPrincipal principal, EvaluateClassDateRequest request);

    ListEvaluateCreatePlusResponse createEvaluatePeriodic(UserPrincipal principal, ListEvaluatePeriodicCreatePlusRequest request) throws FirebaseMessagingException;

    List<Integer> statisticalPeriodicOfMonthDate(UserPrincipal principal, EvaluateClassDateRequest request);

    List<StatisticalOfMonthPlusResponse> statisticalOfMonth(UserPrincipal principal, EvaluateClassDateRequest request);

    List<Integer> statisticalDateAndPeriodic(UserPrincipal principal, EvaluateClassDateRequest request);

    TotalPlusResponse findSchoolUnread(UserPrincipal principal, KidsSearchPlusRequest request);

    EvaluateDateKidPlusResponse getEvaluateDateKid(UserPrincipal principal, KidsSearchPlusRequest request);

    int countKidDateSchoolUnread(UserPrincipal principal, KidsSearchPlusRequest request);

    EvaluateDateHavePlusResponse findKidDateHaveOfMonth(UserPrincipal principal, KidsSearchPlusRequest request);

    ReplyTypeEditObject createKidDateReply(UserPrincipal principal, ContentAndIdMobileRequest request) throws FirebaseMessagingException;

    ReplyTypeEditObject revokeKidDateReplye(UserPrincipal principal, Long id);

    ListEvaluateKidPlusResponse getEvaluateWeekKid(UserPrincipal principal, KidsPageNumberPlusRequest request);

    boolean viewEvaluateWeekKid(UserPrincipal principal, Long id);

    ReplyTypeEditObject createKidWeekReply(UserPrincipal principal, ContentAndIdMobileRequest request) throws FirebaseMessagingException;

    ReplyTypeEditObject revokeKidWeekReplye(UserPrincipal principal, Long id);

    ListEvaluateKidPlusResponse getEvaluateMonthKid(UserPrincipal principal, KidsPageNumberPlusRequest request);

    boolean viewEvaluateMonthKid(UserPrincipal principal, Long id);

    ReplyTypeEditObject createKidMonthReply(UserPrincipal principal, ContentAndIdMobileRequest request) throws FirebaseMessagingException;

    ReplyTypeEditObject revokeKidMonthReply(UserPrincipal principal, Long id);

    boolean viewEvaluatePeriodicKid(UserPrincipal principal, Long id);

    ReplyTypeEditObject createKidPeriodicReply(UserPrincipal principal, ContentAndIdMobileRequest request) throws FirebaseMessagingException;

    ReplyTypeEditObject revokeKidPeriodicReply(UserPrincipal principal, Long id);

    ListEvaluateKidPlusResponse getEvaluatePeriodicKid(UserPrincipal principal, KidsPageNumberPlusRequest request);

    List<EvaluateStatusKidDayPlusResponse> findEvaluateStatusKidDate(UserPrincipal principal, EvaluatePlusRequest request);

    List<EvaluateStatusKidMonthPlusResponse> findEvaluateStatusKidMonth(UserPrincipal principal, EvaluatePlusRequest request);

    EvaluateStatusResponse getEvaluateStatus(UserPrincipal principal, Long idClass);

    boolean approvedPeriodic(UserPrincipal principal, IdEvaluateListRequest request) throws FirebaseMessagingException;

    boolean approvedMonth(UserPrincipal principal, IdEvaluateListRequest request) throws FirebaseMessagingException;

    boolean approvedWeek(UserPrincipal principal, IdEvaluateListRequest request) throws FirebaseMessagingException;

    boolean approvedDate(UserPrincipal principal, IdEvaluateListRequest request) throws FirebaseMessagingException;
}
