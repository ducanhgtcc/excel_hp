package com.example.onekids_project.repository;

import com.example.onekids_project.entity.usermaster.AgentMaster;
import com.example.onekids_project.repository.repositorycustom.AgentMasterRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AgentMasterRepository extends JpaRepository<AgentMaster, Long>, AgentMasterRepositoryCustom {
    List<AgentMaster> findByAgentIdAndDelActiveTrue(Long idAgent);

    /**
     * find agentmaster by id
     * @param id
     * @return
     */
    Optional<AgentMaster> findByIdAndDelActiveTrue(Long id);
}
