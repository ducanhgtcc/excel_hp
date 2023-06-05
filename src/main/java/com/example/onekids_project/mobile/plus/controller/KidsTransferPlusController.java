package com.example.onekids_project.mobile.plus.controller;

import com.example.onekids_project.mobile.plus.response.kidstransfer.KidsTransferPlusResponse;
import com.example.onekids_project.mobile.plus.service.servicecustom.KidsTransferPlusService;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lavanviet
 */
@RestController
@RequestMapping(value = "/mob/plus/transfer")
public class KidsTransferPlusController {
    @Autowired
    private KidsTransferPlusService kidsTransferPlusService;

    /**
     * đưa đi học
     *
     * @param principal
     * @param idKid
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/in")
    public ResponseEntity kidsTransferInSearch(@CurrentUser UserPrincipal principal, @RequestParam Long idKid) {
        RequestUtils.getFirstRequest(principal, idKid);
        List<KidsTransferPlusResponse> data = kidsTransferPlusService.searchDataArrive(idKid);
        return NewDataResponse.setDataSearch(data);
    }

    /**
     * đón về
     *
     * @param principal
     * @param idKid
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/out")
    public ResponseEntity kidsTransferOutSearch(@CurrentUser UserPrincipal principal, @RequestParam Long idKid) {
        RequestUtils.getFirstRequest(principal, idKid);
        List<KidsTransferPlusResponse> data = kidsTransferPlusService.searchDataLeave(idKid);
        return NewDataResponse.setDataSearch(data);
    }
}
