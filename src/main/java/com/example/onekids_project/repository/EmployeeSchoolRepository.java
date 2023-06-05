package com.example.onekids_project.repository;

import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.repository.repositorycustom.EmployeeSchoolRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeSchoolRepository extends JpaRepository<InfoEmployeeSchool, Long>, EmployeeSchoolRepositoryCustom {


}
