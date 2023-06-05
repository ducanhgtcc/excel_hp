package com.example.onekids_project.repository;

import com.example.onekids_project.entity.school.ConfigPlus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfigPlusRepository extends JpaRepository<ConfigPlus, Long> {

    Optional<ConfigPlus> findBySchoolId(Long idSchool);
}
