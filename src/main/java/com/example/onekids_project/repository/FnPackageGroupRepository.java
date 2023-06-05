package com.example.onekids_project.repository;

import com.example.onekids_project.entity.finance.fees.FnPackageGroup;
import com.example.onekids_project.repository.repositorycustom.FnPackageGroupRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * date 2021-06-01 14:06
 *
 * @author lavanviet
 */
public interface FnPackageGroupRepository extends JpaRepository<FnPackageGroup, Long>, FnPackageGroupRepositoryCustom {
    List<FnPackageGroup> findByIdSchoolAndNameContainingAndDelActiveTrueOrderByIdDesc(Long idSchool, String name);

    Optional<FnPackageGroup> findByIdAndIdSchoolAndDelActiveTrue(Long id, Long idSchool);
    List<FnPackageGroup> findByIdInAndIdSchoolAndDelActiveTrue(List<Long> idList, Long idSchool);
}
