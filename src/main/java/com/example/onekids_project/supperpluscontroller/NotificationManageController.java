package com.example.onekids_project.supperpluscontroller;

import com.example.onekids_project.master.request.notify.NotificationManageRequest;
import com.example.onekids_project.master.request.notify.NotificationStatusRequest;
import com.example.onekids_project.master.request.notify.NotifyManageDateRequest;
import com.example.onekids_project.master.request.notify.SearchNotificationRequest;
import com.example.onekids_project.master.response.notify.ListNotifyManageResponse;
import com.example.onekids_project.master.response.notify.NotifyManageDateResponse;
import com.example.onekids_project.master.service.NotificationManageService;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * date 2021-08-02 9:47 AM
 *
 * @author nguyễn văn thụ
 */
@RestController
@RequestMapping("/web/notification-manage")
public class NotificationManageController {
    @Autowired
    private NotificationManageService notificationManageService;

    /**
     * Search all data
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "search")
    public ResponseEntity findNotifications(@CurrentUser UserPrincipal principal, @Valid SearchNotificationRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        ListNotifyManageResponse responseList = notificationManageService.findNotifyManage(principal.getIdSchoolLogin(), request);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * lấy dữ liệu date notify
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "date-notification/{id}")
    public ResponseEntity findByDateNotify(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequestPlus(principal);
        NotifyManageDateResponse response = notificationManageService.findDateNotification(id);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * thêm mới dữ liệu date notify
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "date-notification")
    public ResponseEntity createDateNotify(@CurrentUser UserPrincipal principal, @RequestBody NotifyManageDateRequest request ) {
        RequestUtils.getFirstRequestPlus(principal, request);
        boolean check = notificationManageService.createDateNotification(principal.getIdSchoolLogin(), request);
        return NewDataResponse.setDataCreate(check);
    }

    /**
     * cập nhật dữ liệu date notify
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "date-notification")
    public ResponseEntity updateDateNotify(@CurrentUser UserPrincipal principal, @RequestBody NotifyManageDateRequest request ) {
        RequestUtils.getFirstRequestPlus(principal, request);
        boolean check = notificationManageService.updateDateNotification(request);
        return NewDataResponse.setDataUpdate(check);
    }

    /**
     * xóa dữ liệu date notify
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "date-notification/{id}")
    public ResponseEntity deleteDateNotify(@CurrentUser UserPrincipal principal, @PathVariable Long id ) {
        RequestUtils.getFirstRequestPlus(principal);
        boolean check = notificationManageService.deleteDateNotification(id);
        return NewDataResponse.setDataDelete(check);
    }

    /**
     * Cập nhật thông tin gửi tin
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity getUpdateNotification(@CurrentUser UserPrincipal principal, @Valid @RequestBody NotificationManageRequest request) {
        boolean check = notificationManageService.getUpdateNotifyManage(principal.getIdSchoolLogin(), request);
        return NewDataResponse.setDataUpdate(check);
    }

    /**
     * set trạng thái gửi tin
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "active")
    public ResponseEntity getStatusNotification(@CurrentUser UserPrincipal principal, @Valid @RequestBody NotificationStatusRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = notificationManageService.getStatusNotifyManage(principal.getIdSchoolLogin(), request);
        return NewDataResponse.setDataActive(request.isStatus());
    }

}
