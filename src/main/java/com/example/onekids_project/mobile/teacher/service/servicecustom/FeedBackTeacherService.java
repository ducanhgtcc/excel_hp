package com.example.onekids_project.mobile.teacher.service.servicecustom;

import com.example.onekids_project.mobile.request.DateTimeRequest;
import com.example.onekids_project.mobile.request.PageNumberRequest;
import com.example.onekids_project.mobile.teacher.request.FeedBackTeacherRequest;
import com.example.onekids_project.mobile.teacher.response.feedback.FeedBackDetailTeacherResponse;
import com.example.onekids_project.mobile.teacher.response.feedback.ListFeedBackTeacherResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public interface FeedBackTeacherService {

    /**
     * find feedback
     * @param principal
     * @param
     * @return
     */
    ListFeedBackTeacherResponse findFeedBackList(UserPrincipal principal, PageNumberRequest pageNumberRequest);

    /**
     * find by id
     * @param principal
     * @param id
     * @return
     */
    FeedBackDetailTeacherResponse findFeedBackDetail(UserPrincipal principal, Long id);

    /**
     * create feedback
     * @param principal
     * @param feedBackTeacherRequest
     * @return
     */
    boolean createFeedBackTeacher(UserPrincipal principal, FeedBackTeacherRequest feedBackTeacherRequest) throws FirebaseMessagingException;
}
