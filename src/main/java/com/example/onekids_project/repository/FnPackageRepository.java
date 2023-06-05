package com.example.onekids_project.repository;

import com.example.onekids_project.entity.finance.fees.FnPackage;
import com.example.onekids_project.repository.repositorycustom.FnPackageRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FnPackageRepository extends JpaRepository<FnPackage, Long>, FnPackageRepositoryCustom {
    @Modifying
    @Query(value = "insert into ex_class_package(id_class, id_package) value(:idClass, :idPackage)", nativeQuery = true)
    void insertPackageClass(Long idClass, Long idPackage);

    @Modifying
    @Query(value = "delete from ex_class_package where id_class=:idClass and id_package=:idPackage", nativeQuery = true)
    void deletePackageClass(Long idClass, Long idPackage);

    @Query(value = "select exists(select * from ex_class_package where id_class=:idClass and id_package=:idPackage)", nativeQuery = true)
    int checkExistsPackageClass(Long idClass, Long idPackage);

    Optional<FnPackage> findByIdAndDelActiveTrue(Long id);

    Optional<FnPackage> findByIdAndIdSchoolAndDelActiveTrue(Long id, Long idSchool);

    List<FnPackage> findByIdInAndIdSchoolAndDelActiveTrue(List<Long> idList, Long idSchool);

    List<FnPackage> findByIdSchoolAndDelActiveTrueOrderByCategory(Long idSchool);

    List<FnPackage> findByIdSchoolAndRootStatusFalseAndDelActiveTrueOrderByCategoryAscSortNumberAsc(Long idSchool);

    List<FnPackage> findByIdSchoolAndMaClassSetIdAndDelActiveTrueOrderBySortNumber(Long idSchool, Long idClass);

    List<FnPackage> findByIdSchoolAndMaClassSetIdAndDelActiveTrueOrderByCategory(Long idSchool, Long idClass);

    List<FnPackage> findByIdSchoolAndMaClassSetIdAndDelActiveTrue(Long idSchool, Long idClass);

    List<FnPackage> findByMaClassSetIdAndDelActiveTrue(Long idClass);

    List<FnPackage> findByIdSchoolAndRootStatusFalseAndFnPackageExtendIsNullAndDelActiveTrue(Long idSchool);

    List<FnPackage> findByIdSchoolAndRootStatusTrueAndDelActiveTrue(Long idSchool);
    Optional<FnPackage> findByIdSchoolAndRootNumberAndRootStatusTrueAndDelActiveTrue(Long idSchool, int rootNumber);
}
