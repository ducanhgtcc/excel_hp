package com.example.onekids_project.master.controller;

import com.example.onekids_project.dto.AgentDTO;
import com.example.onekids_project.master.request.ActiveAgentRequest;
import com.example.onekids_project.master.request.AgentSmsRequest;
import com.example.onekids_project.master.response.SmsAgentResponse;
import com.example.onekids_project.request.agent.CreateAgentRequest;
import com.example.onekids_project.request.agent.SearchAgentRequest;
import com.example.onekids_project.request.agent.UpdateAgentRequest;
import com.example.onekids_project.response.agent.AgentOtherResponse;
import com.example.onekids_project.response.agent.AgentResponse;
import com.example.onekids_project.response.agent.AgentUniqueResponse;
import com.example.onekids_project.response.agent.ListAgentResponse;
import com.example.onekids_project.response.common.DataResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.AgentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/web/agent")
public class AgentController {

    private static final Logger logger = LoggerFactory.getLogger(AgentController.class);

    @Autowired
    private AgentService agentService;

    @RequestMapping(method = RequestMethod.GET, value = "/common")
    public ResponseEntity findAllAgentOther(@CurrentUser UserPrincipal principal) {
        logger.info("username: {}, fullName: {}, {}", principal.getUsername(), principal.getFullName());
        List<AgentOtherResponse> dataList = agentService.findAllAgentOther();
        return NewDataResponse.setDataSearch(dataList);
    }

    /**
     * find all only agent
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/unique")
    public ResponseEntity findAllAgentUnique() {
        List<AgentUniqueResponse> agentUniqueResponseList = agentService.findAllAgentUnique();
        return NewDataResponse.setDataSearch(agentUniqueResponseList);
    }

    /**
     * find all agent
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity findAll() {
        ListAgentResponse listAgentResponse = agentService.findAllAgent(null);
        return DataResponse.getData(listAgentResponse, HttpStatus.OK);
    }

    /**
     * Lấy đại lý theo ID
     *
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity getById(@PathVariable("id") Long id) {
        Optional<AgentDTO> agentDTOOptional = agentService.findByIdAgent(id);
        agentDTOOptional.get().setSchoolListResponse(agentDTOOptional.get().getSchoolList());
        return NewDataResponse.setDataSearch(agentDTOOptional.get());

    }

    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public ResponseEntity search(SearchAgentRequest searchAgentRequest) {
        ListAgentResponse listAgentResponse = agentService.searchAgent(null, searchAgentRequest);
        return NewDataResponse.setDataSearch(listAgentResponse);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity create(@Valid @RequestBody CreateAgentRequest createAgentRequest) {
        AgentResponse agentResponse = agentService.createAgent(createAgentRequest);
        return NewDataResponse.setDataCreate(agentResponse);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity update(@PathVariable(name = "id") Long id, @Valid @RequestBody UpdateAgentRequest updateAgentRequest) {
        AgentResponse gradeEditResponse = agentService.updateAgent(updateAgentRequest);
        return NewDataResponse.setDataUpdate(gradeEditResponse);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") Long id) {
        boolean checkDelete = agentService.deleteAgent(id);
        return NewDataResponse.setDataDelete(checkDelete);

    }

    @PutMapping("/add-sms-agent")
    public ResponseEntity addSmsAgent(@RequestBody AgentSmsRequest agentSmsRequest) {
        boolean check = agentService.saveAgentSms(agentSmsRequest);
        return NewDataResponse.setDataUpdate(check);

    }

    /**
     * Lịch sử cấp SMS
     *
     * @return
     */
    @GetMapping("/add-sms-agent/{idAgent}")
    public ResponseEntity getAllAgentSms(@PathVariable("idAgent") long idAgent) {
        List<SmsAgentResponse> agentResponseList = agentService.findAgentSmsByIdAgent(idAgent);
        return NewDataResponse.setDataSearch(agentResponseList);
    }

    /**
     * Kích hoạt nhiều agent
     *
     * @return
     */
    @PutMapping("/active-multi-agent")
    public ResponseEntity updateMultiActiveAgent(@RequestBody ActiveAgentRequest activeAgentRequest) {
        boolean checkMultiActive = agentService.updateMultiActiveAgent(activeAgentRequest.getIds(), activeAgentRequest.isActiveOrUnActive());
        return NewDataResponse.setDataUpdate(checkMultiActive);
    }

}
