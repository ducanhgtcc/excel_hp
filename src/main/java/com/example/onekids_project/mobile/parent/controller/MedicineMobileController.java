package com.example.onekids_project.mobile.parent.controller;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.mobile.parent.request.medicine.MedicineMobileRequest;
import com.example.onekids_project.mobile.parent.response.medicine.ListMedicineMobileResponse;
import com.example.onekids_project.mobile.parent.response.medicine.MedicineDetailMobileResponse;
import com.example.onekids_project.mobile.parent.service.servicecustom.MedicineMobileService;
import com.example.onekids_project.mobile.request.DateTimeRequest;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/mob/parent/medicine")
public class MedicineMobileController {

    @Autowired
    private MedicineMobileService medicineMobileService;

    /**
     * tìm kiếm danh sách lời nhắn
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity searchMessageParent(@CurrentUser UserPrincipal principal, DateTimeRequest localDateTime) {
        RequestUtils.getFirstRequest(principal, localDateTime);
        CommonValidate.checkDataParent(principal);
        Pageable pageable = PageRequest.of(0, AppConstant.MAX_PAGE_ITEM);
        ListMedicineMobileResponse listMedicineMobileResponse = medicineMobileService.findMedicineMoblie(principal, pageable, localDateTime.getDateTime());
        if (listMedicineMobileResponse == null) {
            return NewDataResponse.setDataCustom(listMedicineMobileResponse,"Không có dặn thuốc nào cho phụ huynh");
        }
        return NewDataResponse.setDataSearch(listMedicineMobileResponse);

    }

    @RequestMapping(method = RequestMethod.PUT, value = "/revoke/{id}")
    public ResponseEntity revokeMedicine(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkDataParent(principal);
        boolean checkUpdate = medicineMobileService.medicineRevoke(id);
        if (!checkUpdate) {
            return NewDataResponse.setDataCustom(checkUpdate,"Lỗi thu hồi dặn thuốc");
        }
        return NewDataResponse.setDataRevoke(checkUpdate);

    }

    /**
     * xem chi tiết dặn thuốc
     *
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/detail/{id}")
    public ResponseEntity findMedicineDetail(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkDataParent(principal);
        MedicineDetailMobileResponse medicineDetailMobileResponse = medicineMobileService.findMedicineDetailMobile(principal, id);
        return NewDataResponse.setDataSearch(medicineDetailMobileResponse);

    }

    @RequestMapping(method = RequestMethod.POST, value = "/create")
    public ResponseEntity createMedicine(@CurrentUser UserPrincipal principal, @Valid @ModelAttribute MedicineMobileRequest medicineMobileRequest) throws FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal, medicineMobileRequest);
        CommonValidate.checkDataParent(principal);
        boolean checkCreate = medicineMobileService.createMedicineMob(principal, medicineMobileRequest);
        if (!checkCreate) {
            return NewDataResponse.setDataCustom(checkCreate,"Lỗi tạo dặn thuốc");
        }
        return NewDataResponse.setDataCreate(checkCreate);

    }

}
