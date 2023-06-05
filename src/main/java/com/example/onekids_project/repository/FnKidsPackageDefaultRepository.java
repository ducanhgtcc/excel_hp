package com.example.onekids_project.repository;

import com.example.onekids_project.entity.finance.fees.FnKidsPackageDefault;
import com.example.onekids_project.repository.repositorycustom.FnKidsPackageDefaultRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FnKidsPackageDefaultRepository extends JpaRepository<FnKidsPackageDefault, Long>, FnKidsPackageDefaultRepositoryCustom {

    @Modifying
    @Query(value = "delete model from fn_kids_package_default as model where id_class=:idClass and id_package=:idPackage", nativeQuery = true)
    void deletePackageInClass(Long idClass, Long idPackage);

    void deleteByKidsId(Long idKid);

    Optional<FnKidsPackageDefault> findByKidsIdAndFnPackageId(Long idKid, Long idPackage);

    List<FnKidsPackageDefault> findByKidsId(Long idKid);

    List<FnKidsPackageDefault> findByIdClass(Long idClass);

    List<FnKidsPackageDefault> findByIdInAndFnPackageIdSchool(List<Long> idList, Long idSchool);

    List<FnKidsPackageDefault> findByIdClassAndDelActiveTrue(Long idClass);

}
