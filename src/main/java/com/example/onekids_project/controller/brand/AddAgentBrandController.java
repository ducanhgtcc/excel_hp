package com.example.onekids_project.controller.brand;

import com.example.onekids_project.common.MessageWebConstant;
import com.example.onekids_project.master.response.agent.BrandforAgentResponse;
import com.example.onekids_project.request.base.IdObjectRequest;
import com.example.onekids_project.request.brand.FindAgentBrandRequest;
import com.example.onekids_project.response.brandManagement.ListFindAgentBrandResponse;
import com.example.onekids_project.response.common.DataResponse;
import com.example.onekids_project.response.common.ErrorResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.AgentService;
import com.example.onekids_project.service.servicecustom.BrandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/web/agent-brand")
public class AddAgentBrandController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AgentService agentService;

    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public ResponseEntity getAccountUserSchool(@CurrentUser UserPrincipal principal, @Valid FindAgentBrandRequest request) {
        logger.info("username: {}, fullName: {}, {}", principal.getUsername(), principal.getFullName(), request);
        ListFindAgentBrandResponse data = agentService.findAllAgentMaster(request);
        return NewDataResponse.setDataCustom(data, MessageWebConstant.FIND_ACCOUNT);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/find/{id}")
    public ResponseEntity findAdminOfSchool(@PathVariable("id") Long idAgent) {
            List<BrandforAgentResponse> brandForAgentResponseList = agentService.findBrandforAgent(idAgent);
            return NewDataResponse.setDataSearch(brandForAgentResponseList);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/agent-brand/{id}")
    public ResponseEntity updateAdminOfSchool(@PathVariable("id") Long idAgent, @Valid @RequestBody List<IdObjectRequest> idObjectRequestList) {
            boolean  checkUpdate = agentService.updateBrandForAgent(idAgent, idObjectRequestList);
            return NewDataResponse.setDataUpdate(checkUpdate);
    }
}
