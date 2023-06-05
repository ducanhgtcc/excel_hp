package com.example.onekids_project.repository;

import com.example.onekids_project.entity.kids.EvaluateDate;
import com.example.onekids_project.entity.kids.EvaluatePeriodic;
import com.example.onekids_project.repository.repositorycustom.EvaluatePeriodicRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EvaluatePeriodicRepository extends JpaRepository<EvaluatePeriodic, Long>, EvaluatePeriodicRepositoryCustom {

    /**
     * tìm kiếm đánh giá định kỳ cho một học sinh theo id
     * @param id
     * @param idSchool
     * @param delActive
     * @return
     */
    Optional<EvaluatePeriodic> findEvaluatePeriodicByIdAndIdSchoolAndDelActive(Long id, Long idSchool, boolean delActive);

    /**
     * tìm kiếm đánh giá định kỳ cho một học sinh theo id
     * @param id
     * @param idSchool
     * @return
     */
    Optional<EvaluatePeriodic> findEvaluatePeriodicByIdAndIdSchoolAndDelActiveTrue(Long id, Long idSchool);

    /**
     * find by id
     * @param id
     * @param idSchool
     * @return
     */
    Optional<EvaluatePeriodic> findByIdAndIdSchoolAndDelActiveTrue(Long id, Long idSchool);

    /**
     * search kids
     * @return
     */
    List<EvaluatePeriodic> findByIdSchoolAndKidsIdAndDelActiveTrue(Long idSchool, Long idKid);

    Optional<EvaluatePeriodic> findByDateAndKidsId(LocalDate date, Long idKid);

    List<EvaluatePeriodic> findByIdClassAndDate(Long idClass, LocalDate date);
}
