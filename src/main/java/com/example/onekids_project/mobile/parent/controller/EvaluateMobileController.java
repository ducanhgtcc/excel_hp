package com.example.onekids_project.mobile.parent.controller;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.mobile.parent.response.evaluate.*;
import com.example.onekids_project.mobile.parent.service.servicecustom.EvaluateDateMobileService;
import com.example.onekids_project.mobile.parent.service.servicecustom.EvaluateMonthMobileService;
import com.example.onekids_project.mobile.parent.service.servicecustom.EvaluatePeriodicMobileService;
import com.example.onekids_project.mobile.parent.service.servicecustom.EvaluateWeekMobileService;
import com.example.onekids_project.mobile.request.ContentMobileRequest;
import com.example.onekids_project.mobile.request.DateNotNullRequest;
import com.example.onekids_project.response.common.DataResponse;
import com.example.onekids_project.response.common.ErrorResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.RequestUtils;
import com.example.onekids_project.validate.CommonValidate;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/mob/parent/evaluate")
public class EvaluateMobileController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private EvaluateDateMobileService evaluateDateMobileService;

    @Autowired
    private EvaluateWeekMobileService evaluateWeekMobileService;

    @Autowired
    private EvaluateMonthMobileService evaluateMonthMobileService;

    @Autowired
    private EvaluatePeriodicMobileService evaluatePeriodicMobileService;

    /**
     * get evaluate date for kid
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/satistical")
    public ResponseEntity statisticalEvaluate(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataParent(principal);
        EvaluateStatisticalMobileResponse data = evaluateDateMobileService.evaluateStatisticalUnread(principal);
//        logger.info("Thống kê số nhận xét chưa đọc của phụ huynh thành công");
        return NewDataResponse.setDataCustom(data, "Thống kê số nhận xét chưa đọc của phụ huynh thành công");

    }

    /**
     * get evaluate date for kid
     *
     * @param principal
     * @param dateNotNullRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/date")
    public ResponseEntity searchEvaluateKids(@CurrentUser UserPrincipal principal, DateNotNullRequest dateNotNullRequest) {
        RequestUtils.getFirstRequest(principal, dateNotNullRequest);
        CommonValidate.checkDataParent(principal);
        EvaluateDateMobileResponse data = evaluateDateMobileService.findEvaluateByIdKids(principal, dateNotNullRequest.getDate());
        if (data == null) {
            return NewDataResponse.setDataCustom(data,"Không có nhận xét cho phụ huynh");
        }
        return NewDataResponse.setDataSearch(data);

    }

    /**
     * get total parent unread of month
     *
     * @param principal
     * @param dateNotNullRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/date/month/unread")
    public ResponseEntity getTotalUnreadOfMonth(@CurrentUser UserPrincipal principal, DateNotNullRequest dateNotNullRequest) {
        RequestUtils.getFirstRequest(principal, dateNotNullRequest);
        CommonValidate.checkDataParent(principal);
        int count = evaluateDateMobileService.getTatalParentUnreadOfMonth(principal, dateNotNullRequest.getDate());
//        logger.info("Tìm kiếm tổng nhận xét ngày chưa đọc của tháng thành công");
        return NewDataResponse.setDataCreate(count);

    }

    /**
     * total of month
     *
     * @param principal
     * @param dateNotNullRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/date/month")
    public ResponseEntity searchTotalEvaluateMonth(@CurrentUser UserPrincipal principal, DateNotNullRequest dateNotNullRequest) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataParent(principal);
        List<DateOfMonthMobileResponse> dataList = evaluateDateMobileService.totalEvaluateDateOfMonth(principal, dateNotNullRequest.getDate());
        if (CollectionUtils.isEmpty(dataList)) {
            return NewDataResponse.setDataCustom(dataList,"Không có nhận xét của tháng cho phụ huynh");
        }
        return NewDataResponse.setDataSearch(dataList);

    }

    /**
     * create reply date
     * @param principal
     * @param id
     * @param contentMobileRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/date/reply/{id}")
    public ResponseEntity createParentReply(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id, @Valid @RequestBody ContentMobileRequest contentMobileRequest) throws FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal, contentMobileRequest);
        CommonValidate.checkDataParent(principal);
        boolean checkCreateReply = evaluateDateMobileService.createParentReply(principal, id, contentMobileRequest.getContent());
        return NewDataResponse.setDataCreate(checkCreateReply);
    }

    /**
     * update parent reply
     *
     * @param principal
     * @param id
     * @param contentMobileRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/date/reply/{id}")
    public ResponseEntity updateParentReply(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id, @Valid @RequestBody ContentMobileRequest contentMobileRequest) {
        RequestUtils.getFirstRequest(principal, contentMobileRequest);
        CommonValidate.checkDataParent(principal);
        boolean checkUpdateReply = evaluateDateMobileService.updateParentReply(principal, id, contentMobileRequest.getContent());
        return NewDataResponse.setDataUpdate(checkUpdateReply);
    }

    /**
     * revoke parent reply
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/date/revoke/{id}")
    public ResponseEntity revokeParentReply(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkDataParent(principal);
        boolean revokeUpdateReply = evaluateDateMobileService.revokeParentReply(principal, id);
        return NewDataResponse.setDataCustom(revokeUpdateReply,"Thu hồi phản hồi thành công");

    }

    /**
     * get evaluate date for kid
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/week")
    public ResponseEntity searchWeekOfKid(@CurrentUser UserPrincipal principal, @RequestParam(required = false) Long id) {
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkDataParent(principal);
        Pageable pageable = PageRequest.of(1, AppConstant.MAX_PAGE_ITEM);
        EvaluateWeekMobileResponse data = evaluateWeekMobileService.findEvaluateWeek(principal, id, pageable);
        if (CollectionUtils.isEmpty(data.getDataList())) {
            return NewDataResponse.setDataCustom(data,"Không có nhận xét tuần");
        }
        return NewDataResponse.setDataSearch(data);
    }

    /**
     * create reply week
     * @param principal
     * @param id
     * @param contentMobileRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/week/reply/{id}")
    public ResponseEntity createParentWeekReply(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id, @Valid @RequestBody ContentMobileRequest contentMobileRequest) throws FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal, contentMobileRequest);
        CommonValidate.checkDataParent(principal);
        ListEvaluateWeekMobileResponse listEvaluateWeekMobileResponse = evaluateWeekMobileService.createParentReply(principal, id, contentMobileRequest.getContent());
        return NewDataResponse.setDataCreate(listEvaluateWeekMobileResponse);

    }

    /**
     * @param principal
     * @param id
     * @param contentMobileRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/week/reply/{id}")
    public ResponseEntity updateParentWeekReply(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id, @Valid @RequestBody ContentMobileRequest contentMobileRequest) {
        RequestUtils.getFirstRequest(principal, contentMobileRequest);
        CommonValidate.checkDataParent(principal);
        ListEvaluateWeekMobileResponse listEvaluateWeekMobileResponse = evaluateWeekMobileService.updateParentReply(principal, id, contentMobileRequest.getContent());
        return NewDataResponse.setDataUpdate(listEvaluateWeekMobileResponse);

    }

    /**
     * revoke parent reply
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/week/revoke/{id}")
    public ResponseEntity revokeParentWeekReply(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkDataParent(principal);
        ListEvaluateWeekMobileResponse listEvaluateWeekMobileResponse = evaluateWeekMobileService.revokeParentReply(principal, id);
        return NewDataResponse.setDataRevoke(listEvaluateWeekMobileResponse);

    }

    /**
     * revoke parent reply
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/week/view/{id}")
    public ResponseEntity parentViewWeek(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkDataParent(principal);
        int countParentUnread = evaluateWeekMobileService.parentView(principal, id);
        return NewDataResponse.setDataSearch(countParentUnread);

    }

    /**
     * get evaluate date for kid
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/month")
    public ResponseEntity searchMonthOfKid(@CurrentUser UserPrincipal principal, @RequestParam(required = false) Long id) {
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkDataParent(principal);
        Pageable pageable = PageRequest.of(1, AppConstant.MAX_PAGE_ITEM);
        EvaluateMonthMobileResponse data = evaluateMonthMobileService.findEvaluateMonth(principal, id, pageable);
        if (CollectionUtils.isEmpty(data.getDataList())) {
            return NewDataResponse.setDataCustom(data,"Không có nhận xét tháng");
        }
        return NewDataResponse.setDataSearch(data);

    }

    /**
     * @param principal
     * @param id
     * @param contentMobileRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/month/reply/{id}")
    public ResponseEntity createParentMonthReply(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id, @Valid @RequestBody ContentMobileRequest contentMobileRequest) throws FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal, contentMobileRequest);
        CommonValidate.checkDataParent(principal);
        ListEvaluateMonthMobileResponse listEvaluateMonthMobileResponse = evaluateMonthMobileService.createParentReply(principal, id, contentMobileRequest.getContent());
        return NewDataResponse.setDataCreate(listEvaluateMonthMobileResponse);

    }

    /**
     * @param principal
     * @param id
     * @param contentMobileRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/month/reply/{id}")
    public ResponseEntity updateParentMonthReply(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id, @Valid @RequestBody ContentMobileRequest contentMobileRequest) {
        RequestUtils.getFirstRequest(principal, contentMobileRequest);
        CommonValidate.checkDataParent(principal);
        ListEvaluateMonthMobileResponse listEvaluateMonthMobileResponse = evaluateMonthMobileService.updateParentReply(principal, id, contentMobileRequest.getContent());
        return NewDataResponse.setDataUpdate(listEvaluateMonthMobileResponse);

    }

    /**
     * revoke parent reply
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/month/revoke/{id}")
    public ResponseEntity revokeParentMonthReply(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkDataParent(principal);
        ListEvaluateMonthMobileResponse listEvaluateMonthMobileResponse = evaluateMonthMobileService.revokeParentReply(principal, id);
        return NewDataResponse.setDataRevoke(listEvaluateMonthMobileResponse);

    }

    /**
     * revoke parent reply
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/month/view/{id}")
    public ResponseEntity parentViewMonth(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkDataParent(principal);
        int countParentUnread = evaluateMonthMobileService.parentView(principal, id);
        return NewDataResponse.setDataSearch(countParentUnread);

    }

    /**
     * get evaluate date for kid
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/periodic")
    public ResponseEntity searchPeriodicOfKid(@CurrentUser UserPrincipal principal, @RequestParam(required = false) Long id) {
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkDataParent(principal);
        Pageable pageable = PageRequest.of(1, AppConstant.MAX_PAGE_ITEM);
        EvaluatePeriodicMobileResponse data = evaluatePeriodicMobileService.findEvaluatePeriodic(principal, id, pageable);
        if (CollectionUtils.isEmpty(data.getDataList())) {
            return NewDataResponse.setDataCustom(data,"Không có nhận xét định kỳ");
        }
        return NewDataResponse.setDataSearch(data);

    }

    /**
     * @param principal
     * @param id
     * @param contentMobileRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/periodic/reply/{id}")
    public ResponseEntity createParentPeriodicReply(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id, @Valid @RequestBody ContentMobileRequest contentMobileRequest) throws FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkDataParent(principal);
        ListEvaluatePeriodicMobileResponse data = evaluatePeriodicMobileService.createParentReply(principal, id, contentMobileRequest.getContent());
        return NewDataResponse.setDataCreate(data);

    }

    /**
     * @param principal
     * @param id
     * @param contentMobileRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/periodic/reply/{id}")
    public ResponseEntity updateParentPeriodicReply(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id, @Valid @RequestBody ContentMobileRequest contentMobileRequest) {
        RequestUtils.getFirstRequest(principal, contentMobileRequest);
        CommonValidate.checkDataParent(principal);
        ListEvaluatePeriodicMobileResponse data = evaluatePeriodicMobileService.updateParentReply(principal, id, contentMobileRequest.getContent());
        return NewDataResponse.setDataUpdate(data);

    }

    /**
     * revoke parent reply
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/periodic/revoke/{id}")
    public ResponseEntity revokeParentPeriodicReply(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkDataParent(principal);
        ListEvaluatePeriodicMobileResponse data = evaluatePeriodicMobileService.revokeParentReply(principal, id);
        return NewDataResponse.setDataRevoke(data);

    }

    /**
     * revoke parent reply
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/periodic/view/{id}")
    public ResponseEntity parentViewPeriodic(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkDataParent(principal);
        int countParentUnread = evaluatePeriodicMobileService.parentView(principal, id);
        return NewDataResponse.setDataSearch(countParentUnread);

    }
}
