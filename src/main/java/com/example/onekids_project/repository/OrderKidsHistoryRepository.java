package com.example.onekids_project.repository;

import com.example.onekids_project.entity.finance.fees.OrderKidsHistory;
import com.example.onekids_project.repository.repositorycustom.OrderKidsHistoryRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * date 2021-03-02 15:06
 *
 * @author lavanviet
 */
public interface OrderKidsHistoryRepository extends JpaRepository<OrderKidsHistory, Long>, OrderKidsHistoryRepositoryCustom {
    List<OrderKidsHistory> findByFnOrderKidsIdOrderByIdDesc(Long idOrder);
}
