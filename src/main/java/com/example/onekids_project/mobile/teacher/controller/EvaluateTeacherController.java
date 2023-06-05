package com.example.onekids_project.mobile.teacher.controller;

import com.example.onekids_project.common.MessageConstant;
import com.example.onekids_project.mobile.request.ContentAndIdMobileRequest;
import com.example.onekids_project.mobile.request.DateNotNullRequest;
import com.example.onekids_project.mobile.response.ReplyTypeEditObject;
import com.example.onekids_project.mobile.teacher.request.evaluate.*;
import com.example.onekids_project.mobile.teacher.response.evaluate.*;
import com.example.onekids_project.mobile.teacher.service.servicecustom.EvaluateTeacherService;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.RequestUtils;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/mob/teacher/evaluate")
public class EvaluateTeacherController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private EvaluateTeacherService evaluateTeacherService;

    /**
     * - lấy ra số nhận xét của một học sinh
     * - đếm số phụ huynh có phản mà nhà trường, giáo viên chưa đọc
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/kid-total")
    public ResponseEntity findEvaluateTotal(@CurrentUser UserPrincipal principal, @Valid KidsSearchRequest request) {
        RequestUtils.getFirstRequest(principal,request);
        TotalTeacherResponse data = evaluateTeacherService.findSchoolUnread(principal, request.getIdKid(), request.getDate());
        return NewDataResponse.setDataCustom(data, MessageConstant.FIND_EVALUATE_TOTAL_KID);
    }

    /**
     * lấy nhận xét ngày của một lớp
     *
     * @param principal
     * @param dateNotNullRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/date")
    public ResponseEntity findEvaluateDate(@CurrentUser UserPrincipal principal, @Valid DateNotNullRequest dateNotNullRequest) {
        RequestUtils.getFirstRequest(principal,dateNotNullRequest);
        List<EvaluateDateTeacherResponse> dataList = evaluateTeacherService.findEvaluateKisDate(principal, dateNotNullRequest.getDate());
        return NewDataResponse.setDataCustom(dataList, MessageConstant.FIND_EVALUATE_DATE);
    }

    /**
     * tạo nhận xét ngày của một lớp
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/date")
    public ResponseEntity createEvaluateDate(@CurrentUser UserPrincipal principal, @Valid @ModelAttribute ListEvaluateDateCreateTeacherRequest request) throws FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal,request);
        ListEvaluateDateCreateTeacherResponse response = evaluateTeacherService.createEvaluateDate(principal, request);
        List<EvaluateDateCreateTeacherResponse> dataList = response.getDataList();
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
     * thống kê những ngày có ít nhận một học sinh có nhận xét trong một tháng
     *
     * @param principal
     * @param dateNotNullRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/date/statistical-month")
    public ResponseEntity statisticalOfMonth(@CurrentUser UserPrincipal principal, @Valid DateNotNullRequest dateNotNullRequest) {
        RequestUtils.getFirstRequest(principal,dateNotNullRequest);
        List<Integer> dataList = evaluateTeacherService.findEvaluateDateOfMonth(principal, dateNotNullRequest.getDate());
        return NewDataResponse.setDataCustom(dataList, MessageConstant.EVALUATE_DATE_STATISTICAL_MONTH);
    }

    /**
     * tìm kiếm nhận xét tuần
     *
     * @param principal
     * @param dateNotNullRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/week")
    public ResponseEntity findEvaluateWeek(@CurrentUser UserPrincipal principal, @Valid DateNotNullRequest dateNotNullRequest) {
        RequestUtils.getFirstRequest(principal,dateNotNullRequest);
        List<EvaluateTeacherResponse> dataList = evaluateTeacherService.findEvaluateKidsWeek(principal, dateNotNullRequest.getDate());
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
    public ResponseEntity createEvaluateWeek(@CurrentUser UserPrincipal principal, @Valid @ModelAttribute ListEvaluateCreateTeacherRequest request) throws FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal,request);
        ListEvaluateCreateTeacherResponse response = evaluateTeacherService.createEvaluateWeek(principal, request);
        List<EvaluateCreateTeacherResponse> dataList = response.getDataList();
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
     * @param dateNotNullRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/month")
    public ResponseEntity findEvaluateMonth(@CurrentUser UserPrincipal principal, @Valid DateNotNullRequest dateNotNullRequest) {
        RequestUtils.getFirstRequest(principal,dateNotNullRequest);
        List<EvaluateTeacherResponse> dataList = evaluateTeacherService.findEvaluateKidsMonth(principal, dateNotNullRequest.getDate());
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
    public ResponseEntity createEvaluateMonth(@CurrentUser UserPrincipal principal, @Valid @ModelAttribute ListEvaluateCreateTeacherRequest request) throws FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal,request);
        ListEvaluateCreateTeacherResponse response = evaluateTeacherService.createEvaluateMonth(principal, request);
        List<EvaluateCreateTeacherResponse> dataList = response.getDataList();
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
     * @param dateNotNullRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/periodic")
    public ResponseEntity findEvaluatePeriodic(@CurrentUser UserPrincipal principal, @Valid DateNotNullRequest dateNotNullRequest) {
        RequestUtils.getFirstRequest(principal,dateNotNullRequest);
        List<EvaluatePeriodicTeacherResponse> dataList = evaluateTeacherService.findEvaluateKidsPeriodic(principal, dateNotNullRequest.getDate());
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
    public ResponseEntity createEvaluatePeriodic(@CurrentUser UserPrincipal principal, @Valid @ModelAttribute ListEvaluatePeriodicCreateTeacherRequest request) throws FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal,request);
        ListEvaluateCreateTeacherResponse response = evaluateTeacherService.createEvaluatePeriodic(principal, request);
        List<EvaluateCreateTeacherResponse> dataList = response.getDataList();
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
     * @param dateNotNullRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/statistical-periodic/month")
    public ResponseEntity statisticalPeriodicOfMonthForClass(@CurrentUser UserPrincipal principal, @Valid DateNotNullRequest dateNotNullRequest) {
        RequestUtils.getFirstRequest(principal,dateNotNullRequest);
        List<Integer> dataList = evaluateTeacherService.statisticalPeriodicOfMonthDate(principal, dateNotNullRequest.getDate());
        return NewDataResponse.setDataCustom(dataList, MessageConstant.STATISTICAL_EVALUATE_PERIODIC);
    }


    /**
     * Lấy danh sách nhận xét lớp thành công
     *
     * @param principal
     * @param dateNotNullRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/statistical-month")
    public ResponseEntity statisticalOfMonthForClass(@CurrentUser UserPrincipal principal, @Valid DateNotNullRequest dateNotNullRequest) {
        RequestUtils.getFirstRequest(principal,dateNotNullRequest);
        List<StatisticalOfMonthTeacherResponse> dataList = evaluateTeacherService.statisticalOfMonth(principal, dateNotNullRequest.getDate());
        return NewDataResponse.setDataCustom(dataList, MessageConstant.STATISTICAL_EVALUATE_MONTH);
    }

    /**
     * List ngày có học sinh nhận xét ngày hoặc định kỳ theo tháng
     *
     * @param principal
     * @param dateNotNullRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/statistical-date-periodic")
    public ResponseEntity statisticalOfMonthDateAndPeriodic(@CurrentUser UserPrincipal principal, @Valid DateNotNullRequest dateNotNullRequest) {
        RequestUtils.getFirstRequest(principal,dateNotNullRequest);
        List<Integer> dataList = evaluateTeacherService.statisticalDateAndPeriodic(principal, dateNotNullRequest.getDate());
        return NewDataResponse.setDataCustom(dataList, MessageConstant.STATISTICAL_EVALUATE_MONTH);
    }

    /**
     * Lấy nhận xét ngày của một học sinh thành công
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/kid/date")
    public ResponseEntity getEvaluateDateKid(@CurrentUser UserPrincipal principal, KidsSearchRequest request) {
        RequestUtils.getFirstRequest(principal,request);
        EvaluateDateKidTeacherResponse data = evaluateTeacherService.getEvaluateDateKid(principal, request.getIdKid(), request.getDate());
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
    public ResponseEntity getEvaluateDateKidSchoolUnreadOfMonth(@CurrentUser UserPrincipal principal, @Valid KidsSearchRequest request) {
        RequestUtils.getFirstRequest(principal,request);
        int countSchoolUnread = evaluateTeacherService.countKidDateSchoolUnread(principal, request.getIdKid(), request.getDate());
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
    public ResponseEntity getEvaluateDateKidHaveOfMonth(@CurrentUser UserPrincipal principal, @Valid KidsSearchRequest request) {
        RequestUtils.getFirstRequest(principal,request);
        EvaluateDateHaveTeacherResponse data = evaluateTeacherService.findKidDateHaveOfMonth(principal, request.getIdKid(), request.getDate());
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
        RequestUtils.getFirstRequest(principal,request);
        ReplyTypeEditObject data = evaluateTeacherService.createKidDateReply(principal, request);
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
        RequestUtils.getFirstRequest(principal,id);
        ReplyTypeEditObject data = evaluateTeacherService.revokeKidDateReplye(principal, id);
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
    public ResponseEntity getEvaluateWeekKid(@CurrentUser UserPrincipal principal, @Valid KidsPageNumberRequest request) {
        RequestUtils.getFirstRequest(principal,request);
        ListEvaluateKidTeacherResponse data = evaluateTeacherService.getEvaluateWeekKid(principal, request);
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
        RequestUtils.getFirstRequest(principal,id);
        boolean data = evaluateTeacherService.viewEvaluateWeekKid(principal, id);
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
        RequestUtils.getFirstRequest(principal,request);
        ReplyTypeEditObject data = evaluateTeacherService.createKidWeekReply(principal, request);
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
        RequestUtils.getFirstRequest(principal,id);
        ReplyTypeEditObject data = evaluateTeacherService.revokeKidWeekReplye(principal, id);
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
    public ResponseEntity getEvaluateMonthKid(@CurrentUser UserPrincipal principal, @Valid KidsPageNumberRequest request) {
        RequestUtils.getFirstRequest(principal,request);
        ListEvaluateKidTeacherResponse data = evaluateTeacherService.getEvaluateMonthKid(principal, request);
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
        RequestUtils.getFirstRequest(principal,id);
        boolean data = evaluateTeacherService.viewEvaluateMonthKid(principal, id);
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
        RequestUtils.getFirstRequest(principal,request);
        ReplyTypeEditObject data = evaluateTeacherService.createKidMonthReply(principal, request);
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
        RequestUtils.getFirstRequest(principal,id);
        ReplyTypeEditObject data = evaluateTeacherService.revokeKidMonthReply(principal, id);
        return NewDataResponse.setDataCustom(data, MessageConstant.REVOKE_KID_MONTH);
    }

    /**
     * lấy nhận xét tháng
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/kid/periodic")
    public ResponseEntity getEvaluatePeriodicKid(@CurrentUser UserPrincipal principal, @Valid KidsPageNumberRequest request) {
        RequestUtils.getFirstRequest(principal,request);
        ListEvaluateKidTeacherResponse data = evaluateTeacherService.getEvaluatePeriodicKid(principal, request);
        return NewDataResponse.setDataCustom(data, MessageConstant.FIND_KID_EVALUATE_PERIODIC);
    }

    /**
     * click tháng
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/kid/periodic/view/{id}")
    public ResponseEntity viewEvaluatePeriodicKid(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequest(principal,id);
        boolean data = evaluateTeacherService.viewEvaluatePeriodicKid(principal, id);
        return NewDataResponse.setDataCustom(data, MessageConstant.VIEW_KID_EVALUATE_PERIODIC);
    }

    /**
     * gửi phản hồi nhận xét dinh ky
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/kid/periodic")
    public ResponseEntity createEvaluatePeriodicKidReply(@CurrentUser UserPrincipal principal, @Valid @RequestBody ContentAndIdMobileRequest request) throws FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal,request);
        ReplyTypeEditObject data = evaluateTeacherService.createKidPeriodicReply(principal, request);
        return NewDataResponse.setDataCustom(data, MessageConstant.CREATE_KID_PERIODIC);
    }

    /**
     * thu hồi nhận xét tháng
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/kid/periodic/revoke/{id}")
    public ResponseEntity revokePeriodicKid(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequest(principal,id);
        ReplyTypeEditObject data = evaluateTeacherService.revokeKidPeriodicReply(principal, id);
        return NewDataResponse.setDataCustom(data, MessageConstant.REVOKE_KID_PERIODIC);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/statistical/status")
    public ResponseEntity evaluateStatus(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        EvaluateStatusResponse response = evaluateTeacherService.getEvaluateStatus(principal);
        return NewDataResponse.setDataSearch(response);
    }


    /**
     * 0 là tạo nhận xét bình thường
     * 1 là text đã được sửa đổi
     * 2 là text không được sủa đổi nhưng số file quá 3 file
     *
     * @param dataList
     * @return
     */
    private int getMessage(List<EvaluateCreateTeacherResponse> dataList) {
        int check = 0;
        if (!CollectionUtils.isEmpty(dataList)) {
            //text thay đổi
            for (EvaluateCreateTeacherResponse x : dataList) {
                if (x.getContent() != null) {
                    check = 1;
                    break;
                }
            }
            //file thay đổi quá 3 file
            if (check == 0) {
                check = 2;
            }
        }
        return check;
    }
}
