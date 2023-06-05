package com.example.onekids_project.controller;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.MessageWebConstant;
import com.example.onekids_project.request.base.IdListRequest;
import com.example.onekids_project.request.kids.KidsActionRequest;
import com.example.onekids_project.request.kids.KidsChangeClassRequest;
import com.example.onekids_project.request.parentdiary.ExportMedicineRequest;
import com.example.onekids_project.response.common.DataResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.response.excel.ExcelResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.KidsService;
import com.example.onekids_project.service.servicecustom.excelexport.ExcelExportService;
import com.example.onekids_project.util.RequestUtils;
import com.example.onekids_project.validate.CommonValidate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/web/student/action")
public class KidsActionController {
    private static final Logger logger = LoggerFactory.getLogger(KidsActionController.class);

    @Autowired
    private KidsService kidsService;

    @Autowired
    private ExcelExportService excelExportService;

    /**
     * Kích hoạt tài khoản cho nhiều học sinh
     *
     * @param principal
     * @param kidsActionRequestList
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/many-actived")
    public ResponseEntity updateManyKidsActive(@CurrentUser UserPrincipal principal, @Valid @RequestBody List<KidsActionRequest> kidsActionRequestList) {
        RequestUtils.getFirstRequest(principal, kidsActionRequestList);
        kidsService.actionActiveManyKids(principal, kidsActionRequestList, AppConstant.APP_TRUE);
        return NewDataResponse.setDataCustom(null,"Kích hoạt tài khoản cho các học sinh thành công");

    }

    /**
     * hủy kích hoạt tài khoản cho nhiều học sinh
     *
     * @param principal
     * @param kidsActionRequestList
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/many-cancel-actived")
    public ResponseEntity updateManyKidsCancelActive(@CurrentUser UserPrincipal principal, @Valid @RequestBody List<KidsActionRequest> kidsActionRequestList) {
        RequestUtils.getFirstRequest(principal, kidsActionRequestList);
        kidsService.actionActiveManyKids(principal, kidsActionRequestList, AppConstant.APP_FALSE);
        return NewDataResponse.setDataCustom(null,"Hủy kích hoạt tài khoản cho các học sinh thành công");

    }

    /**
     * kích hoạt nhận sms cho nhiều học sinh
     *
     * @param principal
     * @param kidsActionRequestList
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/many-actived-sms")
    public ResponseEntity updateManyKidsActiveSMS(@CurrentUser UserPrincipal principal, @Valid @RequestBody List<KidsActionRequest> kidsActionRequestList) {
        RequestUtils.getFirstRequest(principal, kidsActionRequestList);
        kidsService.actionActiveManyKidsSMS(kidsActionRequestList, AppConstant.APP_TRUE);
        return NewDataResponse.setDataCustom(null,"Kích hoạt SMS cho các học sinh thành công");

    }

    /**
     * hủy kích hoạt nhận sms cho nhiều học sinh
     *
     * @param principal
     * @param kidsActionRequestList
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/many-cancel-actived-sms")
    public ResponseEntity updateManyKidsCancelActiveSMS(@CurrentUser UserPrincipal principal, @Valid @RequestBody List<KidsActionRequest> kidsActionRequestList) {
        RequestUtils.getFirstRequest(principal, kidsActionRequestList);
        kidsService.actionActiveManyKidsSMS(kidsActionRequestList, AppConstant.APP_FALSE);
        return NewDataResponse.setDataCustom(null,"Hủy kích hoạt SMS cho các học sinh thành công");
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/change-class")
    public ResponseEntity changeClassKids(@CurrentUser UserPrincipal principal, @Valid @RequestBody KidsChangeClassRequest request) throws IOException {
        RequestUtils.getFirstRequest(principal, request);
        boolean checkChangeClass = kidsService.actionChangeClassKids(principal, request);
        return NewDataResponse.setDataCustom(checkChangeClass, MessageWebConstant.CHANGE_CLASS);
    }

    /**
     * bỏ thao tác xóa nhiều học sinh
     *
     * @param principal
     * @param kidsActionRequestList
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/delete-many-kids")
    public ResponseEntity deleteManyKids(@CurrentUser UserPrincipal principal, @Valid @RequestBody List<KidsActionRequest> kidsActionRequestList) {
        RequestUtils.getFirstRequest(principal, kidsActionRequestList);
        boolean check = kidsService.deleteManyKid(principal, kidsActionRequestList);
        return NewDataResponse.setDataCustom(check, MessageWebConstant.DELETE_STUDENT);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/export/message-parent")
    public ResponseEntity exportExcelMessageParent(@CurrentUser UserPrincipal principal, @Valid IdListRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkPlusOrTeacher(principal);
        List<ExcelResponse> responseList = excelExportService.getExportMessageParent(principal, request);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * Xuất excel dặn thuốc
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/export/message-medicine")
    public ResponseEntity exportExcelMessageMedicine(@CurrentUser UserPrincipal principal, @Valid IdListRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkPlusOrTeacher(principal);
        List<ExcelResponse> responseList = excelExportService.getExportMedicine(principal, request);
        return NewDataResponse.setDataSearch(responseList);
    }
    /**
     * Xuất excel dặn thuốc theo ngay
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/export/message-medicine-date")
    public ResponseEntity exportExcelMessageMedicineDate(@CurrentUser UserPrincipal principal, @Valid ExportMedicineRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkPlusOrTeacher(principal);
        List<ExcelResponse> responseList = excelExportService.getExportMedicineDate(principal, request);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * Xuất excel xin nghỉ
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/export/message-absent")
    public ResponseEntity exportExcelMessageAbsent(@CurrentUser UserPrincipal principal, @Valid IdListRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkPlusOrTeacher(principal);
        List<ExcelResponse> responseList = excelExportService.getExportAbsentLetter(principal, request);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * Xuất excel SMS
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/export/excel-sms")
    public ResponseEntity exportExcelSMS(@CurrentUser UserPrincipal principal, @Valid IdListRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkPlusOrTeacher(principal);
        List<ExcelResponse> responseList = excelExportService.getExportExcelSMS(principal.getIdSchoolLogin(), request);
        return NewDataResponse.setDataSearch(responseList);
    }

}
