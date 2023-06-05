package com.example.onekids_project.service.servicecustom.evaluatekids;

import com.example.onekids_project.request.evaluatekids.*;
import com.example.onekids_project.response.evaluatekids.EvaluateMonthKidResponse;
import com.example.onekids_project.response.evaluatekids.EvaluateMonthResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.google.firebase.messaging.FirebaseMessagingException;

import java.util.List;

public interface EvaluteMonthService {

    /**
     * tìm kiếm đánh giá tháng cho các học sinh
     *
     * @param idSchool
     * @param evaluateDateSearchRequest
     * @return
     */
    List<EvaluateMonthResponse> searchEvaluateMonth(Long idSchool, EvaluateDateSearchRequest evaluateDateSearchRequest);

    /**
     * search by id
     *
     * @param idSchool
     * @param id
     * @return
     */
    EvaluateMonthKidResponse getEvaluateMonthById(Long idSchool, Long id);

    /**
     * lưu đánh giá tháng cho một học sinh chưa bao gồm phản hồi
     *
     * @param idSchool
     * @param evaluateMonthRequest
     * @param principal
     * @return
     */
    EvaluateMonthResponse saveEvaluateMonthOne(Long idSchool, EvaluateMonthRequest evaluateMonthRequest, UserPrincipal principal);

    /**
     * lưu đánh giá tháng cho nhiều học sinh chưa bao gồm phản hồi
     *
     * @param idSchool
     * @param evaluateMonthRequestList
     * @param principal
     * @return
     */
    boolean saveEvaluateMonthMany(Long idSchool, List<EvaluateMonthRequest> evaluateMonthRequestList, UserPrincipal principal);

    /**
     * cập nhật xét duyệt tháng cho một học sinh
     *
     * @param idSchool
     * @param evaluateDateApprovedRequest
     * @return
     */
    boolean updateIsApprovedMonthOne(Long idSchool, EvaluateDateApprovedRequest evaluateDateApprovedRequest) throws FirebaseMessagingException;

    /**
     * cập nhật xét duyệt tháng cho nhiều học sinh
     *
     * @param idSchool
     * @param evaluateDateApprovedRequestList
     * @return
     */
    boolean updateIsApprovedMonthMany(Long idSchool, List<EvaluateDateApprovedRequest> evaluateDateApprovedRequestList) throws FirebaseMessagingException;

    /**
     * Tìm kiếm đánh giá một tháng cho một học sinh
     *
     * @param idSchool
     * @param evaluateWeekSearchKidRequest
     * @return
     */
    EvaluateMonthKidResponse searchEvaluateMonthKid(Long idSchool, EvaluateWeekSearchKidRequest evaluateWeekSearchKidRequest);

    /**
     * lưu đánh giá một tháng cho một học sinh bao gồm phản hồi
     *
     * @param idSchool
     * @param evaluateMonthDetailRequest
     * @param principal
     * @return
     */
    EvaluateMonthResponse saveEvaluateMonthDetailOne(Long idSchool, EvaluateMonthDetailRequest evaluateMonthDetailRequest, UserPrincipal principal) throws FirebaseMessagingException;

    /**
     * save evaluate month common
     *
     * @param userPrincipal
     * @param evaluateMonthCommonRequest
     * @return
     */
    boolean saveEvaluateMonthCommon(UserPrincipal userPrincipal, EvaluateMonthCommonRequest evaluateMonthCommonRequest) throws FirebaseMessagingException;
}
