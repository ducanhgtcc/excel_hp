package com.example.onekids_project.mobile.plus.controller;


import com.example.onekids_project.common.MessageConstant;
import com.example.onekids_project.mobile.plus.request.KidsPageNumberPlusRequest;
import com.example.onekids_project.mobile.plus.request.KidsSearchPlusRequest;
import com.example.onekids_project.mobile.plus.request.evaluate.*;
import com.example.onekids_project.mobile.plus.response.evaluate.*;
import com.example.onekids_project.mobile.plus.service.servicecustom.EvaluatePlusService;
import com.example.onekids_project.mobile.request.ContentAndIdMobileRequest;
import com.example.onekids_project.mobile.response.ReplyTypeEditObject;
import com.example.onekids_project.mobile.teacher.response.evaluate.EvaluateStatusResponse;
import com.example.onekids_project.request.evaluatekids.EvaluateClassDateRequest;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.RequestUtils;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/mob/plus/evaluate")
public class EvaluatePlusController {

    @Autowired
    private EvaluatePlusService evaluatePlusService;


    /**
     * thống kê trạng thái nhận xét của học sinh theo khối và ngày chọn
     *
     * @param principal
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/date/status-kid")
    public ResponseEntity findEvaluateStatusKidDate(@CurrentUser UserPrincipal principal, @Valid EvaluatePlusRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        List<EvaluateStatusKidDayPlusResponse> dataList = evaluatePlusService.findEvaluateStatusKidDate(principal, request);
        return NewDataResponse.setDataCustom(dataList, MessageConstant.FIND_EVALUATE_STATUS_KID);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/statistical/status")
    public ResponseEntity evaluateStatus(@CurrentUser UserPrincipal principal, @RequestParam Long idClass) {
        RequestUtils.getFirstRequestPlus(principal, idClass);
        EvaluateStatusResponse response = evaluatePlusService.getEvaluateStatus(principal, idClass);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * thống kê trạng thái nhận xét của học sinh theo khối và tháng chọn
     *
     * @param principal
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/month/status-kid")
    public ResponseEntity findEvaluateStatusKidMonth(@CurrentUser UserPrincipal principal, @Valid EvaluatePlusRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        List<EvaluateStatusKidMonthPlusResponse> dataList = evaluatePlusService.findEvaluateStatusKidMonth(principal, request);
        return NewDataResponse.setDataCustom(dataList, MessageConstant.FIND_EVALUATE_STATUS_KID);
    }

    /**
     * lấy nhận xét ngày của một lớp
     *
     * @param principal
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/date")
    public ResponseEntity findEvaluateDate(@CurrentUser UserPrincipal principal, @Valid EvaluateClassDateRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        List<EvaluateDatePlusResponse> dataList = evaluatePlusService.findEvaluateKisDate(principal, request);
        return NewDataResponse.setDataCustom(dataList, MessageConstant.FIND_EVALUATE_DATE);
    }

    /**
     * thống kê những ngày có ít nhận một học sinh có nhận xét trong một tháng
     *
     * @param principal
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/date/statistical-month")
    public ResponseEntity statisticalOfMonth(@CurrentUser UserPrincipal principal, @Valid EvaluateClassDateRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        List<Integer> dataList = evaluatePlusService.findEvaluateDateOfMonth(principal, request);
        return NewDataResponse.setDataCustom(dataList, MessageConstant.EVALUATE_DATE_STATISTICAL_MONTH);
    }

    /**
     * tạo nhận xét ngày của một lớp
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/date")
    public ResponseEntity createEvaluateDate(@CurrentUser UserPrincipal principal, @Valid @ModelAttribute ListEvaluateDateCreatePlusRequest request) throws FirebaseMessagingException {
        RequestUtils.getFirstRequestPlus(principal, request);
        ListEvaluateDateCreatePlusResponse response = evaluatePlusService.createEvaluateDate(principal, request);
        List<EvaluateDateCreatePlusResponse> dataList = response.getDataList();
        String message = response.getMessage();
        if (StringUtils.isBlank(message)) {
            int total = request.getDataList().size();
            int fail = response.getFailNumber();
            if (fail == 0) {
                message = MessageConstant.EVALUATE_DATE_SAVE;
            } else if (fail == total) {
                message = MessageConstant.EVALUATE_APPROVED;
            } else {
                message = MessageConstant.EVALUATE_DATE_SAVE + " " + (total - fail) + "/" + total + "\n" + MessageConstant.EVALUATE_APPROVED;
            }
        }
        return NewDataResponse.setDataCustom(dataList, message);
    }

    /**
     * tìm kiếm nhận xét tuần
     *
     * @param principal
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/week")
    public ResponseEntity findEvaluateWeek(@CurrentUser UserPrincipal principal, @Valid EvaluateClassDateRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        List<EvaluatePlusResponse> dataList = evaluatePlusService.findEvaluateKidsWeek(principal, request);
        return NewDataResponse.setDataCustom(dataList, MessageConstant.FIND_EVALUATE_WEEK);
    }

    /**
     * tạo nhận xét tuần
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/week")
    public ResponseEntity createEvaluateWeek(@CurrentUser UserPrincipal principal, @Valid @ModelAttribute ListEvaluateCreatePlusRequest request) throws FirebaseMessagingException {
        RequestUtils.getFirstRequestPlus(principal, request);
        ListEvaluateCreatePlusResponse response = evaluatePlusService.createEvaluateWeek(principal, request);
        List<EvaluateCreatePlusResponse> dataList = response.getDataList();
        String message = response.getMessage();
        if (StringUtils.isBlank(message)) {
            int total = request.getDataList().size();
            int fail = response.getFailNumber();
            if (fail == 0) {
                message = MessageConstant.EVALUATE_WEEK_SAVE;
            } else if (fail == total) {
                message = MessageConstant.EVALUATE_APPROVED;
            } else {
                message = MessageConstant.EVALUATE_WEEK_SAVE + " " + (total - fail) + "/" + total + "\n" + MessageConstant.EVALUATE_APPROVED;
            }
        }
        return NewDataResponse.setDataCustom(dataList, message);
    }

    /**
     * tìm kiếm nhận xét tháng
     *
     * @param principal
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/month")
    public ResponseEntity findEvaluateMonth(@CurrentUser UserPrincipal principal, @Valid EvaluateClassDateRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        List<EvaluatePlusResponse> dataList = evaluatePlusService.findEvaluateKidsMonth(principal, request);
        return NewDataResponse.setDataCustom(dataList, MessageConstant.FIND_EVALUATE_MONTH);
    }

    /**
     * tạo nhận xét tháng
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/month")
    public ResponseEntity createEvaluateMonth(@CurrentUser UserPrincipal principal, @Valid @ModelAttribute ListEvaluateCreatePlusRequest request) throws FirebaseMessagingException {
        RequestUtils.getFirstRequestPlus(principal, request);
        ListEvaluateCreatePlusResponse response = evaluatePlusService.createEvaluateMonth(principal, request);
        List<EvaluateCreatePlusResponse> dataList = response.getDataList();
        String message = response.getMessage();
        if (StringUtils.isBlank(message)) {
            int total = request.getDataList().size();
            int fail = response.getFailNumber();
            if (fail == 0) {
                message = MessageConstant.EVALUATE_MONTH_SAVE;
            } else if (fail == total) {
                message = MessageConstant.EVALUATE_APPROVED;
            } else {
                message = MessageConstant.EVALUATE_MONTH_SAVE + " " + (total - fail) + "/" + total + "\n" + MessageConstant.EVALUATE_APPROVED;
            }
        }
        return NewDataResponse.setDataCustom(dataList, message);
    }

    /**
     * tìm kiếm nhận xét định kỳ
     *
     * @param principal
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/periodic")
    public ResponseEntity findEvaluatePeriodic(@CurrentUser UserPrincipal principal, @Valid EvaluateClassDateRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        List<EvaluatePeriodicPlusResponse> dataList = evaluatePlusService.findEvaluateKidsPeriodic(principal, request);
        return NewDataResponse.setDataCustom(dataList, MessageConstant.FIND_EVALUATE_PERIODIC);
    }

    /**
     * tạo nhận xét định kỳ
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/periodic")
    public ResponseEntity createEvaluatePeriodic(@CurrentUser UserPrincipal principal, @Valid @ModelAttribute ListEvaluatePeriodicCreatePlusRequest request) throws FirebaseMessagingException {
        RequestUtils.getFirstRequestPlus(principal, request);
        ListEvaluateCreatePlusResponse response = evaluatePlusService.createEvaluatePeriodic(principal, request);
        List<EvaluateCreatePlusResponse> dataList = response.getDataList();
        String message = response.getMessage();
        if (StringUtils.isBlank(message)) {
            int total = request.getDataList().size();
            int fail = response.getFailNumber();
            if (fail == 0) {
                message = MessageConstant.EVALUATE_PERIODIC_SAVE;
            } else if (fail == total) {
                message = MessageConstant.EVALUATE_APPROVED;
            } else {
                message = MessageConstant.EVALUATE_PERIODIC_SAVE + " " + (total - fail) + "/" + total + "\n" + MessageConstant.EVALUATE_APPROVED;
            }
        }
        return NewDataResponse.setDataCustom(dataList, message);
    }

    /**
     * lấy số ngày có nhận xét định kỳ trong tháng
     *
     * @param principal
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/statistical-periodic/month")
    public ResponseEntity statisticalPeriodicOfMonthForClass(@CurrentUser UserPrincipal principal, @Valid EvaluateClassDateRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        List<Integer> dataList = evaluatePlusService.statisticalPeriodicOfMonthDate(principal, request);
        return NewDataResponse.setDataCustom(dataList, MessageConstant.STATISTICAL_EVALUATE_PERIODIC);
    }

    /**
     * Lấy danh sách nhận xét lớp thành công
     *
     * @param principal
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/statistical-month")
    public ResponseEntity statisticalOfMonthForClass(@CurrentUser UserPrincipal principal, @Valid EvaluateClassDateRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        List<StatisticalOfMonthPlusResponse> dataList = evaluatePlusService.statisticalOfMonth(principal, request);
        return NewDataResponse.setDataCustom(dataList, MessageConstant.STATISTICAL_EVALUATE_MONTH);
    }

    /**
     * List ngày có học sinh nhận xét ngày hoặc định kỳ theo tháng
     *
     * @param principal
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/statistical-date-periodic")
    public ResponseEntity statisticalOfMonthDateAndPeriodic(@CurrentUser UserPrincipal principal, @Valid EvaluateClassDateRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        List<Integer> dataList = evaluatePlusService.statisticalDateAndPeriodic(principal, request);
        return NewDataResponse.setDataCustom(dataList, MessageConstant.STATISTICAL_EVALUATE_MONTH);
    }

    /**
     * - lấy ra số nhận xét của một học sinh
     * - đếm số phụ huynh có phản mà nhà trường, giáo viên chưa đọc
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/kid-total")
    public ResponseEntity findEvaluateTotal(@CurrentUser UserPrincipal principal, @Valid KidsSearchPlusRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        TotalPlusResponse data = evaluatePlusService.findSchoolUnread(principal, request);
        return NewDataResponse.setDataCustom(data, MessageConstant.FIND_EVALUATE_TOTAL_KID);
    }

    /**
     * Lấy nhận xét ngày của một học sinh thành công
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/kid/date")
    public ResponseEntity getEvaluateDateKid(@CurrentUser UserPrincipal principal, KidsSearchPlusRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        EvaluateDateKidPlusResponse data = evaluatePlusService.getEvaluateDateKid(principal, request);
        return NewDataResponse.setDataCustom(data, MessageConstant.FIND_KID_EVALUATE_DATE);
    }

    /**
     * Lấy tổng số ngày chưa đọc trong 1 tháng
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/kid/date/unread")
    public ResponseEntity getEvaluateDateKidSchoolUnreadOfMonth(@CurrentUser UserPrincipal principal, @Valid KidsSearchPlusRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        int countSchoolUnread = evaluatePlusService.countKidDateSchoolUnread(principal, request);
        return NewDataResponse.setDataCustom(countSchoolUnread, MessageConstant.COUNT_KID_DATE_UNREAD_MONTH);
    }

    /**
     * Lấy danh sách ngày có nhận xét trong tháng
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/kid/date/have")
    public ResponseEntity getEvaluateDateKidHaveOfMonth(@CurrentUser UserPrincipal principal, @Valid KidsSearchPlusRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        EvaluateDateHavePlusResponse data = evaluatePlusService.findKidDateHaveOfMonth(principal, request);
        return NewDataResponse.setDataCustom(data, MessageConstant.COUNT_KID_DATE_HAVE_MONTH);
    }

    /**
     * gửi phản hồi ngày
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/kid/date")
    public ResponseEntity createEvaluateDateKidReply(@CurrentUser UserPrincipal principal, @Valid @RequestBody ContentAndIdMobileRequest request) throws FirebaseMessagingException {
        RequestUtils.getFirstRequestPlus(principal, request);
        ReplyTypeEditObject data = evaluatePlusService.createKidDateReply(principal, request);
        return NewDataResponse.setDataCustom(data, MessageConstant.CREATE_KID_DATE);
    }

    /**
     * thu hồi nhận xét ngày
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/kid/date/revoke/{id}")
    public ResponseEntity revokeDateKid(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequestPlus(principal, id);
        ReplyTypeEditObject data = evaluatePlusService.revokeKidDateReplye(principal, id);
        return NewDataResponse.setDataCustom(data, MessageConstant.REVOKE_KID_DATE);
    }

    /**
     * lấy nhận xét tuần
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/kid/week")
    public ResponseEntity getEvaluateWeekKid(@CurrentUser UserPrincipal principal, @Valid KidsPageNumberPlusRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        ListEvaluateKidPlusResponse data = evaluatePlusService.getEvaluateWeekKid(principal, request);
        return NewDataResponse.setDataCustom(data, MessageConstant.FIND_KID_EVALUATE_WEEK);
    }

    /**
     * click tuần
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/kid/week/view/{id}")
    public ResponseEntity viewEvaluateWeekKid(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequestPlus(principal, id);
        boolean data = evaluatePlusService.viewEvaluateWeekKid(principal, id);
        return NewDataResponse.setDataCustom(data, MessageConstant.VIEW_KID_EVALUATE_WEEK);
    }

    /**
     * gửi phản hồi nhận xét tuần
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/kid/week")
    public ResponseEntity createEvaluateWeekKidReply(@CurrentUser UserPrincipal principal, @Valid @RequestBody ContentAndIdMobileRequest request) throws FirebaseMessagingException {
        RequestUtils.getFirstRequestPlus(principal, request);
        ReplyTypeEditObject data = evaluatePlusService.createKidWeekReply(principal, request);
        return NewDataResponse.setDataCustom(data, MessageConstant.CREATE_KID_WEEK);
    }

    /**
     * thu hồi nhận xét tuần
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/kid/week/revoke/{id}")
    public ResponseEntity revokeWeekKid(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequestPlus(principal, id);
        ReplyTypeEditObject data = evaluatePlusService.revokeKidWeekReplye(principal, id);
        return NewDataResponse.setDataCustom(data, MessageConstant.REVOKE_KID_WEEK);
    }


    /**
     * lấy nhận xét tháng
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/kid/month")
    public ResponseEntity getEvaluateMonthKid(@CurrentUser UserPrincipal principal, @Valid KidsPageNumberPlusRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        ListEvaluateKidPlusResponse data = evaluatePlusService.getEvaluateMonthKid(principal, request);
        return NewDataResponse.setDataCustom(data, MessageConstant.FIND_KID_EVALUATE_MONTH);
    }

    /**
     * click tháng
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/kid/month/view/{id}")
    public ResponseEntity viewEvaluateMonthKid(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequestPlus(principal, id);
        boolean data = evaluatePlusService.viewEvaluateMonthKid(principal, id);
        return NewDataResponse.setDataCustom(data, MessageConstant.VIEW_KID_EVALUATE_MONTH);
    }

    /**
     * gửi phản hồi nhận xét tháng
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/kid/month")
    public ResponseEntity createEvaluateMonthKidReply(@CurrentUser UserPrincipal principal, @Valid @RequestBody ContentAndIdMobileRequest request) throws FirebaseMessagingException {
        RequestUtils.getFirstRequestPlus(principal, request);
        ReplyTypeEditObject data = evaluatePlusService.createKidMonthReply(principal, request);
        return NewDataResponse.setDataCustom(data, MessageConstant.CREATE_KID_MONTH);
    }

    /**
     * thu hồi nhận xét tháng
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/kid/month/revoke/{id}")
    public ResponseEntity revokeMonthKid(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequestPlus(principal, id);
        ReplyTypeEditObject data = evaluatePlusService.revokeKidMonthReply(principal, id);
        return NewDataResponse.setDataCustom(data, MessageConstant.REVOKE_KID_MONTH);
    }

    /**
     * lấy nhận xét định kỳ
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/kid/periodic")
    public ResponseEntity getEvaluatePeriodicKid(@CurrentUser UserPrincipal principal, @Valid KidsPageNumberPlusRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        ListEvaluateKidPlusResponse data = evaluatePlusService.getEvaluatePeriodicKid(principal, request);
        return NewDataResponse.setDataCustom(data, MessageConstant.FIND_KID_EVALUATE_PERIODIC);
    }

    /**
     * click định kỳ
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/kid/periodic/view/{id}")
    public ResponseEntity viewEvaluatePeriodicKid(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequestPlus(principal, id);
        boolean data = evaluatePlusService.viewEvaluatePeriodicKid(principal, id);
        return NewDataResponse.setDataCustom(data, MessageConstant.VIEW_KID_EVALUATE_PERIODIC);
    }

    /**
     * gửi phản hồi nhận xét định kỳ
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/kid/periodic")
    public ResponseEntity createEvaluatePeriodicKidReply(@CurrentUser UserPrincipal principal, @Valid @RequestBody ContentAndIdMobileRequest request) throws FirebaseMessagingException {
        RequestUtils.getFirstRequestPlus(principal, request);
        ReplyTypeEditObject data = evaluatePlusService.createKidPeriodicReply(principal, request);
        return NewDataResponse.setDataCustom(data, MessageConstant.CREATE_KID_PERIODIC);
    }

    /**
     * thu hồi nhận định kỳ
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/kid/periodic/revoke/{id}")
    public ResponseEntity revokePeriodicKid(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequestPlus(principal, id);
        ReplyTypeEditObject data = evaluatePlusService.revokeKidPeriodicReply(principal, id);
        return NewDataResponse.setDataCustom(data, MessageConstant.REVOKE_KID_PERIODIC);
    }

    /**
     * Duyệt nhận xét ngày
     *
     * @param principal
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/approved-date")
    public ResponseEntity approvedDate(@CurrentUser UserPrincipal principal, @RequestBody IdEvaluateListRequest request) throws FirebaseMessagingException {
        RequestUtils.getFirstRequestPlus(principal, request);
        boolean data = evaluatePlusService.approvedDate(principal, request);
        return NewDataResponse.setDataCustom(data, MessageConstant.APPROVE_KID_DATE);
    }

    /**
     * Duyệt nhận xét ngày
     *
     * @param principal
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/approved-week")
    public ResponseEntity approvedWeek(@CurrentUser UserPrincipal principal, @RequestBody IdEvaluateListRequest request) throws FirebaseMessagingException {
        RequestUtils.getFirstRequestPlus(principal, request);
        boolean data = evaluatePlusService.approvedWeek(principal, request);
        return NewDataResponse.setDataCustom(data, MessageConstant.APPROVE_KID_WEEK);
    }

    /**
     * Duyệt nhận xét ngày
     *
     * @param principal
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/approved-month")
    public ResponseEntity approvedMonth(@CurrentUser UserPrincipal principal, @RequestBody IdEvaluateListRequest request) throws FirebaseMessagingException {
        RequestUtils.getFirstRequestPlus(principal, request);
        boolean data = evaluatePlusService.approvedMonth(principal, request);
        return NewDataResponse.setDataCustom(data, MessageConstant.APPROVE_KID_MONTH);
    }

    /**
     * Duyệt nhận xét ngày
     *
     * @param principal
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/approved-periodic")
    public ResponseEntity approvedPeriodic(@CurrentUser UserPrincipal principal, @RequestBody IdEvaluateListRequest request) throws FirebaseMessagingException {
        RequestUtils.getFirstRequestPlus(principal, request);
        boolean data = evaluatePlusService.approvedPeriodic(principal, request);
        return NewDataResponse.setDataCustom(data, MessageConstant.APPROVE_KID_PERIODIC);
    }

}
