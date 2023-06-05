package com.example.onekids_project.repository;

import com.example.onekids_project.entity.employee.EmployeeNotify;
import com.example.onekids_project.repository.repositorycustom.NotifyPlusSchoolRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotifyPlusSchoolRepository extends JpaRepository<EmployeeNotify, Long>, NotifyPlusSchoolRepositoryCustom {
}
