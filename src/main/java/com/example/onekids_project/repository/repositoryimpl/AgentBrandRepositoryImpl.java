package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.agent.Brand;
import com.example.onekids_project.repository.repositorycustom.AgentBrandRepositoryCustom;
import com.example.onekids_project.request.brand.SearchAgentBrandRequest;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AgentBrandRepositoryImpl extends BaseRepositoryimpl<Brand> implements AgentBrandRepositoryCustom {

    @Override
    public List<Brand> searchAgentBrand(Long idSchoolLogin, SearchAgentBrandRequest searchAgentBrandRequest) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (searchAgentBrandRequest != null) {
            if (StringUtils.isNotBlank(searchAgentBrandRequest.getName())) {
                queryStr.append("and brand_name like :brandName ");
                mapParams.put("brandName", "%" + searchAgentBrandRequest.getName() + "%");
            }

        }
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public Optional<Brand> findByIdBrand(Long idSchoolLogin, Long id) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (id != null) {
            queryStr.append("and id=:id");
            mapParams.put("id", id);
        }
        List<Brand> brandList = findAllNoPaging(queryStr.toString(), mapParams);
        if (brandList.size() > 0) {
            return Optional.ofNullable(brandList.get(0));
        }
        return Optional.empty();
    }

//    @Override
//    public Optional<Agent> findByIdAgentBrand(Long idSchoolLogin, Long id) {
//        StringBuilder queryStr = new StringBuilder("");
//        Map<String, Object> mapParams = new HashMap<>();
//        if (id != null) {
//            queryStr.append("and id=:id");
//            mapParams.put("id", id);
//        }
//        List<Agent> agentList = findAllNoPaging(queryStr.toString(), mapParams);
//        if (agentList.size() > 0) {
//            return Optional.ofNullable(agentList.get(0));
//        }
//        return Optional.empty();
//    }
}

