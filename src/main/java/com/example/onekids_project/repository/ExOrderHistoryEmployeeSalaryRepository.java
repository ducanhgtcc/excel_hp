package com.example.onekids_project.repository;

import com.example.onekids_project.entity.finance.employeesalary.ExOrderHistoryEmployeeSalary;
import com.example.onekids_project.repository.repositorycustom.ExOrderHistoryEmployeeSalaryRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * date 2021-03-03 4:55 CH
 *
 * @author ADMIN
 */
@Repository
public interface ExOrderHistoryEmployeeSalaryRepository extends JpaRepository<ExOrderHistoryEmployeeSalary, Long>, ExOrderHistoryEmployeeSalaryRepositoryCustom {

    List<ExOrderHistoryEmployeeSalary> findByOrderEmployeeHistoryId(Long id);

    List<ExOrderHistoryEmployeeSalary> findByFnEmployeeSalaryIdOrderByIdDesc(Long id);
}
