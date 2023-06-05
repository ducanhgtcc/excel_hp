package com.example.onekids_project.repository;

import com.example.onekids_project.entity.parent.WalletParent;
import com.example.onekids_project.repository.repositorycustom.WalletParentRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * date 2021-02-22 10:08
 *
 * @author lavanviet
 */
public interface WalletParentRepository extends JpaRepository<WalletParent, Long>, WalletParentRepositoryCustom {
    Optional<WalletParent> findByCode(String code);

    List<WalletParent> findByIdSchool(Long idSchool);

}
