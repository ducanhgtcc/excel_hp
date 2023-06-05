package com.example.onekids_project.master.service;

import com.example.onekids_project.master.request.agent.AgentMasterSearchRequest;
import com.example.onekids_project.master.request.agent.CreateAccountAgentRequest;
import com.example.onekids_project.master.response.agent.AgentMasterForAgentResponse;
import com.example.onekids_project.master.response.agent.AgentMasterResponse;
import com.example.onekids_project.master.response.agent.AgentMasterUpdateRequest;

import java.util.List;

public interface AccountAgentService {

    /**
     * create account of agent
     *
     * @param createAccountAgentRequest
     * @return
     */
    boolean createAccountAgent(CreateAccountAgentRequest createAccountAgentRequest);

    /**
     * get all agent master
     * @param agentMasterSearchRequest
     * @return
     */
    List<AgentMasterResponse> getAllAgentMaster(AgentMasterSearchRequest agentMasterSearchRequest);

    /**
     * find agentmaster for one agent
     * @param idAgent
     * @return
     */
    List<AgentMasterForAgentResponse> getAccountAgentByIdAgent(Long idAgent);

    /**
     * update agent master
     * @param agentMasterUpdateRequest
     * @return
     */
    AgentMasterResponse updateAccountAgent(AgentMasterUpdateRequest agentMasterUpdateRequest);

    /**
     * delete agent master
     * @param id
     * @return
     */
    boolean deleteAgentMaster(Long id);
}
