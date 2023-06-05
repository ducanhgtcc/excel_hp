package com.example.onekids_project.service.servicecustom.evaluatekids;

import com.example.onekids_project.request.evaluatekids.*;
import com.example.onekids_project.response.evaluatekids.EvaluateWeekKidResponse;
import com.example.onekids_project.response.evaluatekids.EvaluateWeekResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.google.firebase.messaging.FirebaseMessagingException;

import java.util.List;

public interface EvaluateWeekService {
    /**
     * tìm kiếm đánh giá tuần của các học sinh
     *
     * @param idSchool
     * @param evaluateDateSearchRequest
     * @return
     */
    List<EvaluateWeekResponse> searchEvaluateWeek(Long idSchool, EvaluateDateSearchRequest evaluateDateSearchRequest);

    /**
     * tìm kiếm đánh giá tuần của các học sinh
     *
     * @param idSchool
     * @param id
     * @return
     */
    EvaluateWeekKidResponse getEvaluateWeekById(Long idSchool, Long id);

    /**
     * lưu đánh giá tuần cho một học sinh chưa bao gồm phản hồi
     *
     * @param idSchool
     * @param evaluateWeekRequest
     * @param principal
     * @return
     */
    EvaluateWeekResponse saveEvaluateWeekOne(Long idSchool, EvaluateWeekRequest evaluateWeekRequest, UserPrincipal principal);

    /**
     * save week common
     *
     * @param userPrincipal
     * @param evaluateWeekCommonRequest
     * @return
     */
    boolean saveEvaluateWeekCommon(UserPrincipal userPrincipal, EvaluateWeekCommonRequest evaluateWeekCommonRequest) throws FirebaseMessagingException;

    /**
     * lưu đánh giá tuần cho nhiều học sinh chưa bao gồm phản hồi
     *
     * @param idSchool
     * @param evaluateWeekRequestList
     * @param principal
     * @return
     */
    boolean saveEvaluateWeekMany(Long idSchool, List<EvaluateWeekRequest> evaluateWeekRequestList, UserPrincipal principal);

    /**
     * cập nhật xét duyệt tuần cho một học sinh
     *
     * @param idSchool
     * @param evaluateDateApprovedRequest
     * @return
     */
    boolean updateIsApprovedWeekOne(Long idSchool, EvaluateDateApprovedRequest evaluateDateApprovedRequest) throws FirebaseMessagingException;

    /**
     * cập nhật xét duyệt tuần cho nhiều học sinh
     *
     * @param idSchool
     * @param evaluateDateApprovedRequestList
     * @return
     */
    boolean updateIsApprovedWeekMany(Long idSchool, List<EvaluateDateApprovedRequest> evaluateDateApprovedRequestList) throws FirebaseMessagingException;

    /**
     * lưu đánh giá một tuần cho một học sinh có kèm phản hồi
     *
     * @param principal
     * @param evaluateWeekDetailRequest
     * @param principal
     * @return
     */
    EvaluateWeekResponse saveEvaluateWeekDetailOne(EvaluateWeekDetailRequest evaluateWeekDetailRequest, UserPrincipal principal) throws FirebaseMessagingException;

    /**
     * tìm kiếm đánh giá một tuần cho một học sinh
     *
     * @param idSchool
     * @param evaluateWeekSearchKidRequest
     * @return
     */
    EvaluateWeekKidResponse searchEvaluateWeekKid(Long idSchool, EvaluateWeekSearchKidRequest evaluateWeekSearchKidRequest);

}
