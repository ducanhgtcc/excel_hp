package com.example.onekids_project.service.servicecustom.evaluatekids;

import com.example.onekids_project.request.evaluatekids.*;
import com.example.onekids_project.request.kids.SearchKidsClassRequest;
import com.example.onekids_project.response.evaluatekids.EvaluatePeriodicKidResponse;
import com.example.onekids_project.response.evaluatekids.EvaluatePeriodicResponse;
import com.example.onekids_project.response.kids.KidOtherResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.google.firebase.messaging.FirebaseMessagingException;

import java.util.List;

public interface EvaluatePeriodicService {
    /**
     * search kids in class
     *
     * @param idSchool
     * @param searchKidsClassRequest
     * @return
     */
    List<KidOtherResponse> searchKidsClass(Long idSchool, SearchKidsClassRequest searchKidsClassRequest);

    /**
     * create evaluate periodic for one kid
     *
     * @param principal
     * @param idKid
     * @param evaluatePeriodicCreateRequest
     * @return
     */
    boolean createEvaluatePeriodic(UserPrincipal principal, Long idKid, EvaluatePeriodicCreateRequest evaluatePeriodicCreateRequest) throws FirebaseMessagingException;

    /**
     * create evaluate periodic for many kid
     *
     * @param principal
     * @param evaluatePeriodicCreateManyRequest
     * @return
     */
    boolean createEvaluatePeriodicMany(UserPrincipal principal, EvaluatePeriodicCreateManyRequest evaluatePeriodicCreateManyRequest) throws FirebaseMessagingException;

    /**
     * tìm kiếm đánh giá định kỳ cho các học sinh
     *
     * @param idSchool
     * @param evaluatePeriodicSearchRequest
     * @return
     */
    List<EvaluatePeriodicResponse> searchEvaluatePeriodic(Long idSchool, EvaluatePeriodicSearchRequest evaluatePeriodicSearchRequest);


    /**
     * lưu đánh giá định kỳ cho một học sinh chưa bao gồm phải hồi
     *
     * @param idSchool
     * @param evaluatePeriodicRequest
     * @param principal
     * @return
     */
    EvaluatePeriodicResponse saveEvaluatePeriodicOne(Long idSchool, EvaluatePeriodicRequest evaluatePeriodicRequest, UserPrincipal principal);

    /**
     * lưu đánh giá định kỳ cho nhiều học sinh chưa bao gồm phản hồi
     *
     * @param idSchool
     * @param evaluatePeriodicRequestList
     * @param principal
     * @return
     */
    boolean saveEvaluatePeriodicMany(Long idSchool, List<EvaluatePeriodicRequest> evaluatePeriodicRequestList, UserPrincipal principal);

    /**
     * update approved
     *
     * @param idSchool
     * @param evaluateDateApprovedRequest
     * @return
     */
    boolean updateIsApprovedPeriodicOne(Long idSchool, EvaluateDateApprovedRequest evaluateDateApprovedRequest) throws FirebaseMessagingException;

    /**
     * update approved many kid
     *
     * @param idSchool
     * @param evaluateDateApprovedRequestList
     * @return
     */
    boolean updateIsApprovedPeriodicMany(Long idSchool, List<EvaluateDateApprovedRequest> evaluateDateApprovedRequestList) throws FirebaseMessagingException;

    /**
     * tìm kiếm các lần đánh giá định kỳ của một học sinh
     *
     * @param idSchool
     * @param idKid
     * @return
     */
    List<EvaluatePeriodicKidResponse> searchEvaluatePeriodicDetaial(Long idSchool, Long idKid);

    /**
     * lưu đánh giá định kỳ cho một học sinh bao gồm phàn hồi
     *
     * @param idSchool
     * @param evaluatePeriodicDetailRequest
     * @param principal
     * @return
     */
    EvaluatePeriodicResponse saveEvaluatePeriodicDetailOne(Long idSchool, EvaluatePeriodicDetailRequest evaluatePeriodicDetailRequest, UserPrincipal principal) throws FirebaseMessagingException;
}
