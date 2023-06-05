package com.example.onekids_project.repository;

import com.example.onekids_project.entity.finance.fees.ExOrderHistoryKidsPackage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * date 2021-03-02 16:28
 *
 * @author lavanviet
 */
public interface ExOrderHistoryKidsPackageRepository extends JpaRepository<ExOrderHistoryKidsPackage, Long> {
    List<ExOrderHistoryKidsPackage> findByOrderKidsHistoryId(Long idOrderHistory);

    List<ExOrderHistoryKidsPackage> findByFnKidsPackageIdOrderByIdDesc(Long idKidsPackage);
}
