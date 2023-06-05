package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.usermaster.AgentMaster;
import com.example.onekids_project.master.request.agent.AgentMasterSearchRequest;
import com.example.onekids_project.repository.repositorycustom.AgentMasterRepositoryCustom;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgentMasterRepositoryImpl extends BaseRepositoryimpl<AgentMaster> implements AgentMasterRepositoryCustom {
    @Override
    public List<AgentMaster> findAllAgentMaster(AgentMasterSearchRequest agentMasterSearchRequest) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (agentMasterSearchRequest != null) {

            if (agentMasterSearchRequest.getIdAgent() != null) {
                queryStr.append("and id_agent =:idAgent ");
                mapParams.put("idAgent", agentMasterSearchRequest.getIdAgent());
            }
            String name = agentMasterSearchRequest.getFullName();
            if (StringUtils.isNotBlank(name)) {
                name = name.trim();
                queryStr.append("and full_name like :fullName ");
                mapParams.put("fullName", "%" + name + "%");
            }
            queryStr.append("order by id_agent ASC ");

        }
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

}
