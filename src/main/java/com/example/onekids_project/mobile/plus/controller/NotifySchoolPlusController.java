package com.example.onekids_project.mobile.plus.controller;

import com.example.onekids_project.mobile.plus.request.notifyschool.NotifySchoolActivePlusRequest;
import com.example.onekids_project.mobile.plus.request.notifyschool.NotifySchoolPlusRequest;
import com.example.onekids_project.mobile.plus.request.notifyschool.SearchNotifySchoolPlusRequest;
import com.example.onekids_project.mobile.plus.response.notifyschool.ListNotifySchoolPlusResponse;
import com.example.onekids_project.mobile.plus.response.notifyschool.NotifySchoolPlusResponse;
import com.example.onekids_project.mobile.service.servicecustom.NotifySchoolPlusService;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.RequestUtils;
import com.example.onekids_project.validate.CommonValidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * date 2021-10-22 11:03 AM
 *
 * @author nguyễn văn thụ
 */
@RestController
@RequestMapping("/mob/plus/notify-school")
public class NotifySchoolPlusController {

    @Autowired
    private NotifySchoolPlusService notifySchoolPlusService;

    /**
     * Tìm kiếm thông báo nhà trường cho trường
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public ResponseEntity searchNotifySchoolPlus(@CurrentUser UserPrincipal principal, @Valid SearchNotifySchoolPlusRequest request) {
        RequestUtils.getFirstRequestPlus(principal);
        CommonValidate.checkDataPlus(principal);
        ListNotifySchoolPlusResponse data = notifySchoolPlusService.searchNotifySchoolPlus(principal.getIdSchoolLogin(), request);
        return NewDataResponse.setDataSearch(data);
    }

    /**
     * Chi tiết thông báo
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/detail/{id}")
    public ResponseEntity findByNotifySchoolPlus(@CurrentUser UserPrincipal principal, @PathVariable Long id){
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataPlus(principal);
        NotifySchoolPlusResponse data = notifySchoolPlusService.findByNotifySchoolPlus(id);
        return NewDataResponse.setDataSearch(data);
    }

    /**
     * Tạo thông báo nhà trường cho mobile plus
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/create")
    public ResponseEntity createNotifySchoolPlus(@CurrentUser UserPrincipal principal, @Valid @ModelAttribute NotifySchoolPlusRequest request){
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        boolean check = notifySchoolPlusService.saveNotifySchoolPlus(principal, request);
        return NewDataResponse.setDataCreate(check);

    }

    /**
     * Cập nhật thông báo
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/update")
    public ResponseEntity updateNotifySchoolPlus(@CurrentUser UserPrincipal principal, @Valid @ModelAttribute NotifySchoolPlusRequest request){
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        boolean check = notifySchoolPlusService.saveNotifySchoolPlus(principal, request);
        return NewDataResponse.setDataUpdate(check);

    }

    /**
     * Xóa thông báo
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/delete/{id}")
    public ResponseEntity deleteNotifySchoolPlus(@CurrentUser UserPrincipal principal, @PathVariable Long id){
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataPlus(principal);
        boolean check = notifySchoolPlusService.deleteNotifySchoolPlus(id);
        return NewDataResponse.setDataDelete(check);

    }

    /**
     * Set trạng thái hiện thông báo
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/active")
    public ResponseEntity activeNotifySchoolPlus(@CurrentUser UserPrincipal principal, @Valid @RequestBody NotifySchoolActivePlusRequest request){
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        boolean check = notifySchoolPlusService.activeNotifySchoolPlus(request);
        return NewDataResponse.setDataActive(request.isActive());
    }
}
