package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.agent.Agent;
import com.example.onekids_project.request.agent.SearchAgentRequest;
import com.example.onekids_project.request.brand.FindAgentBrandRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AgentRepositoryCustom {

    /**
     * tìm kiếm tất cả đại lý
     * @param pageable
     * @return
     */
    List<Agent> findAllAgent(Pageable pageable);

    /**
     * tìm kiếm đại lý theo tùy chọn
     * @param pageable
     * @param searchAgentRequest
     * @return
     */
    List<Agent> searchAgent(Pageable pageable, SearchAgentRequest searchAgentRequest);

    List<Agent> findAllA();

    long countTotalAccount(FindAgentBrandRequest request);

    List<Agent> findAgent(FindAgentBrandRequest request);
}
