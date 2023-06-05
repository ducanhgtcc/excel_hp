package com.example.onekids_project.controller.notifyschool;

import com.example.onekids_project.request.notifyschool.NotifySchoolActiveRequest;
import com.example.onekids_project.request.notifyschool.NotifySchoolRequest;
import com.example.onekids_project.request.notifyschool.SearchNotifySchoolRequest;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.response.notifyschool.ListNotifySchoolResponse;
import com.example.onekids_project.response.notifyschool.NotifySchoolResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.notifyschool.NotifySchoolService;
import com.example.onekids_project.util.RequestUtils;
import com.example.onekids_project.validate.CommonValidate;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * date 2021-10-21 9:14 AM
 *
 * @author nguyễn văn thụ
 */
@RestController
@RequestMapping("/web/notify-school")
public class NotifySchoolController {

    @Autowired
    private NotifySchoolService notifySchoolService;

    /**
     * Tìm kiếm thông báo
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public ResponseEntity searchNotifySchool(@CurrentUser UserPrincipal principal, @Valid SearchNotifySchoolRequest request){
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        ListNotifySchoolResponse data = notifySchoolService.searchNotifySchool(principal.getIdSchoolLogin(), request);
        return NewDataResponse.setDataSearch(data);
    }

    /**
     * Chi tiết thông báo
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/detail/{id}")
    public ResponseEntity findByNotifySchool(@CurrentUser UserPrincipal principal, @PathVariable Long id){
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataPlus(principal);
        NotifySchoolResponse data = notifySchoolService.findByNotifySchool(id);
        return NewDataResponse.setDataSearch(data);
    }

    /**
     * Tạo thông báo nhà trường cho web
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/create")
    public ResponseEntity createNotifySchool(@CurrentUser UserPrincipal principal, @Valid @ModelAttribute NotifySchoolRequest request) throws FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        boolean check = notifySchoolService.saveNotifySchool(principal, request);
        return NewDataResponse.setDataCreate(check);

    }

    /**
     * Cập nhật thông báo
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/update")
    public ResponseEntity updateNotifySchool(@CurrentUser UserPrincipal principal, @Valid @ModelAttribute NotifySchoolRequest request) throws FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        boolean check = notifySchoolService.saveNotifySchool(principal, request);
        return NewDataResponse.setDataUpdate(check);

    }

    /**
     * Xóa thông báo
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/delete/{id}")
    public ResponseEntity deleteNotifySchool(@CurrentUser UserPrincipal principal, @PathVariable Long id){
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataPlus(principal);
        boolean check = notifySchoolService.deleteNotifySchool(id);
        return NewDataResponse.setDataDelete(check);

    }

    /**
     * Set trạng thái hiện thông báo
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/active")
    public ResponseEntity activeNotifySchool(@CurrentUser UserPrincipal principal, @Valid @RequestBody NotifySchoolActiveRequest request){
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        boolean check = notifySchoolService.activeNotifySchool(request);
        return NewDataResponse.setDataActive(request.isActive());
    }

}
