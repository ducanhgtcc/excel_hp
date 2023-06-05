package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.dto.FeedbackDTO;
import com.example.onekids_project.request.feedback.FeedBackRequest;
import com.example.onekids_project.request.feedback.SearchParentFeedbackRequest;
import com.example.onekids_project.request.feedback.SearchTeacherFeedbackRequest;
import com.example.onekids_project.request.feedback.UpdateFeedbackRequest;
import com.example.onekids_project.response.feedback.FeedBackResponse;
import com.example.onekids_project.response.feedback.ListFeedBackResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FeedBackSerVice {

    ListFeedBackResponse findAllFeedBack(Long idSchoolLogin, Pageable pageable);

    ListFeedBackResponse searchTitle(UserPrincipal principal, SearchParentFeedbackRequest request);

    FeedbackDTO findByIdFeedback(Long id);

    ListFeedBackResponse findAllTeacherFeedBack(Long idSchoolLogin, Pageable pageable);

    FeedbackDTO findByIdTeacherFeedback(Long id);

    FeedBackResponse updateFeedback(Long idSchoolLogin, UserPrincipal userPrincipal, UpdateFeedbackRequest feedBackEditRequest) throws FirebaseMessagingException;

    FeedBackRequest updateRead(Long id, List<FeedBackRequest> feedBackResponses);

    ListFeedBackResponse searchTitleTeacher(UserPrincipal principal, SearchTeacherFeedbackRequest request);
}
