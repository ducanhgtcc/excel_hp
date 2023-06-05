package com.example.onekids_project.repository;

import com.example.onekids_project.entity.agent.AgentSms;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgentSmsRepository extends JpaRepository<AgentSms, Long> {
    List<AgentSms> findByAgentId(Long idAgent, Sort sortBy);
}
