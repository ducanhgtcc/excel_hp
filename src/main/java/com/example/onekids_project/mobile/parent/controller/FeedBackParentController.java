package com.example.onekids_project.mobile.parent.controller;

import com.example.onekids_project.mobile.parent.request.FeedBackParentRequest;
import com.example.onekids_project.mobile.parent.response.feedback.FeedBackDetailParentResponse;
import com.example.onekids_project.mobile.parent.response.feedback.ListFeedBackParentResponse;
import com.example.onekids_project.mobile.parent.service.servicecustom.FeedBackParentService;
import com.example.onekids_project.mobile.request.DateTimeRequest;
import com.example.onekids_project.response.common.DataResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.RequestUtils;
import com.example.onekids_project.validate.CommonValidate;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/mob/parent/feedback")
public class FeedBackParentController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private FeedBackParentService feedBackParentService;

    /**
     * find feedback
     *
     * @param principal
     * @param dateTimeRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity findFeedBack(@CurrentUser UserPrincipal principal, DateTimeRequest dateTimeRequest) {
        RequestUtils.getFirstRequest(principal, dateTimeRequest);
        CommonValidate.checkDataParent(principal);
        ListFeedBackParentResponse data = feedBackParentService.findFeedBackList(principal, dateTimeRequest.getDateTime());
        return NewDataResponse.setDataSearch(data);

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
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkDataParent(principal);
        FeedBackDetailParentResponse data = feedBackParentService.findFeedBackDetail(principal, id);
        return NewDataResponse.setDataSearch(data);

    }

    /**
     * create feedback
     *
     * @param principal
     * @param feedBackParentRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createFeedBack(@CurrentUser UserPrincipal principal, @Valid @ModelAttribute FeedBackParentRequest feedBackParentRequest) throws FirebaseMessagingException, IOException {
        RequestUtils.getFirstRequest(principal, feedBackParentRequest);
        boolean checkCreate = feedBackParentService.createFeedBackParent(principal, feedBackParentRequest);
        return NewDataResponse.setDataCreate(checkCreate);
    }
}
