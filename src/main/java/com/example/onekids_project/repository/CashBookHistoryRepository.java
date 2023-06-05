package com.example.onekids_project.repository;

import com.example.onekids_project.entity.school.CashBookHistory;
import com.example.onekids_project.repository.repositorycustom.CashBookHistoryRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * date 2021-03-01 10:28
 *
 * @author lavanviet
 */
public interface CashBookHistoryRepository extends JpaRepository<CashBookHistory, Long>, CashBookHistoryRepositoryCustom {
}
