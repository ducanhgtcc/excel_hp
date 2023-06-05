package com.example.onekids_project.repository;

import com.example.onekids_project.entity.school.ConfigNotifyPlus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * date 2021-04-10 16:19
 *
 * @author lavanviet
 */
public interface ConfigNotifyPlusRepository extends JpaRepository<ConfigNotifyPlus, Long> {
    Optional<ConfigNotifyPlus> findBySchoolId(Long idSchool);
}
