package com.example.onekids_project.repository;

import com.example.onekids_project.entity.finance.feesextend.FnPackageKidsExtend;
import com.example.onekids_project.repository.repositorycustom.FnPackageKidsExtendRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * date 2021-10-07 16:29
 *
 * @author lavanviet
 */
public interface FnPackageKidsExtendRepository extends JpaRepository<FnPackageKidsExtend, Long>, FnPackageKidsExtendRepositoryCustom {
    Optional<FnPackageKidsExtend> findByIdAndIdSchool(Long id, Long idSchool);
}
