package com.example.onekids_project.mobile.parent.controller;

import com.example.onekids_project.mobile.parent.request.kidstransfer.KidsTransferParentCreateRequest;
import com.example.onekids_project.mobile.parent.request.kidstransfer.KidsTransferParentUpdateRequest;
import com.example.onekids_project.mobile.parent.response.kidstransfer.KidsTransferParentResponse;
import com.example.onekids_project.mobile.parent.service.servicecustom.KidsTransferParentService;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * @author lavanviet
 */
@RestController
@RequestMapping(value = "/mob/parent/transfer")
public class KidsTransferParentController {
    @Autowired
    private KidsTransferParentService service;

    /**
     * đưa đi học
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public ResponseEntity kidsTransferSearch(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        List<KidsTransferParentResponse> data = service.kidsTransferSearch();
        return NewDataResponse.setDataSearch(data);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity kidsTransferCreate(@CurrentUser UserPrincipal principal, @Valid @ModelAttribute KidsTransferParentCreateRequest request) throws IOException {
        RequestUtils.getFirstRequestParent(principal, request);
        service.kidsTransferCreateService(principal, request);
        return NewDataResponse.setDataCreate(true);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity kidsTransferInUpdate(@CurrentUser UserPrincipal principal, @Valid @ModelAttribute KidsTransferParentUpdateRequest request) throws IOException {
        RequestUtils.getFirstRequest(principal, request);
        service.kidsTransferUpdateService(principal, request);
        return NewDataResponse.setDataUpdate(true);
    }
}
