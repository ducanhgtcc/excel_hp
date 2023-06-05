package com.example.onekids_project.mobile.teacher.service.servicecustom;

import com.example.onekids_project.mobile.request.ContentAndIdMobileRequest;
import com.example.onekids_project.mobile.response.ReplyTypeEditObject;
import com.example.onekids_project.mobile.teacher.request.evaluate.*;
import com.example.onekids_project.mobile.teacher.response.evaluate.*;
import com.example.onekids_project.security.model.UserPrincipal;
import com.google.firebase.messaging.FirebaseMessagingException;

import java.time.LocalDate;
import java.util.List;

public interface EvaluateTeacherService {

    /**
     * - lấy ra số nhận xét của một học sinh
     * - đếm số phụ huynh có phản mà nhà trường, giáo viên chưa đọc
     *
     * @param principal
     * @param idKid
     * @return
     */
    TotalTeacherResponse findSchoolUnread(UserPrincipal principal, Long idKid, LocalDate localDate);

    /**
     * lấy nhận xét ngày của các học sinh trong một lớp
     *
     * @param principal
     * @param localDate
     * @return
     */
    List<EvaluateDateTeacherResponse> findEvaluateKisDate(UserPrincipal principal, LocalDate localDate);

    /**
     * tạo nhận xét ngày
     *
     * @param principal
     * @param listEvaluateDateCreateTeacherRequest
     * @return
     */
    ListEvaluateDateCreateTeacherResponse createEvaluateDate(UserPrincipal principal, ListEvaluateDateCreateTeacherRequest listEvaluateDateCreateTeacherRequest) throws FirebaseMessagingException;

    /**
     * sửa nhận xét ngày chung
     * những học sinh đã tạo nhận xét và đã duyệt thì ko tạo được
     * trả về số học sinh ko thành công
     *
     * @param principal
     * @param evaluateDateCommonCreateTeacherRequest
     * @return
     */
//    int createEvaluateDateCommon(UserPrincipal principal, EvaluateDateCommonCreateTeacherRequest evaluateDateCommonCreateTeacherRequest);

    /**
     * thống kê số ngày có ít nhất 1 học sinh được nhận xét
     *
     * @param principal
     * @param localDate
     * @return
     */
    List<Integer> findEvaluateDateOfMonth(UserPrincipal principal, LocalDate localDate);

    /**
     * tìm kiếm nhận xét tuần
     *
     * @param principal
     * @param localDate
     * @return
     */
    List<EvaluateTeacherResponse> findEvaluateKidsWeek(UserPrincipal principal, LocalDate localDate);

    /**
     * tạo nhận xét tuần
     *
     * @param principal
     * @param listEvaluateCreateTeacherRequest
     * @return
     */
    ListEvaluateCreateTeacherResponse createEvaluateWeek(UserPrincipal principal, ListEvaluateCreateTeacherRequest listEvaluateCreateTeacherRequest) throws FirebaseMessagingException;

    /**
     * tạo nhận xét tuần chung
     *
     * @param principal
     * @param evaluateCommonCreateTeacherRequest
     * @return
     */
//    int createEvaluateWeekCommon(UserPrincipal principal, EvaluateCommonCreateTeacherRequest evaluateCommonCreateTeacherRequest);

    /**
     * tìm kiếm nhận xét tuần
     *
     * @param principal
     * @param localDate
     * @return
     */
    List<EvaluateTeacherResponse> findEvaluateKidsMonth(UserPrincipal principal, LocalDate localDate);

    /**
     * tạo nhận xét tuần
     *
     * @param principal
     * @param listEvaluateCreateTeacherRequest
     * @return
     */
    ListEvaluateCreateTeacherResponse createEvaluateMonth(UserPrincipal principal, ListEvaluateCreateTeacherRequest listEvaluateCreateTeacherRequest) throws FirebaseMessagingException;

    /**
     * tạo nhận xét tháng chung
     *
     * @param principal
     * @param evaluateCommonCreateTeacherRequest
     * @return
     */
//    int createEvaluateMonthCommon(UserPrincipal principal, EvaluateCommonCreateTeacherRequest evaluateCommonCreateTeacherRequest);

    /**
     * lấy nhận xét định kỳ
     *
     * @param principal
     * @param localDate
     * @return
     */
    List<EvaluatePeriodicTeacherResponse> findEvaluateKidsPeriodic(UserPrincipal principal, LocalDate localDate);

    /**
     * tạo nhận xét định kỳ
     *
     * @param principal
     * @param listEvaluatePeriodicCreateTeacherRequest
     * @return
     */
    ListEvaluateCreateTeacherResponse createEvaluatePeriodic(UserPrincipal principal, ListEvaluatePeriodicCreateTeacherRequest listEvaluatePeriodicCreateTeacherRequest) throws FirebaseMessagingException;

    /**
     * tạo nhận xét định kỳ chung
     *
     * @param principal
     * @param evaluatePeriodicCommonCreateTeacherRequest
     * @return
     */
//    int createEvaluatePeriodicCommon(UserPrincipal principal, EvaluatePeriodicCommonCreateTeacherRequest evaluatePeriodicCommonCreateTeacherRequest);

    /**
     * thống kê số ngày có nhận xét định kỳ trong tháng của 1 lớp
     *
     * @param principal
     * @param localDate
     * @return
     */
    List<Integer> statisticalPeriodicOfMonthDate(UserPrincipal principal, LocalDate localDate);

    /**
     * thống kê số ngày có nhận xét trong một tháng của các học sinh trong một lớp
     *
     * @param principal
     * @param localDate
     * @return
     */
    List<StatisticalOfMonthTeacherResponse> statisticalOfMonth(UserPrincipal principal, LocalDate localDate);

    /**
     * thống kê ngày lớp có học sinh nhận xét ngày hoặc định kỳ theo tháng
     *
     * @param principal
     * @param localDate
     * @return
     */
    List<Integer> statisticalDateAndPeriodic(UserPrincipal principal, LocalDate localDate);

//    EvaluateDateMobileResponse findDetailEvaluateKidInDay(UserPrincipal principal, LocalDate date);

    /**
     * Lấy nhận xét ngày của một học sinh thành công
     *
     * @param principal
     * @param idKid
     * @param localDate
     * @return
     */
    EvaluateDateKidTeacherResponse getEvaluateDateKid(UserPrincipal principal, Long idKid, LocalDate localDate);

    /**
     * Lấy tổng số ngày chưa đọc trong 1 tháng của 1 học sinh
     *
     * @param principal
     * @param idKid
     * @param localDate
     * @return
     */
    int countKidDateSchoolUnread(UserPrincipal principal, Long idKid, LocalDate localDate);

    /**
     * Lấy danh sách ngày có nhận xét trong tháng
     *
     * @param principal
     * @param idKid
     * @param localDate
     * @return
     */
    EvaluateDateHaveTeacherResponse findKidDateHaveOfMonth(UserPrincipal principal, Long idKid, LocalDate localDate);

    /**
     * Gửi phản hồi ngày
     *
     * @param principal
     * @param request
     * @return
     */
    ReplyTypeEditObject createKidDateReply(UserPrincipal principal, ContentAndIdMobileRequest request) throws FirebaseMessagingException;

    /**
     * thu hồi phản hồi ngày
     *
     * @param principal
     * @return
     */
    ReplyTypeEditObject revokeKidDateReplye(UserPrincipal principal, Long id);

    /**
     * lấy nhận xét tuần
     *
     * @param principal
     * @return
     */
    ListEvaluateKidTeacherResponse getEvaluateWeekKid(UserPrincipal principal, KidsPageNumberRequest request);

    /**
     * click xem tuần
     *
     * @param principal
     * @param id
     * @return
     */
    boolean viewEvaluateWeekKid(UserPrincipal principal, Long id);

    /**
     * gửi phản hồi nhận xét tuần
     *
     * @param principal
     * @param request
     * @return
     */
    ReplyTypeEditObject createKidWeekReply(UserPrincipal principal, ContentAndIdMobileRequest request) throws FirebaseMessagingException;

    /**
     * thu hồi nhận xét tuần
     *
     * @param principal
     * @param id
     * @return
     */
    ReplyTypeEditObject revokeKidWeekReplye(UserPrincipal principal, Long id);


    /**
     * lấy nhận xét tháng
     *
     * @param principal
     * @return
     */
    ListEvaluateKidTeacherResponse getEvaluateMonthKid(UserPrincipal principal, KidsPageNumberRequest request);

    /**
     * click xem tháng
     *
     * @param principal
     * @param id
     * @return
     */
    boolean viewEvaluateMonthKid(UserPrincipal principal, Long id);

    /**
     * gửi phản hồi nhận xét tháng
     *
     * @param principal
     * @param request
     * @return
     */
    ReplyTypeEditObject createKidMonthReply(UserPrincipal principal, ContentAndIdMobileRequest request) throws FirebaseMessagingException;

    /**
     * thu hồi nhận xét tháng
     *
     * @param principal
     * @param id
     * @return
     */
    ReplyTypeEditObject revokeKidMonthReply(UserPrincipal principal, Long id);

    /**
     * lấy nhận xét định kỳ
     *
     * @param principal
     * @return
     */
    ListEvaluateKidTeacherResponse getEvaluatePeriodicKid(UserPrincipal principal, KidsPageNumberRequest request);

    /**
     * click xem định kỳ
     *
     * @param principal
     * @param id
     * @return
     */
    boolean viewEvaluatePeriodicKid(UserPrincipal principal, Long id);

    /**
     * gửi phản hồi nhận xét định kỳ
     *
     * @param principal
     * @param request
     * @return
     */
    ReplyTypeEditObject createKidPeriodicReply(UserPrincipal principal, ContentAndIdMobileRequest request) throws FirebaseMessagingException;

    /**
     * thu hồi nhận xét định kỳ
     *
     * @param principal
     * @param id
     * @return
     */
    ReplyTypeEditObject revokeKidPeriodicReply(UserPrincipal principal, Long id);

    EvaluateStatusResponse getEvaluateStatus(UserPrincipal principal);


}
