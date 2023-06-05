package com.example.onekids_project.repository;

import com.example.onekids_project.entity.finance.employeesalary.FnSalary;
import com.example.onekids_project.repository.repositorycustom.FnSalaryRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FnSalaryRepository extends JpaRepository<FnSalary, Long>, FnSalaryRepositoryCustom {
    List<FnSalary> findBySchoolIdAndDelActiveTrue(Long idSchool);

    List<FnSalary> findBySchoolIdAndDelActiveTrueOrderByCategoryDesc(Long idSchool);

    Optional<FnSalary> findByIdAndDelActiveTrue(Long id);

    Optional<FnSalary> findByIdAndSchoolIdAndDelActiveTrue(Long id, Long idSchool);

    List<FnSalary> findByIdInAndSchoolIdAndDelActiveTrue(List<Long> idList, Long idSchool);

}
