package com.example.onekids_project.controller.finance;

import com.example.onekids_project.request.cyclemoney.CycleMoneyRequest;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.response.cyclemoney.CycleMoneyResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.finance.CycleMoneyService;
import com.example.onekids_project.util.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author lavanviet
 */
@RestController
@RequestMapping("/web/fn/cycle-money")
public class CycleMoneyController {
    @Autowired
    private CycleMoneyService cycleMoneyService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getCycleMoneyController(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        CycleMoneyResponse data = cycleMoneyService.getCycleMoneyService();
        return NewDataResponse.setDataSearch(data);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity getCycleMoneyController(@CurrentUser UserPrincipal principal, @RequestBody @Valid CycleMoneyRequest request) {
        RequestUtils.getFirstRequest(principal);
        cycleMoneyService.updateCycleMoneyService(request);
        return NewDataResponse.setDataUpdate(true);
    }
}
