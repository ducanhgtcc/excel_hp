package com.example.onekids_project.mobile.parent.service.servicecustom;

import com.example.onekids_project.mobile.parent.request.FeedBackParentRequest;
import com.example.onekids_project.mobile.parent.response.feedback.FeedBackDetailParentResponse;
import com.example.onekids_project.mobile.parent.response.feedback.ListFeedBackParentResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.google.firebase.messaging.FirebaseMessagingException;

import java.io.IOException;
import java.time.LocalDateTime;

public interface FeedBackParentService {
    /**
     * find feedback
     * @param principal
     * @param localDateTime
     * @return
     */
    ListFeedBackParentResponse findFeedBackList(UserPrincipal principal, LocalDateTime localDateTime);

    /**
     * find by id
     * @param principal
     * @param id
     * @return
     */
    FeedBackDetailParentResponse findFeedBackDetail(UserPrincipal principal, Long id);

    /**
     * create feedback
     * @param principal
     * @param feedBackParentRequest
     * @return
     */
    boolean createFeedBackParent(UserPrincipal principal, FeedBackParentRequest feedBackParentRequest) throws FirebaseMessagingException, IOException;


}
