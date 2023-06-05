package com.example.onekids_project.repository;

import com.example.onekids_project.entity.finance.feesextend.FnPackageExtend;
import com.example.onekids_project.repository.repositorycustom.FnPackageExtendRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * date 2021-10-01 16:03
 *
 * @author lavanviet
 */
public interface FnPackageExtendRepository extends JpaRepository<FnPackageExtend, Long>, FnPackageExtendRepositoryCustom {
    @Modifying
    @Query(value = "delete from fn_package_extend where id=:id", nativeQuery = true)
    void deletePackageExtendById(Long id);

    Optional<FnPackageExtend> findByIdAndIdSchoolAndDelActiveTrue(Long id, Long idSchool);
}
