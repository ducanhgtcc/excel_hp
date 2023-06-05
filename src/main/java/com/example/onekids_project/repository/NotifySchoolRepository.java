package com.example.onekids_project.repository;

import com.example.onekids_project.entity.school.NotifySchool;
import com.example.onekids_project.repository.repositorycustom.NotifySchoolRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * date 2021-10-21 9:09 AM
 *
 * @author nguyễn văn thụ
 */
public interface NotifySchoolRepository extends JpaRepository<NotifySchool, Long>, NotifySchoolRepositoryCustom {

    Optional<NotifySchool> findByIdAndDelActiveTrue(Long id);

    List<NotifySchool> findByIdSchoolAndActiveTrueAndDelActiveTrueOrderByIdDesc(Long idSchool);
}
