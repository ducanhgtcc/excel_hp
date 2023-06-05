package com.example.onekids_project.repository;

import com.example.onekids_project.entity.finance.feesextend.FnMoneyKidsExtend;
import com.example.onekids_project.repository.repositorycustom.FnMoneyKidsExtendRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * date 2021-10-07 16:35
 *
 * @author lavanviet
 */
public interface FnMoneyKidsExtendRepository extends JpaRepository<FnMoneyKidsExtend, Long>, FnMoneyKidsExtendRepositoryCustom {
}
