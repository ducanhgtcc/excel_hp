package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.agent.Agent;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.master.request.school.SearchAccountRequest;
import com.example.onekids_project.repository.repositorycustom.AgentRepositoryCustom;
import com.example.onekids_project.request.agent.SearchAgentRequest;
import com.example.onekids_project.request.brand.FindAgentBrandRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgentRepositoryImpl extends BaseRepositoryimpl<Agent> implements AgentRepositoryCustom {

    @Override
    public List<Agent> findAllAgent(Pageable pageable) {

        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append(" order by agent_name ASC");
//        queryStr.append("order by agent_name desc");
        return findAll(queryStr.toString(), mapParams, pageable);
    }

    @Override
    public List<Agent> searchAgent(Pageable pageable, SearchAgentRequest searchAgentRequest) {

        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (StringUtils.isNotBlank(searchAgentRequest.getAgentName())) {
            String CodeOrName = searchAgentRequest.getAgentName();
            queryStr.append("and agent_name like :agentName");
            mapParams.put("agentName", "%" + CodeOrName + "%");
        }
        if (searchAgentRequest.getActiveOrUnActive() != null) {
            queryStr.append(" and agent_active =:activeAgent");
            mapParams.put("activeAgent", searchAgentRequest.getActiveOrUnActive());
        }
        queryStr.append(" order by agent_name ASC");
        return findAll(queryStr.toString(), mapParams, pageable);
    }

    @Override
    public List<Agent> findAllA() {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append(" order by agent_name ASC");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<Agent> findAgent(FindAgentBrandRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        if (StringUtils.isNotBlank(request.getName())) {
            queryStr.append("and agent_name like :agentName");
            mapParams.put("agentName", "%" + request.getName() + "%");
        }
        queryStr.append(" order by agent_name ASC");
        return findAllWebPaging(queryStr.toString(), mapParams, request.getPageNumber(), request.getMaxPageItem());
    }


    @Override
    public long countTotalAccount(FindAgentBrandRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        return countAll(queryStr.toString(), mapParams);
    }

}

