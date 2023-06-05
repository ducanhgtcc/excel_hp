package com.example.onekids_project.repository;

import com.example.onekids_project.entity.finance.fees.FnKidsPackage;
import com.example.onekids_project.repository.repositorycustom.FnKidsPackageRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FnKidsPackageRepository extends JpaRepository<FnKidsPackage, Long>, FnKidsPackageRepositoryCustom {
    Optional<FnKidsPackage> findByKidsIdAndFnPackageIdAndMonthAndYearAndDelActiveTrue(Long idKid, Long idPackage, int month, int year);

    List<FnKidsPackage> findByKidsIdAndMonthAndYearAndApprovedTrueAndDelActiveTrue(Long idKid, int month, int year);

    List<FnKidsPackage> findByKidsIdAndFnPackageIdAndDelActiveTrue(Long idKid, Long idPackage);

    Optional<FnKidsPackage> findByIdAndDelActiveTrue(Long id);
    Optional<FnKidsPackage> findByIdAndApprovedTrueAndDelActiveTrue(Long id);

    List<FnKidsPackage> findByKidsIdAndFnPackageCategoryAndMonthAndYearAndApprovedTrueAndDelActiveTrue(Long idKid, String category, int month, int year);

    List<FnKidsPackage> findByIdInAndKidsIdSchoolAndDelActiveTrue(List<Long> idList, Long idSchool);

    List<FnKidsPackage> findByIdInAndKidsIdSchoolAndApprovedTrueAndDelActiveTrue(List<Long> idList, Long idSchool);

    List<FnKidsPackage> findByYearAndMonthAndFnPackageIdAndDelActiveTrue(int year, int month, Long idPackage);

    List<FnKidsPackage> findByYearAndMonthAndKidsIdInAndFnPackageIdInAndApprovedFalseAndLockedFalseAndDelActiveTrue(int year, int month, List<Long> idKidList, List<Long> idPackageList);

}
