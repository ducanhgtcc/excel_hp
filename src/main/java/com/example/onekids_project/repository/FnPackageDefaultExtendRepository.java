package com.example.onekids_project.repository;

import com.example.onekids_project.entity.finance.feesextend.FnPackageDefaultExtend;
import com.example.onekids_project.repository.repositorycustom.FnPackageDefaultExtendRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * date 2021-10-06 11:07
 *
 * @author lavanviet
 */
public interface FnPackageDefaultExtendRepository extends JpaRepository<FnPackageDefaultExtend, Long>, FnPackageDefaultExtendRepositoryCustom {
    Optional<FnPackageDefaultExtend> findByIdAndIdSchool(Long id, Long idSchool);
}
