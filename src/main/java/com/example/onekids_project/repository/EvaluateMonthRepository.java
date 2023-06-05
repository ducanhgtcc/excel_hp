package com.example.onekids_project.repository;

import com.example.onekids_project.entity.kids.EvaluateMonth;
import com.example.onekids_project.repository.repositorycustom.EvaluateMonthRepositoryCustom;
import com.example.onekids_project.response.changeQuery.chart.EvaluateKidsWeekMonthQueryResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EvaluateMonthRepository extends JpaRepository<EvaluateMonth, Long>, EvaluateMonthRepositoryCustom {
    @Query(value = "select distinct id_kids from ma_evaluate_month where id_class=:idClass and month=:month and year=:year", nativeQuery = true)
    List<Long> findIdKidOfMonthList(Long idClass, int month, int year);

    /**
     * tìm kiếm đánh giá tháng theo id
     *
     * @param id
     * @param idSchool
     * @param delActive
     * @return
     */
    Optional<EvaluateMonth> findEvaluateMonthByIdAndIdSchoolAndDelActive(Long id, Long idSchool, boolean delActive);

    /**
     * tìm kiếm đánh giá tháng theo id
     *
     * @param id
     * @param idSchool
     * @return
     */
    Optional<EvaluateMonth> findEvaluateMonthByIdAndIdSchoolAndDelActiveTrue(Long id, Long idSchool);

    /**
     * tìm kiếm đánh giá một tháng cho một học sinh
     *
     * @param idKid
     * @param idSchool
     * @param month
     * @param year
     * @param delActive
     * @return
     */
    Optional<EvaluateMonth> findEvaluateMonthByKidsIdAndIdSchoolAndMonthAndYearAndDelActive(Long idKid, Long idSchool, int month, int year, boolean delActive);


    List<EvaluateMonth> findByIdClassAndMonthAndYear(Long idClass, int month, int year);

    int countByKidsIdAndMonthAndYear(Long idKid, int month, int year);

    List<EvaluateMonth> findByKidsIdAndIdClassAndYearAndMonth(Long idKid, Long idClass, int year, int month);

    List<EvaluateMonth> findByKidsIdAndYearAndMonth(Long idKid, int year, int month);

    List<EvaluateMonth> findByMonthAndYear(int month, int year);

    @Query("select new com.example.onekids_project.response.changeQuery.chart.EvaluateKidsWeekMonthQueryResponse(e.content, e.kids) " +
            "from EvaluateMonth e " +
            "where e.idSchool = :idSchool " +
            "and (:idGrade is null or e.idGrade = :idGrade) " +
            "and (:idClass is null or e.idClass = :idClass) " +
            "and (:month is null or (e.month = :month)) " +
            "and (:year is null or (e.year = :year)) " +
            "and e.delActive = true ")
    List<EvaluateKidsWeekMonthQueryResponse> getEvaluateKidsMonthChart(@Param("idSchool") Long idSchool,
                                                                      @Param("idGrade") Long idGrade,
                                                                      @Param("idClass") Long idClass,
                                                                      @Param("month") int month,
                                                                      @Param("year") int year);

}
