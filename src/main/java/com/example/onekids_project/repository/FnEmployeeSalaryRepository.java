package com.example.onekids_project.repository;

import com.example.onekids_project.entity.finance.employeesalary.FnEmployeeSalary;
import com.example.onekids_project.repository.repositorycustom.FnEmployeeSalaryRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FnEmployeeSalaryRepository extends JpaRepository<FnEmployeeSalary, Long>, FnEmployeeSalaryRepositoryCustom {

    Optional<FnEmployeeSalary> findByIdAndDelActiveTrue(Long id);

    Optional<FnEmployeeSalary> findByIdAndInfoEmployeeSchoolSchoolIdAndDelActiveTrue(Long id, Long idSchool);

    List<FnEmployeeSalary> findByIdInAndDelActiveTrue(List<Long> id);

    Optional<FnEmployeeSalary> findByFnEmployeeSalaryDefaultIdAndYearAndMonthAndDelActiveTrue(Long id, int year, int month);

    List<FnEmployeeSalary> findByInfoEmployeeSchoolIdAndYearAndMonthAndDelActiveTrue(Long id, int year, int month);

    List<FnEmployeeSalary> findByInfoEmployeeSchoolIdAndYearAndDelActiveTrue(Long id, int year);

    List<FnEmployeeSalary> findByInfoEmployeeSchoolIdAndMonthAndYearAndApprovedTrueAndDelActiveTrue(Long idInfoEmployee, int month, int year);

    List<FnEmployeeSalary> findByInfoEmployeeSchoolIdAndMonthAndYearAndCategoryAndApprovedTrueAndDelActiveTrue(Long idInfoEmployee, int month, int year, String category);

    List<FnEmployeeSalary> findByIdInAndInfoEmployeeSchool_SchoolId(List<Long> idList, Long idSchool);

    List<FnEmployeeSalary> findByIdInAndInfoEmployeeSchoolSchoolIdAndApprovedTrueAndDelActiveTrueOrderByCategoryDesc(List<Long> idList, Long idSchool);

    List<FnEmployeeSalary> findByIdInAndInfoEmployeeSchoolSchoolIdAndDelActiveTrue(List<Long> idList, Long idSchool);

}
