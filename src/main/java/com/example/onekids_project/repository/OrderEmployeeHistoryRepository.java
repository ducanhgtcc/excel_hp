package com.example.onekids_project.repository;

import com.example.onekids_project.entity.finance.employeesalary.OrderEmployeeHistory;
import com.example.onekids_project.repository.repositorycustom.OrderEmployeeHistoryRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * date 2021-02-24 9:54 SA
 *
 * @author ADMIN
 */
public interface OrderEmployeeHistoryRepository extends JpaRepository<OrderEmployeeHistory, Long>, OrderEmployeeHistoryRepositoryCustom {

    List<OrderEmployeeHistory> findByFnOrderEmployeeIdOrderByIdDesc(Long id);
}
