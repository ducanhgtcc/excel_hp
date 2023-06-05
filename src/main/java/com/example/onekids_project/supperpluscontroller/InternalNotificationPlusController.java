package com.example.onekids_project.supperpluscontroller;

import com.example.onekids_project.master.request.notify.SearchInternalNotificationPlus;
import com.example.onekids_project.master.response.notify.InternalNotificationPlusResponse;
import com.example.onekids_project.master.response.notify.ListInternalNotificationPlusResponse;
import com.example.onekids_project.master.service.InternalNotificationPlusService;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * date 2021-08-12 2:19 PM
 *
 * @author nguyễn văn thụ
 */
@RestController
@RequestMapping("/web/internal-notification-plus")
public class InternalNotificationPlusController {

    @Autowired
    private InternalNotificationPlusService internalNotificationPlusService;

    /**
     * Search all data
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "search")
    public ResponseEntity findInternalNotificationsPlus(@CurrentUser UserPrincipal principal, @Valid SearchInternalNotificationPlus request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        ListInternalNotificationPlusResponse responseList = internalNotificationPlusService.findInternalNotificationPlus(request);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * Detail
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "{id}")
    public ResponseEntity findByIdInternalNotificationsPlus(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequestPlus(principal);
        InternalNotificationPlusResponse response = internalNotificationPlusService.findByIdNotificationPlus(id);
        return NewDataResponse.setDataSearch(response);
    }
}
