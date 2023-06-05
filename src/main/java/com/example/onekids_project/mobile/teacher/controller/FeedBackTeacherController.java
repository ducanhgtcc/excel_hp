package com.example.onekids_project.mobile.teacher.controller;

import com.example.onekids_project.mobile.request.PageNumberRequest;
import com.example.onekids_project.mobile.teacher.request.FeedBackTeacherRequest;
import com.example.onekids_project.mobile.teacher.response.feedback.FeedBackDetailTeacherResponse;
import com.example.onekids_project.mobile.teacher.response.feedback.ListFeedBackTeacherResponse;
import com.example.onekids_project.mobile.teacher.service.servicecustom.FeedBackTeacherService;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.RequestUtils;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/mob/teacher/feedback")
public class FeedBackTeacherController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private FeedBackTeacherService feedBackTeacherService;

    /**
     * find feedback
     *
     * @param principal
     * @param pageNumberRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity findFeedBack(@CurrentUser UserPrincipal principal, @Valid PageNumberRequest pageNumberRequest) {
        RequestUtils.getFirstRequest(principal,pageNumberRequest);
        ListFeedBackTeacherResponse listFeedBackTeacherResponse = feedBackTeacherService.findFeedBackList(principal, pageNumberRequest);
        return NewDataResponse.setDataSearch(listFeedBackTeacherResponse);

    }

    /**
     * find by id
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity findFeedBackDetail(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequest(principal,id);

        FeedBackDetailTeacherResponse feedBackDetailTeacherResponse = feedBackTeacherService.findFeedBackDetail(principal, id);
        return NewDataResponse.setDataSearch(feedBackDetailTeacherResponse);

    }

    /**
     * create feedback
     *
     * @param principal
     * @param feedBackTeacherRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/create")
    public ResponseEntity createFeedBack(@CurrentUser UserPrincipal principal, @Valid @ModelAttribute FeedBackTeacherRequest feedBackTeacherRequest) throws FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal,feedBackTeacherRequest);
        boolean checkCreate = feedBackTeacherService.createFeedBackTeacher(principal, feedBackTeacherRequest);
        return NewDataResponse.setDataCreate(checkCreate);

    }

}
