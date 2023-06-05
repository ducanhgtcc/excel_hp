package com.example.onekids_project.master.controller;

import com.example.onekids_project.common.MessageWebConstant;
import com.example.onekids_project.master.request.agent.AgentMasterSearchRequest;
import com.example.onekids_project.master.request.agent.CreateAccountAgentRequest;
import com.example.onekids_project.master.response.agent.AgentMasterForAgentResponse;
import com.example.onekids_project.master.response.agent.AgentMasterResponse;
import com.example.onekids_project.master.response.agent.AgentMasterUpdateRequest;
import com.example.onekids_project.master.service.AccountAgentService;
import com.example.onekids_project.response.common.DataResponse;
import com.example.onekids_project.response.common.ErrorResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.security.payload.MaUserActiveRequest;
import com.example.onekids_project.security.service.servicecustom.MaUserService;
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
@RequestMapping("/web/account-agent")
public class AgentMasterController {

    private static final Logger logger = LoggerFactory.getLogger(AgentMasterController.class);

    @Autowired
    private AccountAgentService accountAgentService;

    @Autowired
    private MaUserService maUserService;

    /**
     * find all account agent
     *
     * @return
     */
    @GetMapping
    public ResponseEntity findAllAgentMaster(AgentMasterSearchRequest agentMasterSearchRequest) {
            List<AgentMasterResponse> agentMasterResponseList = accountAgentService.getAllAgentMaster(agentMasterSearchRequest);
            return NewDataResponse.setDataSearch(agentMasterResponseList);
    }

    /**
     * create account for agent
     *
     * @param createAccountAgentRequest
     * @return
     */
    @PostMapping
    public ResponseEntity createAccountAgent(@CurrentUser UserPrincipal principal, @Valid @RequestBody CreateAccountAgentRequest createAccountAgentRequest) {
        logger.info("username: {}, fullName: {}, {}", principal.getUsername(), principal.getFullName());
        boolean checkCreate = accountAgentService.createAccountAgent(createAccountAgentRequest);
        return NewDataResponse.setDataCustom(checkCreate, MessageWebConstant.CREATE_ACCOUNT);
    }

    /**
     * update agent master
     *
     * @param agentMasterUpdateRequest
     * @return
     */
    @PutMapping()
    public ResponseEntity update(@Valid @RequestBody AgentMasterUpdateRequest agentMasterUpdateRequest) {
            AgentMasterResponse agentMasterResponse = accountAgentService.updateAccountAgent(agentMasterUpdateRequest);
            return NewDataResponse.setDataUpdate(agentMasterResponse);
    }

    /**
     * delete agent master
     *
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") Long id) {
            boolean checkDelete = accountAgentService.deleteAgentMaster(id);
            return NewDataResponse.setDataDelete(checkDelete);
    }

    /**
     * update active account agent
     *
     * @param maUserActiveRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/active")
    public ResponseEntity updateAgentActive(@Valid @RequestBody MaUserActiveRequest maUserActiveRequest) {
            boolean checkActive = maUserService.checkActiveUser(maUserActiveRequest);
            return NewDataResponse.setDataUpdate(checkActive);
    }

    /**
     * update active mamy account agent
     *
     * @param maUserActiveRequestList
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/active-many")
    public ResponseEntity updateAgentManyActive(@Valid @RequestBody List<MaUserActiveRequest> maUserActiveRequestList) {
            boolean checkActive = maUserService.checkActiveManyUser(maUserActiveRequestList);
            return NewDataResponse.setDataUpdate(checkActive);
    }

    /**
     * Lấy tài khoản đại lý theo ID Agent
     *
     * @param idAgent
     * @return
     */
    @GetMapping(value = "/id-agent/{idAgent}")
    public ResponseEntity getByIdAgent(@PathVariable("idAgent") Long idAgent) {
            List<AgentMasterForAgentResponse> agentMasterForAgentResponseList = accountAgentService.getAccountAgentByIdAgent(idAgent);
            return NewDataResponse.setDataSearch(agentMasterForAgentResponseList);

    }

}
