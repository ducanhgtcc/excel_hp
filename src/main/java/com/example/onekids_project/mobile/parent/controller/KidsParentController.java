package com.example.onekids_project.mobile.parent.controller;

import com.example.onekids_project.mobile.parent.request.VaccinateParentRequest;
import com.example.onekids_project.mobile.parent.request.kids.KidsExtraInforRequest;
import com.example.onekids_project.mobile.parent.request.kids.KidsHeightWeightParentRequest;
import com.example.onekids_project.mobile.parent.response.kids.*;
import com.example.onekids_project.mobile.parent.service.servicecustom.KidsHeightWeightParentService;
import com.example.onekids_project.mobile.parent.service.servicecustom.KidsParentService;
import com.example.onekids_project.mobile.parent.service.servicecustom.VaccinateParentService;
import com.example.onekids_project.mobile.request.DateNotNullRequest;
import com.example.onekids_project.response.common.DataResponse;
import com.example.onekids_project.response.common.ErrorResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.validate.CommonValidate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/mob/parent/kid")
public class KidsParentController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private KidsParentService kidsParentService;

    @Autowired
    private VaccinateParentService vaccinateParentService;

    @Autowired
    private KidsHeightWeightParentService kidsHeightWeightParentService;

    /**
     * lấy thông tin kids
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity searchEvaluateKids(@CurrentUser UserPrincipal principal) {
        try {
            CommonValidate.checkDataParent(principal);
            KidsParentResponse data = kidsParentService.findKidById(principal);
            return NewDataResponse.setDataSearch(data);
        } catch (Exception e) {
            logger.error("Exception Lỗi Tìm kiếm thông tin học sinh" + e.getMessage());
            return ErrorResponse.errorData(e.getMessage(), "Exception Lỗi Tìm kiếm thông tin học sinh", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /**
     * get vaccinate
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/vaccinate")
    public ResponseEntity searchVaccinate(@CurrentUser UserPrincipal principal) {
        try {
            CommonValidate.checkDataParent(principal);
            List<VaccinateParentResponse> dataList = vaccinateParentService.finVaccinateList(principal);
            return NewDataResponse.setDataSearch(dataList);
        } catch (Exception e) {
            logger.error("Exception Lỗi Tìm kiếm vaccinate" + e.getMessage());
            return ErrorResponse.errorData(e.getMessage(), "Exception Lỗi Tìm kiếm vaccinate", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * save vaccinate
     *
     * @param principal
     * @param vaccinateParentRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/vaccinate")
    public ResponseEntity saveVaccinate(@CurrentUser UserPrincipal principal, @RequestBody @Valid VaccinateParentRequest vaccinateParentRequest) {
        try {
            CommonValidate.checkDataParent(principal);
            VaccinateParentResponse data = vaccinateParentService.saveVaccinate(principal, vaccinateParentRequest);
            return NewDataResponse.setDataUpdate(data);
        } catch (Exception e) {
            logger.error("Exception Lỗi Lưu vaccinate" + e.getMessage());
            return ErrorResponse.errorData(e.getMessage(), "Exception Lỗi Lưu vaccinate", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * get extra info
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/extra")
    public ResponseEntity searchKidsExtraInfo(@CurrentUser UserPrincipal principal) {
        try {
            CommonValidate.checkDataParent(principal);
            KidsExtraInforResponse data = kidsParentService.findKidsExtraInfo(principal);
            return NewDataResponse.setDataSearch(data);
        } catch (Exception e) {
            logger.error("Exception Lỗi Tìm kiếm thông tin mở rộng" + e.getMessage());
            return ErrorResponse.errorData(e.getMessage(), "Exception Lỗi Tìm kiếm thông tin mở rộng", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * update kidsextra infor
     *
     * @param principal
     * @param kidsExtraInforRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/extra")
    public ResponseEntity updateKidsExtraInfo(@CurrentUser UserPrincipal principal, @Valid @ModelAttribute KidsExtraInforRequest kidsExtraInforRequest) {
        try {
            CommonValidate.checkDataParent(principal);
            KidsExtraInforResponse data = kidsParentService.updateKidsExtraInfo(principal, kidsExtraInforRequest);
            return NewDataResponse.setDataUpdate(data);
        } catch (Exception e) {
            logger.error("Exception Lỗi Cập nhậtthông tin mở rộng: " + e.getMessage());
            return ErrorResponse.errorData(e.getMessage(), "Exception Lỗi Cập nhật thông tin mở rộng: ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/height-weight-sample")
    public ResponseEntity findHeightSample(@CurrentUser UserPrincipal principal) {
        try {
            CommonValidate.checkDataParent(principal);
            ListHeightWeightSampleParentResponse dataList = kidsHeightWeightParentService.findHeightWeightSampleParent(principal.getIdKidLogin());
            return NewDataResponse.setDataSearch(dataList);
        } catch (Exception e) {
            logger.error("Exception Lỗi Tìm chiều cao, cân nặng tiêu chuẩn" + e.getMessage());
            return ErrorResponse.errorData(e.getMessage(), "Exception Lỗi Tìm kiếm chiều cao, cân nặng tiêu chuẩn", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/save-height-weight")
    public ResponseEntity saveHeightWeight(@CurrentUser UserPrincipal principal, @Valid @RequestBody KidsHeightWeightParentRequest kidsHeightWeightParentRequest) {
        try {
            CommonValidate.checkDataParent(principal);
            KidsHeightWeightParentResponse data = kidsHeightWeightParentService.saveHeightWeightParent(principal, kidsHeightWeightParentRequest);
            return NewDataResponse.setDataUpdate(data);
        } catch (Exception e) {
            logger.error("Exception Lỗi Lưu chiều cao, cân nặng" + e.getMessage());
            return ErrorResponse.errorData(e.getMessage(), "Exception Lỗi Lưu chiều cao, cân nặng", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/height-weight")
    public ResponseEntity findHeightWeight(@CurrentUser UserPrincipal principal, DateNotNullRequest dateNotNullRequest) {
        try {
            CommonValidate.checkDataParent(principal);
            Pageable pageable = PageRequest.of(1, 25);
            ListKidsHeightWeightParentResponse data = kidsHeightWeightParentService.findHeightWeight(principal, dateNotNullRequest.getDate(), pageable);
            if (data == null) {
                logger.warn("Không có cân nặng tiêu chuẩn");
                return NewDataResponse.setDataCustom(data,"Không có cân nặng tiêu chuẩn");
            }
            return NewDataResponse.setDataSearch(data);
        } catch (Exception e) {
            logger.error("Exception Lỗi Tìm cân nặng tiêu chuẩn" + e.getMessage());
            return ErrorResponse.errorData(e.getMessage(), "Exception Lỗi Tìm kiếm cân nặng tiêu chuẩn", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/height-weight")
    public ResponseEntity deleteHeightWeight(@CurrentUser UserPrincipal principal, @RequestParam(required = false) Long idHeight, @RequestParam(required = false) Long idWeight) {
        try {
            CommonValidate.checkDataParent(principal);
            boolean checkDelete = kidsHeightWeightParentService.deleteHeightWeight(idHeight, idWeight);
            return NewDataResponse.setDataDelete(checkDelete);
        } catch (Exception e) {
            logger.error("Exception Lỗi Xóa chiều cao, cân nặng" + e.getMessage());
            return ErrorResponse.errorData(e.getMessage(), "Exception Lỗi Xóa chiều cao, cân nặng", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
