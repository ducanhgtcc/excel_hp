package com.example.onekids_project.repository;

import com.example.onekids_project.dto.ListIdKidDTO;
import com.example.onekids_project.entity.kids.EvaluateDate;
import com.example.onekids_project.repository.repositorycustom.EvaluateDateRepositoryCustom;
import com.example.onekids_project.response.changeQuery.chart.EvaluateKidsDateQueryResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EvaluateDateRepository extends JpaRepository<EvaluateDate, Long>, EvaluateDateRepositoryCustom {

    /**
     * tìm kiếm đánh giá một học sinh theo ngày
     *
     * @param id
     * @param idSchool
     * @param delActive
     * @return
     */
    Optional<EvaluateDate> findByIdAndIdSchoolAndDelActive(Long id, Long idSchool, boolean delActive);

    /**
     * find by id
     *
     * @param id
     * @param idSchool
     * @return
     */
    Optional<EvaluateDate> findByIdAndIdSchoolAndDelActiveTrue(Long id, Long idSchool);

    int countByKidsIdAndDate(Long idKid, LocalDate date);

    @Query(value = "select distinct id_kids from ma_evaluate_date where id_class=:idClass and date>=:startDate and date<:endDate", nativeQuery = true)
    List<Long> findIdKidOfMonthList(Long idClass, LocalDate startDate, LocalDate endDate);

    List<EvaluateDate> findByKidsIdAndDate(Long idKid, LocalDate date);

    Optional<EvaluateDate> findByKidsIdAndDateAndDelActiveTrue(Long idKid, LocalDate date);

    List<EvaluateDate> findByIdClassAndDate(Long idClass, LocalDate date);

    List<EvaluateDate> findByIdGradeAndDateAndIdSchool(Long idGrade, LocalDate date, Long idSchool);

    List<EvaluateDate> findByDate(LocalDate date);

//    List<EvaluateDate> findByIdClass
@Query("select new com.example.onekids_project.dto.ListIdKidDTO(e.kids.id) from EvaluateDate e " +
        "where e.idSchool = :idSchool and e.idClass = :idClass and e.date >= :dateStart and e.date < :dateEnd " +
        "group by e.kids.id")
List<ListIdKidDTO> totalEvaluateKidsDetailOfMonth(@Param("idSchool") Long idSchool,
                                                  @Param("idClass") Long idClass,
                                                  @Param("dateStart") LocalDate dateStart,
                                                  @Param("dateEnd") LocalDate dateEnd);

@Query("select new com.example.onekids_project.response.changeQuery.chart.EvaluateKidsDateQueryResponse(e.learnContent, e.eatContent, e.sleepContent, e.sanitaryContent, e.healtContent, e.commonContent,e.kids) " +
        "from EvaluateDate e " +
        "where e.idSchool = :idSchool " +
        "and (:idGrade is null or e.idGrade = :idGrade) " +
        "and (:idClass is null or e.idClass = :idClass) " +
        "and (:date is null or e.date = :date) " +
        "and e.delActive = true ")
List<EvaluateKidsDateQueryResponse> getEvaluateKidsDateChart(@Param("idSchool") Long idSchool,
                                                                @Param("idGrade") Long idGrade,
                                                                @Param("idClass") Long idClass,
                                                                @Param("date") LocalDate date);

}
