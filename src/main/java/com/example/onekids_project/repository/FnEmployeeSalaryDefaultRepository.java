package com.example.onekids_project.repository;

import com.example.onekids_project.entity.finance.employeesalary.FnEmployeeSalaryDefault;
import com.example.onekids_project.repository.repositorycustom.FnEmployeeSalaryDefaultRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FnEmployeeSalaryDefaultRepository extends JpaRepository<FnEmployeeSalaryDefault, Long>, FnEmployeeSalaryDefaultRepositoryCustom {

    Optional<FnEmployeeSalaryDefault> findByIdAndDelActiveTrue(Long id);

    Optional<FnEmployeeSalaryDefault> findByIdAndInfoEmployeeSchoolSchoolIdAndDelActiveTrue(Long id, Long idSchool);

    Optional<FnEmployeeSalaryDefault> findByInfoEmployeeSchoolIdAndFnSalaryIdAndDelActiveTrue(Long idInfoEmployee, Long idFnSalary);

    List<FnEmployeeSalaryDefault> findByIdInAndInfoEmployeeSchoolSchoolIdAndDelActiveTrue(List<Long> idList, Long idSchool);
}
