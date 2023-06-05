package com.example.onekids_project.repository;

import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.entity.usermaster.AgentMaster;
import com.example.onekids_project.entity.usermaster.SchoolMaster;
import com.example.onekids_project.repository.repositorycustom.SchoolMasterRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SchoolMasterRepository extends JpaRepository<SchoolMaster, Long>, SchoolMasterRepositoryCustom {
    List<SchoolMaster> findBySchoolId(Long idSchool);

    /**
     * find by id
     * @param id
     * @return
     */
    Optional<SchoolMaster> findByIdAndDelActiveTrue(Long id);
}
