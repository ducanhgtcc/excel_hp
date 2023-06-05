package com.example.onekids_project.repository;

import com.example.onekids_project.common.KidsStatusConstant;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.repository.repositorycustom.KidsRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface KidsRepository extends JpaRepository<Kids, Long>, KidsRepositoryCustom {

    @Query("select m.kidStatus from Kids m where m.idSchool=:idSchool and m.delActive = true")
    List<String> countWithStatus(@Param("idSchool") Long idSchool);

    Optional<Kids> findByIdAndDelActive(Long id, boolean delActive);

    Optional<Kids> findByIdAndDelActiveTrue(Long id);

    Optional<Kids> findByIdAndIdSchool(Long id, Long idSchool);

    Optional<Kids> findByKidCodeAndIdSchoolAndDelActiveTrue(String kidCode, Long idSchool);

    Optional<Kids> findByIdAndIdSchoolAndDelActive(Long idSchool, Long id, boolean delActive);

//    Optional<Kids> findByIdAndIdSchool(Long idSchool, Long id);

    Optional<Kids> findByIdAndIdSchoolAndDelActiveTrue(Long id, Long idSchool);

    Optional<Kids> findByIdAndIdSchoolAndDelActiveFalse(Long id, Long idSchool);

    List<Kids> findByIdSchoolAndDelActiveTrueAndKidStatus(Long idSchool, String kidStatus);
    long countByIdSchoolAndDelActiveTrueAndKidStatus(Long idSchool, String kidStatus);
    List<Kids> findByIdSchoolAndDelActiveTrueAndKidStatusAndIsActivatedTrue(Long idSchool, String kidStatus);

    List<Kids> findByIdSchoolAndDelActiveTrueAndKidStatusAndParentNotNull(Long idSchool, String kidStatus);

    List<Kids> findByIdNotInAndKidStatusAndDelActiveTrue(List<Long> idKidList, String kidStatus);

    List<Kids> findByKidStatusAndDelActiveTrue(String kidStatus);

    List<Kids> findByIdSchoolAndKidStatusNotContainingAndDelActiveTrue(Long idSchool, String kidStatus);

    List<Kids> findByIdSchoolAndMaClassIdAndDelActiveTrue(Long idSchool, Long idClass);

    List<Kids> findByIdSchoolAndMaClassIdAndKidStatusAndDelActiveTrue(Long idSchool, Long idClass, String status);

    List<Kids> findByIdSchoolAndDelActiveTrue(Long idSchool);

    List<Kids> findByIdInAndIdSchoolAndDelActiveTrue(List<Long> idList, Long idSchool);

    List<Kids> findByIdSchoolAndMaClassIdInAndKidStatusAndDelActiveTrue(Long idSchool, List<Long> idList, String status);

    List<Kids> findByIdSchool(Long idSchool);

    List<Kids> findByMaClassIdAndKidStatusAndDelActiveTrue(Long idClass, String status);

    List<Kids> findByIdSchoolAndIdGradeAndDelActiveTrue(Long idSchool, Long idGrade);

    List<Kids> findByDelActiveTrueAndIdSchoolAndIdGradeAndMaClass_Id(Long idSchool, Long idGrade, Long idClass);

    List<Kids> findByDelActiveTrueAndIdSchool(Long idSchool);

    List<Kids> findByDelActiveTrueAndMaClass_Id(Long idClass);

    List<Kids> findByMaClassIdAndParentIsNotNullAndDelActiveTrue(Long idClass);

    List<Kids> findByIdSchoolAndParentIsNotNullAndDelActiveTrue(Long idSchool);

    List<Kids> findByDelActiveTrueAndMaClass_IdAndKidStatus(Long idClass, String kidStatus);

    int countByIdSchoolAndBirthDayAndKidStatusAndDelActiveTrue(Long idSchool, LocalDate date, String kidStatus);

    List<Kids> findByMaClassIdAndKidStatusNotContainingAndDelActiveTrue(Long idClass, String status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Kids> findByIdSchoolAndIdInAndDelActiveTrue(Long idSchool, List<Long> idKidList);

    List<Kids> findByIdInAndDelActiveTrue(List<Long> idKidList);

    List<Kids> findByIdInAndParentIsNotNull(List<Long> idKidList);

    List<Kids> findByIdSchoolAndFullNameContaining(Long idSchool, String fullName);

    List<Kids> findByKidStatusAndParentIsNotNullAndDelActiveTrue(String status);

    @Query(value = "select id from ma_kids where id_school=:idSchool and kid_status='" + KidsStatusConstant.STUDYING + "'", nativeQuery = true)
    List<Long> findIdKidsList(@Param("idSchool") Long idSchool);

    @Query(value = "select full_name from ma_kids", nativeQuery = true)
    List<String> findIdKidsList1();

    @Query(value = "select k.id, k.id_class as idClass, u.id as idUser from ma_kids k join ma_parent p on k.id_parent=p.id join ma_user u on p.id_ma_user=u.id where k.id_school=:idSchool and k.id_parent is not null and k.id_grade in :idGradeList and k.is_activated=true and k.del_active=true and k.kid_status='" + KidsStatusConstant.STUDYING + "'", nativeQuery = true)
    <T> Collection<T> getKidsGradeNotify(@Param("idSchool") Long idSchool, @Param("idGradeList") List<Long> idGradeList, Class<T> type);

    @Query(value = "select k.id, k.id_class as idClass, u.id as idUser from ma_kids k join ma_parent p on k.id_parent=p.id join ma_user u on p.id_ma_user=u.id where k.id_school=:idSchool and k.id_parent is not null and k.id_class in :idClassList and k.is_activated=true and k.del_active=true and k.kid_status='" + KidsStatusConstant.STUDYING + "'", nativeQuery = true)
    <T> Collection<T> getKidsClassNotify(@Param("idSchool") Long idSchool, @Param("idClassList") List<Long> idClassList, Class<T> type);

    int countByIdSchoolAndKidStatusAndDelActiveTrue(Long idSchool, String status);

}
