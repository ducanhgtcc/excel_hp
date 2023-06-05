package com.example.onekids_project.repository;

import com.example.onekids_project.entity.parent.WalletParentHistory;
import com.example.onekids_project.repository.repositorycustom.WalletParentHistoryRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * date 2021-02-25 08:56
 *
 * @author lavanviet
 */
public interface WalletParentHistoryRepository extends JpaRepository<WalletParentHistory, Long>, WalletParentHistoryRepositoryCustom {
    int countByWalletParentIdAndTypeAndConfirmFalse(Long idWallet, String type);
    List<WalletParentHistory> findByWalletParentIdOrderByDateDesc(Long idWallet);
    int countByWalletParentIdSchoolAndConfirmFalse(Long idSchool);


}
