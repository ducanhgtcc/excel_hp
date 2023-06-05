package com.example.onekids_project.repository;

import com.example.onekids_project.entity.school.CycleMoney;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author lavanviet
 */
public interface CycleMoneyRepository extends JpaRepository<CycleMoney, Long> {
    Optional<CycleMoney> findBySchoolId(Long idSchool);
}
