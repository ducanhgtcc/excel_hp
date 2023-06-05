package com.example.onekids_project.controller;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.MessageWebConstant;
import com.example.onekids_project.dto.ListIdKidDTO;
import com.example.onekids_project.entity.kids.EvaluateAttachFile;
import com.example.onekids_project.entity.kids.EvaluateMonthFile;
import com.example.onekids_project.entity.kids.EvaluatePeriodicFile;
import com.example.onekids_project.entity.kids.EvaluateWeekFile;
import com.example.onekids_project.importexport.model.EvaluateDateKidModel;
import com.example.onekids_project.importexport.service.EvaluateDateKidExcelService;
import com.example.onekids_project.repository.EvaluateAttachFileRepository;
import com.example.onekids_project.repository.EvaluateMonthFileRepository;
import com.example.onekids_project.repository.EvaluatePeriodicFileRepository;
import com.example.onekids_project.repository.EvaluateWeekFileRepository;
import com.example.onekids_project.request.evaluatekids.*;
import com.example.onekids_project.request.kids.SearchKidsClassRequest;
import com.example.onekids_project.response.common.ErrorResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.response.evaluatekids.*;
import com.example.onekids_project.response.excel.ExcelNewResponse;
import com.example.onekids_project.response.kids.KidOtherResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.evaluatekids.EvaluateDateService;
import com.example.onekids_project.service.servicecustom.evaluatekids.EvaluatePeriodicService;
import com.example.onekids_project.service.servicecustom.evaluatekids.EvaluateWeekService;
import com.example.onekids_project.service.servicecustom.evaluatekids.EvaluteMonthService;
import com.example.onekids_project.util.RequestUtils;
import com.example.onekids_project.validate.CommonValidate;
import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

import javax.validation.Valid;
import java.io.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/web/evaluate-kids")
public class EvaluateController {
    private static final Logger logger = LoggerFactory.getLogger(EvaluateController.class);

    @Autowired
    private EvaluateDateService evaluateDateService;

    @Autowired
    private EvaluateWeekService evaluateWeekService;

    @Autowired
    private EvaluteMonthService evaluateMonthService;

    @Autowired
    private EvaluatePeriodicService evaluatePeriodicService;

    @Autowired
    private EvaluateDateKidExcelService evaluateDateKidExcelService;

    @Autowired
    private EvaluateAttachFileRepository evaluateAttachFileRepository;

    @Autowired
    private EvaluateWeekFileRepository evaluateWeekFileRepository;

    @Autowired
    private EvaluateMonthFileRepository evaluateMonthFileRepository;

    @Autowired
    private EvaluatePeriodicFileRepository evaluatePeriodicFileRepository;

    /**
     * tìm kiếm đánh giá các học sinh trong một ngày
     *
     * @param principal
     * @param evaluateDateSearchRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public ResponseEntity searchDate(@CurrentUser UserPrincipal principal, @Valid EvaluateDateSearchRequest evaluateDateSearchRequest) {
        RequestUtils.getFirstRequest(principal, evaluateDateSearchRequest);
        CommonValidate.checkPlusOrTeacher(principal);
        Long idSchoolLogin = principal.getIdSchoolLogin();
        List<EvaluateDateResponse> evaluateDateResponseList = evaluateDateService.searchEvaluateKidsDate(idSchoolLogin, evaluateDateSearchRequest);
        return NewDataResponse.setDataSearch(evaluateDateResponseList);

    }

    /**
     * tìm kiếm đánh giá chi tiết các học sinh trong một ngày
     *
     * @param principal
     * @param evaluateDateSearchRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/search-detail-date")
    public ResponseEntity searchDetailDate(@CurrentUser UserPrincipal principal, @Valid EvaluateDateSearchRequest evaluateDateSearchRequest) {
        RequestUtils.getFirstRequest(principal, evaluateDateSearchRequest);
        CommonValidate.checkPlusOrTeacher(principal);
        Long idSchoolLogin = principal.getIdSchoolLogin();
        List<EvaluateDateKidsBriefResponse> evaluateDateKidsBriefResponseList = evaluateDateService.searchEvaluateKidsBriefDate(idSchoolLogin, evaluateDateSearchRequest);
        return NewDataResponse.setDataSearch(evaluateDateKidsBriefResponseList);

    }

    /**
     * tìm kiếm đánh giá học sinh trong ngày theo id
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/date/{id}")
    public ResponseEntity searchDateById(@CurrentUser UserPrincipal principal, @PathVariable(name = "id") Long id) {
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkPlusOrTeacher(principal);
        Long idSchoolLogin = principal.getIdSchoolLogin();
        EvaluateDateKidResponse evaluateDateKidResponse = evaluateDateService.findEvaluateKidsDateById(idSchoolLogin, id, principal);
        return NewDataResponse.setDataSearch(evaluateDateKidResponse);

    }

    /**
     * cập nhật xét duyệt cho một học sinh theo ngày
     *
     * @param principal
     * @param evaluateDateApprovedRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/approved-onekids-date")
    public ResponseEntity updateApprovedOnekidsDate(@CurrentUser UserPrincipal principal, @Valid @RequestBody EvaluateDateApprovedRequest evaluateDateApprovedRequest) throws FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal, evaluateDateApprovedRequest);
        CommonValidate.checkPlusOrTeacher(principal);
        Long idSchoolLogin = principal.getIdSchoolLogin();
        boolean checkUpdate = evaluateDateService.updateIsApprovedOneKidsDate(idSchoolLogin, evaluateDateApprovedRequest);
        return NewDataResponse.setDataUpdate(checkUpdate);
    }

    /**
     * cập nhật xét duyệt cho nhiều học sinh theo ngày
     *
     * @param principal
     * @param evaluateDateApprovedRequestList
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/approved-manykids-date")
    public ResponseEntity updateApprovedManykidsDate(@CurrentUser UserPrincipal principal, @Valid @RequestBody List<EvaluateDateApprovedRequest> evaluateDateApprovedRequestList) throws FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal, evaluateDateApprovedRequestList);
        CommonValidate.checkPlusOrTeacher(principal);
        Long idSchoolLogin = principal.getIdSchoolLogin();
        boolean checkUpdate = evaluateDateService.updateIsApprovedManyKidsDate(idSchoolLogin, evaluateDateApprovedRequestList);
        return NewDataResponse.setDataUpdate(checkUpdate);
    }

    /**
     * lưu đánh giá theo ngày cho một học sinh, bao gồm phản hồi
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/onekid-date")
    public ResponseEntity saveOneKidDate(@CurrentUser UserPrincipal principal, @Valid @ModelAttribute EvaluateDateKidRequest request) throws FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal, request);
        EvaluateDateResponse evaluateDateResponse = evaluateDateService.saveEvaluateOneKidDate(principal, request);
        return NewDataResponse.setDataCustom(evaluateDateResponse, MessageWebConstant.SAVE_EVALUATE);
    }

    /**
     * lưu đánh giá cho một học sinh ở tab chi tiết ngày
     *
     * @param principal
     * @param evaluateDateKidsBriefRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/onekid-detail-date")
    public ResponseEntity saveOneKidDetailDate(@CurrentUser UserPrincipal principal, @Valid @ModelAttribute EvaluateDateKidsBriefRequest evaluateDateKidsBriefRequest) throws FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal, evaluateDateKidsBriefRequest);
        CommonValidate.checkPlusOrTeacher(principal);
        EvaluateDateKidsBriefResponse evaluateDateKidsBriefResponse = evaluateDateService.saveEvaluateOneKidDetailDate(principal, evaluateDateKidsBriefRequest);
        return NewDataResponse.setDataCreate(evaluateDateKidsBriefResponse);

    }

    /**
     * lưu đánh giá cho một học sinh ở tab chi tiết ngày
     *
     * @param principal
     * @param evaluateDateKidsBriefCommonRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/manykids-detail-common")
    public ResponseEntity saveManyKidDetailCommon(@CurrentUser UserPrincipal principal, @Valid @ModelAttribute EvaluateDateKidsBriefCommonRequest evaluateDateKidsBriefCommonRequest) throws FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal, evaluateDateKidsBriefCommonRequest);
        CommonValidate.checkPlusOrTeacher(principal);
        boolean checkUpdate = evaluateDateService.saveEvaluateManyKidCommon(principal, evaluateDateKidsBriefCommonRequest);
        return NewDataResponse.setDataCreate(checkUpdate);

    }

    /**
     * lưu đánh giá cho nhiều học sinh ở tab chi tiết ngày
     *
     * @param principal
     * @param evaluateDateKidsBriefRequestList
     * @return
     */
//    @Deprecated
//    @RequestMapping(method = RequestMethod.PUT, value = "/manykids-detail-date")
//    public ResponseEntity saveManyKidDetailDate(@CurrentUser UserPrincipal principal, @Valid @RequestBody List<EvaluateDateKidsBriefRequest> evaluateDateKidsBriefRequestList) {
//        try {
//            Long idSchoolLogin = principal.getIdSchoolLogin();
//            if (idSchoolLogin == null || idSchoolLogin <= 0) {
//                logger.error(AppConstant.ID_SCHOOL_LOGIN_ERROR);
//                return ErrorResponse.errorData(AppConstant.ID_SCHOOL_LOGIN_ERROR, AppConstant.ID_SCHOOL_LOGIN_CONTENT, HttpStatus.NOT_ACCEPTABLE);
//            }
//            boolean checkUpdate = evaluateDateService.saveEvaluateManyKidsDetailDate(idSchoolLogin, principal, evaluateDateKidsBriefRequestList);
//            if (!checkUpdate) {
//                logger.warn("Lỗi lưu đánh giá cho nhiều học sinh theo ngày");
//                return DataResponse.getData("Lỗi lưu đánh giá cho nhiều học sinh theo ngày", HttpStatus.NOT_FOUND);
//            }
//            logger.info("Lưu đánh giá cho nhiều học sinh theo ngày thành công");
//            return DataResponse.getData("Lưu đánh giá cho nhiều học sinh theo ngày thành công", HttpStatus.OK);
//        } catch (Exception e) {
//            logger.error("Exception lưu đánh giá cho nhiều học sinh theo ngày: " + e.getMessage());
//            return ErrorResponse.errorData(e.getMessage(), "Lỗi lưu đánh giá cho nhiều học sinh theo ngày", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//        return null;
//    }

    /**
     * lấy mẫu đánh giá mặc định theo từng trường
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/sample-date")
    public ResponseEntity getEvaluateSampleDate(@CurrentUser UserPrincipal principal) {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        List<EvaluateSampleResponse> evaluateSampleResponseList = evaluateDateService.findEvaluateSampleByIdSchool(idSchoolLogin, principal);
        return NewDataResponse.setDataSearch(evaluateSampleResponseList);
    }

    /**
     * tìm kiếm đánh giá tuần của các học sinh
     *
     * @param principal
     * @param evaluateDateSearchRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/search-week")
    public ResponseEntity searchEvaluateWeekMany(@CurrentUser UserPrincipal principal, @Valid EvaluateDateSearchRequest evaluateDateSearchRequest) {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        List<EvaluateWeekResponse> evaluateWeekResponseList = evaluateWeekService.searchEvaluateWeek(idSchoolLogin, evaluateDateSearchRequest);
        return NewDataResponse.setDataSearch(evaluateWeekResponseList);
    }

    /**
     * tìm kiếm đánh giá tuần của các học sinh
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/search-week/{id}")
    public ResponseEntity searchEvaluateWeekById(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        EvaluateWeekKidResponse evaluateWeekKidResponse = evaluateWeekService.getEvaluateWeekById(idSchoolLogin, id);
        return NewDataResponse.setDataSearch(evaluateWeekKidResponse);
    }
//    /**
//     * lưu đánh giá tuần cho nhiều học sinh chưa bao gồm phản hồi
//     *
//     * @param principal
//     * @param evaluateWeekRequestList
//     * @return
//     */
//    @RequestMapping(method = RequestMethod.PUT, value = "/save-week-many")
//    public ResponseEntity saveEvaluateWeekMany(@CurrentUser UserPrincipal principal, @Valid @RequestBody List<EvaluateWeekRequest> evaluateWeekRequestList) {
//        try {
//            Long idSchoolLogin = principal.getIdSchoolLogin();
//            if (idSchoolLogin == null || idSchoolLogin <= 0) {
//                logger.error(AppConstant.ID_SCHOOL_LOGIN_ERROR);
//                return ErrorResponse.errorData(AppConstant.ID_SCHOOL_LOGIN_ERROR, AppConstant.ID_SCHOOL_LOGIN_CONTENT, HttpStatus.NOT_ACCEPTABLE);
//            }
//            boolean checkUpdate = evaluateWeekService.saveEvaluateWeekMany(idSchoolLogin, evaluateWeekRequestList, principal);
//            if (!checkUpdate) {
//                logger.warn("Lỗi lưu đánh giá tuần cho nhiều học sinh");
//                return DataResponse.getData("Lỗi lưu đánh giá tuần cho nhiều học sinh", HttpStatus.NOT_FOUND);
//            }
//            logger.info("Lưu đánh giá tuần cho nhiều học sinh thành công");
//            return DataResponse.getData("Lưu đánh giá tuần cho nhiều học sinh thành công", HttpStatus.OK);
//        } catch (Exception e) {
//            logger.error("Exception Lưu đánh giá tuần nhiều cho một học sinh: " + e.getMessage());
//            return ErrorResponse.errorData(e.getMessage(), "Exception Lưu đánh giá tuần nhiều cho một học sinh", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    /**
     * cập nhật xét duyệt đánh giá tuần cho một học sinh
     *
     * @param principal
     * @param evaluateDateApprovedRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/approved-week-one")
    public ResponseEntity updateApprovedWeekOne(@CurrentUser UserPrincipal principal, @Valid @RequestBody EvaluateDateApprovedRequest evaluateDateApprovedRequest) throws FirebaseMessagingException {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        boolean checkUpdate = evaluateWeekService.updateIsApprovedWeekOne(idSchoolLogin, evaluateDateApprovedRequest);
        return NewDataResponse.setDataUpdate(checkUpdate);
    }

    /**
     * cập nhật xét duyệt đánh giá tuần cho nhiều học sinh
     *
     * @param principal
     * @param evaluateDateApprovedRequestList
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/approved-week-many")
    public ResponseEntity updateApprovedWeekMany(@CurrentUser UserPrincipal principal, @Valid @RequestBody List<EvaluateDateApprovedRequest> evaluateDateApprovedRequestList) throws FirebaseMessagingException {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        boolean checkUpdate = evaluateWeekService.updateIsApprovedWeekMany(idSchoolLogin, evaluateDateApprovedRequestList);
        return NewDataResponse.setDataUpdate(checkUpdate);
    }

    /**
     * lưu đánh giá tuần cho một học sinh bao gồm phản hồi
     *
     * @param principal
     * @param evaluateWeekDetailRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/save-week-detail")
    public ResponseEntity saveEvaluateWeekDetail(@CurrentUser UserPrincipal principal, @Valid @ModelAttribute EvaluateWeekDetailRequest evaluateWeekDetailRequest) throws FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal, evaluateWeekDetailRequest);
        CommonValidate.checkPlusOrTeacher(principal);
        EvaluateWeekResponse evaluateWeekResponse = evaluateWeekService.saveEvaluateWeekDetailOne(evaluateWeekDetailRequest, principal);
        return NewDataResponse.setDataCreate(evaluateWeekResponse);

    }

    /**
     * lưu đánh giá tuần cho nhiều học sinh
     *
     * @param principal
     * @param evaluateWeekCommonRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/save-week-common")
    public ResponseEntity saveEvaluateWeekCommon(@CurrentUser UserPrincipal principal, @Valid @ModelAttribute EvaluateWeekCommonRequest evaluateWeekCommonRequest) throws FirebaseMessagingException {
        CommonValidate.checkPlusOrTeacher(principal);
        RequestUtils.getFirstRequest(principal, evaluateWeekCommonRequest);
        boolean checkUpdate = evaluateWeekService.saveEvaluateWeekCommon(principal, evaluateWeekCommonRequest);
        return NewDataResponse.setDataCreate(checkUpdate);

    }

    /**
     * tìm kiếm đánh giá một tuần cho một học sinh
     *
     * @param principal
     * @param evaluateWeekSearchKidRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/search-week-date")
    public ResponseEntity searchEvaluateWeekForKid(@CurrentUser UserPrincipal principal, @Valid EvaluateWeekSearchKidRequest evaluateWeekSearchKidRequest) {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        RequestUtils.getFirstRequest(principal, evaluateWeekSearchKidRequest);
        EvaluateWeekKidResponse evaluateWeekKidResponse = evaluateWeekService.searchEvaluateWeekKid(idSchoolLogin, evaluateWeekSearchKidRequest);
        return NewDataResponse.setDataSearch(evaluateWeekKidResponse);
    }

    /**
     * tìm kiếm đánh giá tháng của các học sinh
     *
     * @param principal
     * @param evaluateDateSearchRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/search-month")
    public ResponseEntity searchEvaluateMonthMany(@CurrentUser UserPrincipal principal, @Valid EvaluateDateSearchRequest evaluateDateSearchRequest) {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        RequestUtils.getFirstRequest(principal, evaluateDateSearchRequest);
        List<EvaluateMonthResponse> evaluateMonthResponseList = evaluateMonthService.searchEvaluateMonth(idSchoolLogin, evaluateDateSearchRequest);
        return NewDataResponse.setDataSearch(evaluateMonthResponseList);
    }

    /**
     * tìm kiếm đánh giá thang của 1 học sinh
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/search-month/{id}")
    public ResponseEntity searchEvaluateMonthById(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        RequestUtils.getFirstRequest(principal);
        EvaluateMonthKidResponse evaluateMonthKidResponse = evaluateMonthService.getEvaluateMonthById(idSchoolLogin, id);
        return NewDataResponse.setDataSearch(evaluateMonthKidResponse);
    }

    /**
     * lưu đánh giá tháng cho một học sinh chưa bao bồm phản hồi
     *
     * @param principal
     * @param evaluateMonthRequest
     * @return
     */
//    @Deprecated
//    @RequestMapping(method = RequestMethod.PUT, value = "/save-month-one")
//    public ResponseEntity saveEvaluateMonthOne(@CurrentUser UserPrincipal principal, @Valid @RequestBody EvaluateMonthRequest evaluateMonthRequest) {
//        try {
//            Long idSchoolLogin = principal.getIdSchoolLogin();
//            if (idSchoolLogin == null || idSchoolLogin <= 0) {
//                logger.error(AppConstant.ID_SCHOOL_LOGIN_ERROR);
//                return ErrorResponse.errorData(AppConstant.ID_SCHOOL_LOGIN_ERROR, AppConstant.ID_SCHOOL_LOGIN_CONTENT, HttpStatus.NOT_ACCEPTABLE);
//            }
//            EvaluateMonthResponse evaluateMonthResponse = evaluateMonthService.saveEvaluateMonthOne(idSchoolLogin, evaluateMonthRequest, principal);
//            if (evaluateMonthResponse == null) {
//                logger.warn("Lỗi lưu đánh giá tháng cho một học sinh");
//                return DataResponse.getData("Lỗi lưu đánh giá tháng cho một học sinh", HttpStatus.NOT_FOUND);
//            }
//            logger.info("Lưu đánh giá tháng cho một học sinh thành công");
//            return DataResponse.getData(evaluateMonthResponse, HttpStatus.OK);
//        } catch (Exception e) {
//            logger.error("Exception Lưu đánh giá tháng cho một học sinh: " + e.getMessage());
//            return ErrorResponse.errorData(e.getMessage(), "Exception Lưu đánh giá tháng cho một học sinh", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    /**
     * lưu đánh giá tháng cho nhiều học sinh chưa bao gồm phản hồi
     *
     * @param principal
     * @param evaluateMonthRequestList
     * @return
     */
//    @RequestMapping(method = RequestMethod.PUT, value = "/save-month-many")
//    public ResponseEntity saveEvaluateMonthMany(@CurrentUser UserPrincipal principal, @Valid @RequestBody List<EvaluateMonthRequest> evaluateMonthRequestList) {
//        try {
//            Long idSchoolLogin = principal.getIdSchoolLogin();
//            if (idSchoolLogin == null || idSchoolLogin <= 0) {
//                logger.error(AppConstant.ID_SCHOOL_LOGIN_ERROR);
//                return ErrorResponse.errorData(AppConstant.ID_SCHOOL_LOGIN_ERROR, AppConstant.ID_SCHOOL_LOGIN_CONTENT, HttpStatus.NOT_ACCEPTABLE);
//            }
//            boolean checkUpdate = evaluateMonthService.saveEvaluateMonthMany(idSchoolLogin, evaluateMonthRequestList, principal);
//            if (!checkUpdate) {
//                logger.warn("Lỗi lưu đánh giá tháng cho nhiều học sinh");
//                return DataResponse.getData("Lỗi lưu đánh giá tháng cho nhiều học sinh", HttpStatus.NOT_FOUND);
//            }
//            logger.info("Lưu đánh giá tháng cho nhiều học sinh thành công");
//            return DataResponse.getData("Lưu đánh giá tháng cho nhiều học sinh thành công", HttpStatus.OK);
//        } catch (Exception e) {
//            logger.error("Exception Lưu đánh giá tháng nhiều cho một học sinh: " + e.getMessage());
//            return ErrorResponse.errorData(e.getMessage(), "Exception Lưu đánh giá tháng nhiều cho một học sinh", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    /**
     * cập nhật xét duyệt đánh giá tháng cho một học sinh
     *
     * @param principal
     * @param evaluateDateApprovedRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/approved-month-one")
    public ResponseEntity updateApprovedMonthOne(@CurrentUser UserPrincipal principal, @Valid @RequestBody EvaluateDateApprovedRequest evaluateDateApprovedRequest) throws FirebaseMessagingException {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        RequestUtils.getFirstRequest(principal, evaluateDateApprovedRequest);
        boolean checkUpdate = evaluateMonthService.updateIsApprovedMonthOne(idSchoolLogin, evaluateDateApprovedRequest);
        return NewDataResponse.setDataUpdate(checkUpdate);
    }

    /**
     * cập nhật xét duyệt đánh giá tuần cho nhiều học sinh
     *
     * @param principal
     * @param evaluateDateApprovedRequestList
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/approved-month-many")
    public ResponseEntity updateApprovedMonthMany(@CurrentUser UserPrincipal principal, @Valid @RequestBody List<EvaluateDateApprovedRequest> evaluateDateApprovedRequestList) throws FirebaseMessagingException {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        RequestUtils.getFirstRequest(principal, evaluateDateApprovedRequestList);
        boolean checkUpdate = evaluateMonthService.updateIsApprovedMonthMany(idSchoolLogin, evaluateDateApprovedRequestList);
        return NewDataResponse.setDataUpdate(checkUpdate);
    }

    /**
     * tìm kiếm đánh giá một tháng cho một học sinh
     *
     * @param principal
     * @param evaluateWeekSearchKidRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/search-month-date")
    public ResponseEntity searchEvaluateMonthForKid(@CurrentUser UserPrincipal principal, @Valid EvaluateWeekSearchKidRequest evaluateWeekSearchKidRequest) {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        RequestUtils.getFirstRequest(principal, evaluateWeekSearchKidRequest);
        EvaluateMonthKidResponse evaluateMonthKidResponse = evaluateMonthService.searchEvaluateMonthKid(idSchoolLogin, evaluateWeekSearchKidRequest);
        return NewDataResponse.setDataSearch(evaluateMonthKidResponse);
    }

    /**
     * lưu đánh giá tháng cho một học sinh bao gồm phản hồi
     *
     * @param principal
     * @param evaluateMonthDetailRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/save-month-detail")
    public ResponseEntity saveEvaluateMonthDetail(@CurrentUser UserPrincipal principal, @Valid @ModelAttribute EvaluateMonthDetailRequest evaluateMonthDetailRequest) throws FirebaseMessagingException {
        CommonValidate.checkPlusOrTeacher(principal);
        Long idSchoolLogin = principal.getIdSchoolLogin();
        EvaluateMonthResponse evaluateMonthResponse = evaluateMonthService.saveEvaluateMonthDetailOne(idSchoolLogin, evaluateMonthDetailRequest, principal);
        return NewDataResponse.setDataCreate(evaluateMonthResponse);
    }

    /**
     * lưu đánh giá chung cho nhieu hoc sinh
     *
     * @param principal
     * @param evaluateMonthCommonRequest
     * @return
     */
    @SneakyThrows
    @RequestMapping(method = RequestMethod.POST, value = "/save-month-common")
    public ResponseEntity saveEvaluateMonthCommon(@CurrentUser UserPrincipal principal, @Valid @ModelAttribute EvaluateMonthCommonRequest evaluateMonthCommonRequest) {
        CommonValidate.checkPlusOrTeacher(principal);
        RequestUtils.getFirstRequest(principal, evaluateMonthCommonRequest);
        boolean checkUpdate = evaluateMonthService.saveEvaluateMonthCommon(principal, evaluateMonthCommonRequest);
        return NewDataResponse.setDataCreate(checkUpdate);
    }

    /**
     * tìm kiếm đánh giá định kỳ cho các học sinh
     *
     * @param principal
     * @param evaluatePeriodicSearchRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/search-periodic")
    public ResponseEntity searchEvaluatePeriodic(@CurrentUser UserPrincipal principal, @Valid EvaluatePeriodicSearchRequest evaluatePeriodicSearchRequest) {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        RequestUtils.getFirstRequest(principal, evaluatePeriodicSearchRequest);
        List<EvaluatePeriodicResponse> evaluatePeriodicResponseList = evaluatePeriodicService.searchEvaluatePeriodic(idSchoolLogin, evaluatePeriodicSearchRequest);
        return NewDataResponse.setDataSearch(evaluatePeriodicResponseList);
    }

    /**
     * search kids in class
     *
     * @param principal
     * @param searchKidsClassRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/search-kids-class")
    public ResponseEntity searchKidsClass(@CurrentUser UserPrincipal principal, SearchKidsClassRequest searchKidsClassRequest) {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        RequestUtils.getFirstRequest(principal, searchKidsClassRequest);
        List<KidOtherResponse> kidOtherResponseList = evaluatePeriodicService.searchKidsClass(idSchoolLogin, searchKidsClassRequest);
        return NewDataResponse.setDataSearch(kidOtherResponseList);
    }

    /**
     * create evaluate periodic for one kid
     *
     * @param principal
     * @param evaluatePeriodicCreateRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/periodic/create/{id}")
    public ResponseEntity createEvaluatePeriodicOne(@CurrentUser UserPrincipal principal, @PathVariable("id") Long idKid, @Valid @ModelAttribute EvaluatePeriodicCreateRequest evaluatePeriodicCreateRequest) throws FirebaseMessagingException {
        CommonValidate.checkPlusOrTeacher(principal);
        RequestUtils.getFirstRequest(principal, evaluatePeriodicCreateRequest);
        boolean checkCreate = evaluatePeriodicService.createEvaluatePeriodic(principal, idKid, evaluatePeriodicCreateRequest);
        return NewDataResponse.setDataCreate(checkCreate);
    }

    /**
     * create evaluate periodic for many kid
     *
     * @param principal
     * @param evaluatePeriodicCreateManyRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/periodic/create")
    public ResponseEntity createEvaluatePeriodicCommon(@CurrentUser UserPrincipal principal, @Valid @ModelAttribute EvaluatePeriodicCreateManyRequest evaluatePeriodicCreateManyRequest) throws FirebaseMessagingException {
        CommonValidate.checkPlusOrTeacher(principal);
        RequestUtils.getFirstRequest(principal, evaluatePeriodicCreateManyRequest);
        boolean checkCreate = evaluatePeriodicService.createEvaluatePeriodicMany(principal, evaluatePeriodicCreateManyRequest);
        return NewDataResponse.setDataCreate(checkCreate);
    }


    /**
     * lưu đánh giá định kỳ cho một học sinh chưa bao gồm phản hồi
     *
     * @param principal
     * @param evaluatePeriodicRequest
     * @return
     */
//    @RequestMapping(method = RequestMethod.PUT, value = "/save-periodic-one")
//    public ResponseEntity saveEvaluatePeriodicOne(@CurrentUser UserPrincipal principal, @Valid @RequestBody EvaluatePeriodicRequest evaluatePeriodicRequest) {
//        try {
//            Long idSchoolLogin = principal.getIdSchoolLogin();
//            if (idSchoolLogin == null || idSchoolLogin <= 0) {
//                logger.error(AppConstant.ID_SCHOOL_LOGIN_ERROR);
//                return ErrorResponse.errorData(AppConstant.ID_SCHOOL_LOGIN_ERROR, AppConstant.ID_SCHOOL_LOGIN_CONTENT, HttpStatus.NOT_ACCEPTABLE);
//            }
//            EvaluatePeriodicResponse evaluatePeriodicResponse = evaluatePeriodicService.saveEvaluatePeriodicOne(idSchoolLogin, evaluatePeriodicRequest, principal);
//            if (evaluatePeriodicResponse == null) {
//                logger.warn("Lỗi lưu đánh giá định kỳ cho một học sinh");
//                return DataResponse.getData("Lỗi lưu đánh giá định kỳ cho một học sinh", HttpStatus.NOT_FOUND);
//            }
//            logger.info("lưu đánh giá định kỳ cho một học sinh thành công");
//            return DataResponse.getData(evaluatePeriodicResponse, HttpStatus.OK);
//        } catch (Exception e) {
//            logger.error("Exception lưu đánh giá định kỳ cho một học sinh: " + e.getMessage());
//            return ErrorResponse.errorData(e.getMessage(), "Exception lưu đánh giá định kỳ cho một học sinh", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    /**
     * lưu đánh giá định kỳ cho nhiều học sinh chưa bao gồm phản hồi
     *
     * @param principal
     * @param evaluatePeriodicRequestList
     * @return
     */
//    @RequestMapping(method = RequestMethod.PUT, value = "/save-periodic-many")
//    public ResponseEntity saveEvaluatePeriodicMany(@CurrentUser UserPrincipal principal, @Valid @RequestBody List<EvaluatePeriodicRequest> evaluatePeriodicRequestList) {
//        try {
//            Long idSchoolLogin = principal.getIdSchoolLogin();
//            if (idSchoolLogin == null || idSchoolLogin <= 0) {
//                logger.error(AppConstant.ID_SCHOOL_LOGIN_ERROR);
//                return ErrorResponse.errorData(AppConstant.ID_SCHOOL_LOGIN_ERROR, AppConstant.ID_SCHOOL_LOGIN_CONTENT, HttpStatus.NOT_ACCEPTABLE);
//            }
//            boolean checkUpdate = evaluatePeriodicService.saveEvaluatePeriodicMany(idSchoolLogin, evaluatePeriodicRequestList, principal);
//            if (!checkUpdate) {
//                logger.warn("Lỗi lưu đánh giá định kỳ cho nhiều học sinh");
//                return DataResponse.getData("Lỗi lưu đánh giá định kỳ cho nhiều học sinh", HttpStatus.NOT_FOUND);
//            }
//            logger.info("Lưu đánh giá định kỳ cho nhiều học sinh thành công");
//            return DataResponse.getData("Lưu đánh giá định kỳ cho nhiều học sinh thành công", HttpStatus.OK);
//        } catch (Exception e) {
//            logger.error("Exception Lưu đánh giá định kỳ nhiều cho một học sinh: " + e.getMessage());
//            return ErrorResponse.errorData(e.getMessage(), "Exception Lưu đánh giá định kỳ nhiều cho một học sinh", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    /**
     * cập nhật xét duyệt đánh giá dinh ky cho một học sinh
     *
     * @param principal
     * @param evaluateDateApprovedRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/approved-periodic-one")
    public ResponseEntity updateApprovedPeriodicOne(@CurrentUser UserPrincipal principal, @Valid @RequestBody EvaluateDateApprovedRequest evaluateDateApprovedRequest) throws FirebaseMessagingException {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        RequestUtils.getFirstRequest(principal, evaluateDateApprovedRequest);
        boolean checkUpdate = evaluatePeriodicService.updateIsApprovedPeriodicOne(idSchoolLogin, evaluateDateApprovedRequest);
        return NewDataResponse.setDataUpdate(checkUpdate);
    }

    /**
     * cập nhật xét duyệt đánh giá tuần cho nhiều học sinh
     *
     * @param principal
     * @param evaluateDateApprovedRequestList
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/approved-periodic-many")
    public ResponseEntity updateApprovedPeriodicMany(@CurrentUser UserPrincipal principal, @Valid @RequestBody List<EvaluateDateApprovedRequest> evaluateDateApprovedRequestList) throws FirebaseMessagingException {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        RequestUtils.getFirstRequest(principal, evaluateDateApprovedRequestList);
        boolean checkUpdate = evaluatePeriodicService.updateIsApprovedPeriodicMany(idSchoolLogin, evaluateDateApprovedRequestList);
        return NewDataResponse.setDataUpdate(checkUpdate);
    }

    /**
     * tìm kiếm các lần đánh giá định kỳ cho một học sinh
     *
     * @param principal
     * @param idKid
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/search-periodic-detail/{id}")
    public ResponseEntity searchEvaluatePeriodicDetail(@CurrentUser UserPrincipal principal, @PathVariable("id") Long idKid) {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        List<EvaluatePeriodicKidResponse> evaluatePeriodicKidResponseList = evaluatePeriodicService.searchEvaluatePeriodicDetaial(idSchoolLogin, idKid);
        return NewDataResponse.setDataSearch(evaluatePeriodicKidResponseList);
    }

    /**
     * lưu đánh giá định kỳ cho một học sinh bao gồm phản hồi
     *
     * @param principal
     * @param evaluatePeriodicDetailRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/save-periodic-detail")
    public ResponseEntity saveEvaluatePeriodicDetail(@CurrentUser UserPrincipal principal, @Valid @ModelAttribute EvaluatePeriodicDetailRequest evaluatePeriodicDetailRequest) throws FirebaseMessagingException {
        CommonValidate.checkPlusOrTeacher(principal);
        Long idSchoolLogin = principal.getIdSchoolLogin();
        EvaluatePeriodicResponse evaluatePeriodicResponse = evaluatePeriodicService.saveEvaluatePeriodicDetailOne(idSchoolLogin, evaluatePeriodicDetailRequest, principal);
        return NewDataResponse.setDataCreate(evaluatePeriodicResponse);
    }


    /**
     * Downlaod file
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/download-date/{id}", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> downloadFileDate(@PathVariable("id") Long id) {
        HttpHeaders responseHeader = new HttpHeaders();
        try {
            EvaluateAttachFile evaluateAttachFile = evaluateAttachFileRepository.findById(id).orElseThrow(() -> new NotFoundException("not found evaluateAttachFile by id"));
            File file = new File(evaluateAttachFile.getUrlLocal());
            byte[] data = FileUtils.readFileToByteArray(file);
            responseHeader.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            responseHeader.set("Content-disposition", "attachment; filename=" + file.getName());
            responseHeader.setContentLength(data.length);
            InputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(data));
            InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
            return new ResponseEntity<>(inputStreamResource, responseHeader, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, responseHeader, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Downlaod file
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/download-week/{id}", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> downloadFileWeek(@PathVariable("id") Long id) {
        HttpHeaders responseHeader = new HttpHeaders();
        try {
            EvaluateWeekFile evaluateWeekFile = evaluateWeekFileRepository.findById(id).orElseThrow(() -> new NotFoundException("not found evaluateWeekFile by id"));
            File file = new File(evaluateWeekFile.getUrlLocal());
            byte[] data = FileUtils.readFileToByteArray(file);
            responseHeader.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            responseHeader.set("Content-disposition", "attachment; filename=" + file.getName());
            responseHeader.setContentLength(data.length);
            InputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(data));
            InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
            return new ResponseEntity<>(inputStreamResource, responseHeader, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, responseHeader, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Downlaod file
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/download-month/{id}", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> downloadFileMonth(@PathVariable("id") Long id) {
        HttpHeaders responseHeader = new HttpHeaders();
        try {
            EvaluateMonthFile evaluateMonthFile = evaluateMonthFileRepository.findById(id).orElseThrow(() -> new NotFoundException("not found evaluateMonthFile by id"));
            File file = new File(evaluateMonthFile.getUrlLocal());
            byte[] data = FileUtils.readFileToByteArray(file);
            responseHeader.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            responseHeader.set("Content-disposition", "attachment; filename=" + file.getName());
            responseHeader.setContentLength(data.length);
            InputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(data));
            InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
            return new ResponseEntity<>(inputStreamResource, responseHeader, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, responseHeader, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Downlaod file
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/download-periodic/{id}", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> downloadFilePeriodic(@PathVariable("id") Long id) {
        HttpHeaders responseHeader = new HttpHeaders();
        try {
            EvaluatePeriodicFile evaluatePeriodicFile = evaluatePeriodicFileRepository.findById(id).orElseThrow(() -> new NotFoundException("not found evaluatePeriodicFile by id"));
            File file = new File(evaluatePeriodicFile.getUrlLocal());
            byte[] data = FileUtils.readFileToByteArray(file);
            responseHeader.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            responseHeader.set("Content-disposition", "attachment; filename=" + file.getName());
            responseHeader.setContentLength(data.length);
            InputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(data));
            InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
            return new ResponseEntity<>(inputStreamResource, responseHeader, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, responseHeader, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
     * Xuất file đánh giá theo tháng
     */
    @RequestMapping(method = RequestMethod.GET, value = "/evaluate-to-excel-month")
    public ResponseEntity getAllAttendanceKidMonth(@CurrentUser UserPrincipal principal, @Valid EvaluateDateSearchRequest evaluateDateSearchRequest) {
        try {
            Long idSchoolLogin = principal.getIdSchoolLogin();
            if (idSchoolLogin == null || idSchoolLogin <= 0) {
                logger.error(AppConstant.ID_SCHOOL_LOGIN_ERROR);
                return ErrorResponse.errorData(AppConstant.ID_SCHOOL_LOGIN_ERROR, AppConstant.ID_SCHOOL_LOGIN_CONTENT, HttpStatus.NOT_ACCEPTABLE);
            }
            if (evaluateDateSearchRequest.getIdClass() != null) {

                List<EvaluateDateKidExcelResponse> listKids = evaluateDateService.findEvaluateDateKidsClassOfMonthToExcel(idSchoolLogin, evaluateDateSearchRequest);

                if (listKids == null) {
                    logger.error("lỗi tìm kiếm nhận xét học sinh");
                    return ErrorResponse.errorData("Không thể tìm kiếm nhận xét học sinh", "Không thể tìm kiếm nhận xét học sinh", HttpStatus.INTERNAL_SERVER_ERROR);
                }
                List<ListIdKidDTO> listIdKidDTOS = evaluateDateService.totalEvaluateKidsDetailOfMonth(idSchoolLogin, evaluateDateSearchRequest);
                Map<Long, List<EvaluateDateKidModel>> map = evaluateDateService.detachedListEvaluateKidsClassOfMonth(listKids, listIdKidDTOS);
                ByteArrayInputStream in = null;

                try {
                    in = evaluateDateKidExcelService.evaluateDateToExcelMonth(map, idSchoolLogin, evaluateDateSearchRequest.getIdClass(), evaluateDateSearchRequest.getDate());

                } catch (IOException e) {
                    logger.info("Lỗi" + e.getMessage());
                }
                logger.info("Tìm kiếm nhận xét học sinh thành công");
                return ResponseEntity.ok().body(new InputStreamResource(in)); // chưa trả được DataRespone Custom
            } else {
                logger.info("Thất bại");
                return ErrorResponse.errorData("không có dữ liệu truyền vào", "không có dữ liệu truyền vào", HttpStatus.INTERNAL_SERVER_ERROR);

            }


        } catch (Exception e) {
            logger.error("Exception Tìm kiếm học sinh không thành công: " + e.getMessage());
            return ErrorResponse.errorData(e.getMessage(), "Tìm kiếm học sinh không thành công", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /*
     * Xuất file đánh giá theo tháng NEW
     */
    @RequestMapping(method = RequestMethod.GET, value = "/evaluate-to-excel-month-new")
    public ResponseEntity getAllAttendanceKidMonthNew(@CurrentUser UserPrincipal principal, @Valid EvaluateDateSearchRequest evaluateDateSearchRequest) {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        List<EvaluateDateKidExcelResponse> listKids = evaluateDateService.findEvaluateDateKidsClassOfMonthToExcel(idSchoolLogin, evaluateDateSearchRequest);
        List<ListIdKidDTO> listIdKidDTOS = evaluateDateService.totalEvaluateKidsDetailOfMonth(idSchoolLogin, evaluateDateSearchRequest);
        List<ExcelNewResponse> list = evaluateDateService.detachedListEvaluateKidsClassOfMonthNew(listKids, listIdKidDTOS, idSchoolLogin, evaluateDateSearchRequest.getIdClass(), evaluateDateSearchRequest.getDate());
        return NewDataResponse.setDataSearch(list);
    }

    /*
     * Xuất file đánh giá theo ngày
     */
    @RequestMapping(method = RequestMethod.GET, value = "/evaluate-to-excel-date")
    public ResponseEntity excelEvaluateKidDate(@CurrentUser UserPrincipal principal, @Valid EvaluateDateSearchRequest evaluateDateSearchRequest) {
        try {
            Long idSchoolLogin = principal.getIdSchoolLogin();
            if (idSchoolLogin == null || idSchoolLogin <= 0) {
                logger.error(AppConstant.ID_SCHOOL_LOGIN_ERROR);
                return ErrorResponse.errorData(AppConstant.ID_SCHOOL_LOGIN_ERROR, AppConstant.ID_SCHOOL_LOGIN_CONTENT, HttpStatus.NOT_ACCEPTABLE);
            }
            if (evaluateDateSearchRequest.getIdClass() != null) {

                List<EvaluateDateKidExcelResponse> listKids = evaluateDateService.searchEvaluateKidsDateExcel(idSchoolLogin, evaluateDateSearchRequest);
                if (listKids == null) {
                    logger.error("lỗi tìm kiếm đánh giá học sinh");
                    return ErrorResponse.errorData("Không thể tìm kiếm đánh giá học sinh", "Không thể tìm kiếm đánh giá học sinh", HttpStatus.INTERNAL_SERVER_ERROR);
                }
                List<EvaluateDateKidModel> list = evaluateDateService.convertEvaluateKidsToVM(listKids);
                ByteArrayInputStream in = null;

                try {
                    in = evaluateDateKidExcelService.evaluateDateToExcel(list, idSchoolLogin, evaluateDateSearchRequest.getIdClass(), evaluateDateSearchRequest.getDate());

                } catch (IOException e) {
                    logger.info("Lỗi" + e.getMessage());
                }
                logger.info("Tìm kiếm đánh giá học sinh thành công");
                return ResponseEntity.ok().body(new InputStreamResource(in)); // chưa trả được DataRespone Custom
            } else {
                logger.info("Thất bại");
                return ErrorResponse.errorData("không có dữ liệu truyền vào", "không có dữ liệu truyền vào", HttpStatus.INTERNAL_SERVER_ERROR);

            }

        } catch (Exception e) {
            logger.error("Exception Tìm kiếm học sinh không thành công: " + e.getMessage());
            return ErrorResponse.errorData(e.getMessage(), "Tìm kiếm học sinh không thành công", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
     * Xuất file đánh giá theo ngày NEW
     */
    @RequestMapping(method = RequestMethod.GET, value = "/evaluate-to-excel-date-new")
    public ResponseEntity excelEvaluateKidDateNew(@CurrentUser UserPrincipal principal, @Valid EvaluateDateSearchRequest evaluateDateSearchRequest) {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        List<EvaluateDateKidExcelResponse> listKids = evaluateDateService.searchEvaluateKidsDateExcel(idSchoolLogin, evaluateDateSearchRequest);
        List<ExcelNewResponse> list = evaluateDateService.convertEvaluateKidsToVMNew(principal, listKids);
        return NewDataResponse.setDataSearch(list);
    }

}
