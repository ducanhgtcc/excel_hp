package com.example.onekids_project.service.servicecustom.evaluatekids;

import com.example.onekids_project.dto.ListIdKidDTO;
import com.example.onekids_project.importexport.model.EvaluateDateKidModel;
import com.example.onekids_project.request.evaluatekids.*;
import com.example.onekids_project.response.evaluatekids.*;
import com.example.onekids_project.response.excel.ExcelNewResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.google.firebase.messaging.FirebaseMessagingException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface EvaluateDateService {

    /**
     * tìm kiếm đánh giá các học sinh trong một ngày cho tab ngày
     *
     * @param idSchool
     * @param evaluateDateSearchRequest
     * @return
     */
    List<EvaluateDateResponse> searchEvaluateKidsDate(Long idSchool, EvaluateDateSearchRequest evaluateDateSearchRequest);

    /**
     * tìm kiếm đánh giá các học sinh trong một ngày cho tab chi tiết ngày
     *
     * @param idSchool
     * @param evaluateDateSearchRequest
     * @return
     */
    List<EvaluateDateKidsBriefResponse> searchEvaluateKidsBriefDate(Long idSchool, EvaluateDateSearchRequest evaluateDateSearchRequest);

    /**
     * tìm kiếm đánh giá một học sinh trong ngày theo id
     *
     * @param idSchool
     * @param id
     * @return
     */
    EvaluateDateKidResponse findEvaluateKidsDateById(Long idSchool, Long id, UserPrincipal principal);

    /**
     * cập nhật duyệt một học sinh trong đánh giá ngày
     *
     * @param idSchool
     * @param evaluateDateApprovedRequest
     * @return
     */
    boolean updateIsApprovedOneKidsDate(Long idSchool, EvaluateDateApprovedRequest evaluateDateApprovedRequest) throws FirebaseMessagingException;

    /**
     * cập nhật duyệt cho nhiều học sinh trong đánh giá ngày
     *
     * @param idSchool
     * @param evaluateDateApprovedRequestList
     * @return
     */
    boolean updateIsApprovedManyKidsDate(Long idSchool, List<EvaluateDateApprovedRequest> evaluateDateApprovedRequestList) throws FirebaseMessagingException;

    /**
     * lưu đánh giá cho một học sinh theo ngày tab ngày
     *
     * @param userPrincipal
     * @param evaluateDateKidRequest
     * @return
     */
    EvaluateDateResponse saveEvaluateOneKidDate(UserPrincipal userPrincipal, EvaluateDateKidRequest evaluateDateKidRequest) throws FirebaseMessagingException;

    /**
     * lưu đánh giá cho một học sinh ở tab chi tiết ngày
     *
     * @param userPrincipal
     * @param evaluateDateKidsBriefRequest
     * @return
     */
    EvaluateDateKidsBriefResponse saveEvaluateOneKidDetailDate(UserPrincipal userPrincipal, EvaluateDateKidsBriefRequest evaluateDateKidsBriefRequest) throws FirebaseMessagingException;

    /**
     * update evaluate kids common
     *
     * @param userPrincipal
     * @param evaluateDateKidsBriefCommonRequest
     * @return
     */
    boolean saveEvaluateManyKidCommon(UserPrincipal userPrincipal, EvaluateDateKidsBriefCommonRequest evaluateDateKidsBriefCommonRequest) throws FirebaseMessagingException;

    /**
     * lưu đánh giá cho nhiều học sinh ở tab chi tiết ngày
     *
     * @param idSchool
     * @param userPrincipal
     * @param evaluateDateKidsBriefRequestList
     * @return
     */
    boolean saveEvaluateManyKidsDetailDate(Long idSchool, UserPrincipal userPrincipal, List<EvaluateDateKidsBriefRequest> evaluateDateKidsBriefRequestList) throws FirebaseMessagingException;

    List<EvaluateSampleResponse> findEvaluateSampleByIdSchool(Long idSchool, UserPrincipal principal);

    /**
     * chuyển đổi respon data sang model view excel Evaluate
     * @param evaluateDateResponses
     */
     List<EvaluateDateKidModel> convertEvaluateKidsToVM (List<EvaluateDateKidExcelResponse> evaluateDateResponses);

     List<ExcelNewResponse> convertEvaluateKidsToVMNew (UserPrincipal principal, List<EvaluateDateKidExcelResponse> evaluateDateResponses);

    /**
     * tìm kiếm đánh giá các học sinh trong một ngày cho file excel
     *
     * @param idSchool
     * @param evaluateDateSearchRequest
     * @return
     */
    List<EvaluateDateKidExcelResponse> searchEvaluateKidsDateExcel(Long idSchool, EvaluateDateSearchRequest evaluateDateSearchRequest);

    /**
     * danh sách id của tất cả các học sinh trong 1 lớp trong 1 tháng
     *
     * @param idSchool
     * @param evaluateDateSearchRequest
     */
    List<ListIdKidDTO> totalEvaluateKidsDetailOfMonth(Long idSchool, EvaluateDateSearchRequest evaluateDateSearchRequest);

    /**
     * tìm kiếm nhận xét tất cả học sinh trong 1 lớp trong một tháng
     *
     * @param idSchool
     * @param evaluateDateSearchRequest
     * @return
     */
    List<EvaluateDateResponse> findEvaluateDateKidsClassOfMonth(Long idSchool, EvaluateDateSearchRequest evaluateDateSearchRequest);

    /**
     * tìm kiếm nhận xét tất cả học sinh trong 1 lớp trong một tháng xuất file excel
     *
     * @param idSchool
     * @param evaluateDateSearchRequest
     * @return
     */
    List<EvaluateDateKidExcelResponse> findEvaluateDateKidsClassOfMonthToExcel(Long idSchool, EvaluateDateSearchRequest evaluateDateSearchRequest);
    /**
     * tách list nhận xét cho từng học sinh trong lớp trong một tháng
     *
     * @param evaluateDateResponses
     * @return
     */
    Map<Long, List<EvaluateDateKidModel>> detachedListEvaluateKidsClassOfMonth(List<EvaluateDateKidExcelResponse> evaluateDateResponses, List<ListIdKidDTO> kidDTOList);

    List<ExcelNewResponse> detachedListEvaluateKidsClassOfMonthNew(List<EvaluateDateKidExcelResponse> evaluateDateResponses, List<ListIdKidDTO> kidDTOList, Long idSchool, Long idClass, LocalDate date);
}
