package com.example.onekids_project.repository;

import com.example.onekids_project.entity.DemoCron;
import com.example.onekids_project.entity.classes.ClassSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DemoCronRepository extends JpaRepository<DemoCron, Long> {
}
