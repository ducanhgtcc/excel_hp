package com.example.onekids_project.repository;

import com.example.onekids_project.entity.agent.AgentSms;
import com.example.onekids_project.entity.school.SchoolSms;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SchoolSmsRepository extends JpaRepository<SchoolSms, Long>{
    List<SchoolSms> findBySchoolId(Long idSchool, Sort sort);
}
