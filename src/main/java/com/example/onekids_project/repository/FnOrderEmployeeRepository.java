package com.example.onekids_project.repository;

import com.example.onekids_project.entity.finance.employeesalary.FnOrderEmployee;
import com.example.onekids_project.repository.repositorycustom.FnOrderEmployeeRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * date 2021-02-24 9:51 SA
 *
 * @author ADMIN
 */
public interface FnOrderEmployeeRepository extends JpaRepository<FnOrderEmployee, Long>, FnOrderEmployeeRepositoryCustom {
    Optional<FnOrderEmployee> findByInfoEmployeeSchoolIdAndMonthAndYear(Long id, int month, int year);

    Optional<FnOrderEmployee> findFirstByOrderByIdDesc();

    int countByInfoEmployeeSchoolIdAndMonthIn(Long idInfoEmployee, List<Integer> monthList);

    List<FnOrderEmployee> findByInfoEmployeeSchoolIdAndInfoEmployeeSchoolSchoolIdAndYearOrderByMonthDesc(Long idInfoEmployee, Long idSchool, int year);
    List<FnOrderEmployee> findByInfoEmployeeSchoolIdAndYear(Long idInfoEmployee, int year);

    List<FnOrderEmployee> findByIdInAndInfoEmployeeSchoolSchoolId(List<Long> idList, Long idSchool);

    List<FnOrderEmployee> findByYearAndMonthAndInfoEmployeeSchoolSchoolIdAndDelActiveTrue(int year, int month, Long idSchool);

}
