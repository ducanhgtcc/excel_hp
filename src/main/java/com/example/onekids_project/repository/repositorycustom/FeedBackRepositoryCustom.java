package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.user.FeedBack;
import com.example.onekids_project.master.request.SearchFeedBackOneKidsRequest;
import com.example.onekids_project.mobile.plus.request.feedbackplus.FeedbackPlusRequest;
import com.example.onekids_project.mobile.request.PageNumberRequest;
import com.example.onekids_project.request.feedback.SearchParentFeedbackRequest;
import com.example.onekids_project.request.feedback.SearchTeacherFeedbackRequest;
import com.example.onekids_project.security.model.UserPrincipal;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface FeedBackRepositoryCustom {

    List<FeedBack> findAllFeedBack(Long idSchool, Pageable pageable);

    List<FeedBack> searchTitle(Long idSchool, SearchParentFeedbackRequest request);

    List<FeedBack> searchFeedbackAdmin(SearchFeedBackOneKidsRequest request, List<Long> idSchoolList);

    long countSearchFeedbackAdmin(SearchFeedBackOneKidsRequest request, List<Long> idSchoolList);

    List<FeedBack> findFeedBackParentList(Long idSchool, Long idKid, LocalDateTime localDateTime);

    long getCountParent(Long idSchool, Long idKid, LocalDateTime localDateTime);


    List<FeedBack> searchTitleTeacher(Long idSchool, SearchTeacherFeedbackRequest searchTeacherFeedbackRequest);

    /**
     * find feedback for teacher
     *
     * @return
     */
    List<FeedBack> findFeedBackTeacherList(UserPrincipal principal, PageNumberRequest pageNumberRequest);

    long countTotalAccount(Long idSchool, SearchParentFeedbackRequest request);

    long countTotalAccountTC(Long idSchool, SearchTeacherFeedbackRequest request);

    List<FeedBack> findFeedBackPlus(Long idSchool, FeedbackPlusRequest request);
}


