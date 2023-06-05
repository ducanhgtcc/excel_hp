package com.example.onekids_project.master.controller;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.master.request.menu.MenuSupportRequest;
import com.example.onekids_project.master.request.menu.MenuUpdateRequest;
import com.example.onekids_project.master.response.MenuSupportResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.MenuSupportService;
import com.example.onekids_project.util.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * date 2021-10-15 14:47
 *
 * @author lavanviet
 */
@RestController
@RequestMapping("/web/menu")
public class MenuSupportController {
    @Autowired
    private MenuSupportService menuSupportService;

    @RequestMapping(method = RequestMethod.GET, value = "/support/search")
    public ResponseEntity getSupport(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        List<MenuSupportResponse> responseList = menuSupportService.getSupport();
        return NewDataResponse.setDataSearch(responseList);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/support")
    public ResponseEntity createSupport(@CurrentUser UserPrincipal principal, @Valid @RequestBody MenuSupportRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        menuSupportService.createSupport(request);
        return NewDataResponse.setDataCreate(AppConstant.APP_TRUE);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/support")
    public ResponseEntity updateSupport(@CurrentUser UserPrincipal principal, @Valid @RequestBody MenuUpdateRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        menuSupportService.updateSupport(request);
        return NewDataResponse.setDataUpdate(AppConstant.APP_TRUE);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/support/{id}")
    public ResponseEntity deleteSupportById(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequest(principal, id);
        menuSupportService.deleteSupportById(id);
        return NewDataResponse.setDataDelete(AppConstant.APP_TRUE);
    }
}
