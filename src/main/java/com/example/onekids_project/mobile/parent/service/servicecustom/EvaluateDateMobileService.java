package com.example.onekids_project.mobile.parent.service.servicecustom;

import com.example.onekids_project.mobile.parent.response.evaluate.DateOfMonthMobileResponse;
import com.example.onekids_project.mobile.parent.response.evaluate.EvaluateDateMobileResponse;
import com.example.onekids_project.mobile.parent.response.evaluate.EvaluateStatisticalMobileResponse;
import com.example.onekids_project.mobile.request.ContentMobileRequest;
import com.example.onekids_project.security.model.UserPrincipal;
import com.google.firebase.messaging.FirebaseMessagingException;

import java.time.LocalDate;
import java.util.List;

public interface EvaluateDateMobileService {

    EvaluateStatisticalMobileResponse evaluateStatisticalUnread(UserPrincipal principal);

    /**
     * evaluate date of kid
     * @param principal
     * @param localDate
     * @return
     */
    EvaluateDateMobileResponse findEvaluateByIdKids(UserPrincipal principal, LocalDate localDate);

    /**
     * get total parentunread of month
     * @param principal
     * @param localDate
     * @return
     */
    int getTatalParentUnreadOfMonth(UserPrincipal principal, LocalDate localDate);

    /**
     *
     * @param principal
     * @param localDate
     * @return
     */
    List<DateOfMonthMobileResponse> totalEvaluateDateOfMonth(UserPrincipal principal, LocalDate localDate);

    /**
     * create parent reply
     * @param principal
     * @param id
     * @param content
     * @return
     */
    boolean createParentReply(UserPrincipal principal, Long id, String content) throws FirebaseMessagingException;

    /**
     * update parent reply
     * @param principal
     * @param id
     * @param content
     * @return
     */
    boolean updateParentReply(UserPrincipal principal, Long id, String content);

    /**
     * thu hoi
     * @param principal
     * @param id
     * @return
     */
    boolean revokeParentReply(UserPrincipal principal, Long id);
}
