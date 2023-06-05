package com.example.onekids_project.repository;

import com.example.onekids_project.entity.finance.feesextend.FnMoneyDefaultExtend;
import com.example.onekids_project.repository.repositorycustom.FnMoneyDefaultExtendRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * date 2021-10-06 11:10
 *
 * @author lavanviet
 */
public interface FnMoneyDefaultExtendRepository extends JpaRepository<FnMoneyDefaultExtend, Long>, FnMoneyDefaultExtendRepositoryCustom {
}
