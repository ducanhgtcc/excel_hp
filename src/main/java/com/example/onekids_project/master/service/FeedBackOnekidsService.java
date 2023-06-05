package com.example.onekids_project.master.service;

import com.example.onekids_project.master.request.FeedBackOnekidsRequest;
import com.example.onekids_project.master.request.SearchFeedBackOneKidsRequest;
import com.example.onekids_project.master.response.FeedBackOnekidsResponse;
import com.example.onekids_project.master.response.feedback.FeedbackDetailAdminResponse;
import com.example.onekids_project.master.response.feedback.ListFeedbackAdminResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.google.firebase.messaging.FirebaseMessagingException;

import java.io.IOException;
import java.util.List;

public interface FeedBackOnekidsService {

    ListFeedbackAdminResponse searchFeedbackAdmin(SearchFeedBackOneKidsRequest searchFeedBackOneKidsRequest);

    FeedbackDetailAdminResponse viewFeedbackDetail(Long id);

    FeedBackOnekidsResponse findFeedBackOnekidsById(Long id);

    boolean deleteFeedBackById(Long id);

    boolean createFeedbackHidden(UserPrincipal principal, FeedBackOnekidsRequest feedBackOnekidsRequest);

    boolean deleteMultiFeedBackById(List<Long> idList);
}
