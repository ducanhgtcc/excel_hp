package com.example.onekids_project.repository;

import com.example.onekids_project.entity.kids.EvaluateWeek;
import com.example.onekids_project.repository.repositorycustom.EvaluateWeekRepositoryCustom;
import com.example.onekids_project.response.changeQuery.chart.EvaluateKidsWeekMonthQueryResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EvaluateWeekRepository extends JpaRepository<EvaluateWeek, Long>, EvaluateWeekRepositoryCustom {
    /**
     * tìm kiếm đánh giá tuần theo id
     * @param id
     * @param idSchool
     * @param delActive
     * @return
     */
    Optional<EvaluateWeek> findEvaluateWeekByIdAndIdSchoolAndDelActive(Long id, Long idSchool, boolean delActive);
    /**
     * tìm kiếm đánh giá tuần theo id
     * @param id
     * @param idSchool
     * @return
     */
    Optional<EvaluateWeek> findEvaluateWeekByIdAndIdSchoolAndDelActiveTrue(Long id, Long idSchool);

    /**
     * tìm kiếm đánh một tuần giá tuần cho một học sinh
     * @param idKid
     * @param date
     * @param idSchool
     * @param delActive
     * @return
     */
    Optional<EvaluateWeek> findEvaluateWeekByKidsIdAndDateAndIdSchoolAndDelActive(Long idKid, LocalDate date, Long idSchool, boolean delActive);

    int countByKidsIdAndDate(Long idKid, LocalDate date);

    int countByKidsIdAndIdClassAndDate(Long idKid, Long idClass, LocalDate date);

    List<EvaluateWeek> findByKidsIdAndIdClassAndDate(Long idKid, Long idClass, LocalDate date);

    List<EvaluateWeek> findByKidsIdAndDate(Long idKid, LocalDate date);

    List<EvaluateWeek> findByIdClassAndDate(Long idClass, LocalDate date);

    List<EvaluateWeek> findByDate(LocalDate date);

    @Query("select new com.example.onekids_project.response.changeQuery.chart.EvaluateKidsWeekMonthQueryResponse(e.content, e.kids) " +
            "from EvaluateWeek e " +
            "where e.idSchool = :idSchool " +
            "and (:idGrade is null or e.idGrade = :idGrade) " +
            "and (:idClass is null or e.idClass = :idClass) " +
            "and (:date is null or e.date = :date) " +
            "and e.delActive = true ")
    List<EvaluateKidsWeekMonthQueryResponse> getEvaluateKidsWeekChart(@Param("idSchool") Long idSchool,
                                                                      @Param("idGrade") Long idGrade,
                                                                      @Param("idClass") Long idClass,
                                                                      @Param("date") LocalDate date);
}
