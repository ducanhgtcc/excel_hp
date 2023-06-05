package com.example.onekids_project.repository;

import com.example.onekids_project.entity.employee.EmployeeStatusTimeline;
import com.example.onekids_project.entity.kids.KidsStatusTimeline;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EmployeeStatusTimelineRepository extends JpaRepository<EmployeeStatusTimeline, Long> {
    Optional<EmployeeStatusTimeline> findFirstByInfoEmployeeSchoolIdOrderByStartDateDesc(Long idInforEmployee);

    List<EmployeeStatusTimeline> findByInfoEmployeeSchoolIdOrderByStartDateDesc(Long idInforEmployee);

    Optional<EmployeeStatusTimeline> findFirstByInfoEmployeeSchoolIdAndStartDateIsBeforeOrderByIdDesc(Long idInfoEmployee, LocalDate date);
}
