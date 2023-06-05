package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.usermaster.AgentMaster;
import com.example.onekids_project.master.request.agent.AgentMasterSearchRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface AgentMasterRepositoryCustom {
    /**
     * find all agent master
     * @param agentMasterSearchRequest
     * @return
     */
    List<AgentMaster> findAllAgentMaster(AgentMasterSearchRequest agentMasterSearchRequest);
}
