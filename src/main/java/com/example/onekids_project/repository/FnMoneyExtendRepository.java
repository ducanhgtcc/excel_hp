package com.example.onekids_project.repository;

import com.example.onekids_project.entity.finance.feesextend.FnMoneyExtend;
import com.example.onekids_project.repository.repositorycustom.FnMoneyExtendRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * date 2021-10-01 16:07
 *
 * @author lavanviet
 */
public interface FnMoneyExtendRepository extends JpaRepository<FnMoneyExtend, Long>, FnMoneyExtendRepositoryCustom {
}
