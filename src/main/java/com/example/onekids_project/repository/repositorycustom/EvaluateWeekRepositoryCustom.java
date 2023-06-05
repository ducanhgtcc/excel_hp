package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.kids.EvaluateWeek;
import com.example.onekids_project.mobile.plus.request.KidsPageNumberPlusRequest;
import com.example.onekids_project.mobile.teacher.request.evaluate.KidsPageNumberRequest;
import com.example.onekids_project.request.evaluatekids.EvaluateDateSearchRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface EvaluateWeekRepositoryCustom {
    /**
     * tìm kiếm đánh giá tuần của các học sinh
     *
     * @param idSchool
     * @param evaluateDateSearchRequest
     * @return
     */
    List<EvaluateWeek> searchEvaluateWeek(Long idSchool, EvaluateDateSearchRequest evaluateDateSearchRequest);

    /**
     * tìm kiếm đánh giá các tuần cho một học sinh
     *
     * @param idSchool
     * @param idKid
     * @return
     */
    List<EvaluateWeek> findEvaluateWeekMobile(Long idSchool, Long idKid);

    /**
     * @param idSchool
     * @param idKid
     * @return
     */
    List<EvaluateWeek> findParentUnreadOfYear(Long idSchool, Long idKid);

    /**
     * get parent for week
     *
     * @param idSchool
     * @param idKid
     * @param id
     * @return
     */
    List<EvaluateWeek> findParentdforKid(Long idSchool, Long idKid, Long id, Pageable pageable);

    /**
     * @param idSchool
     * @param idKid
     * @param id
     * @return
     */
    long countParentUnread(Long idSchool, Long idKid, Long id);

    int countNewParentReply(Long idClass);

    List<EvaluateWeek> findEvaluateWeekOfMonthKid(Long idKid, Long idClass, List<Long> idClassList, LocalDate startDate, LocalDate endDate);

    int countSchoolUnreadOfMonth(Long idKid, LocalDate startDate, LocalDate endDate, Long idClass);

    int countSchoolUnreadTeacher(Long idSchool, Long idKid, Long idClass, List<Long> idClassList, LocalDate localDate);

    List<EvaluateWeek> findEvaluateWeekKidAndPaging(Long idSchool, Long idClass, List<Long> idClassList, boolean historyView, KidsPageNumberRequest request);

    List<EvaluateWeek> findEvaluateWeekKidAndPaging(Long idSchool, Long idClass, List<Long> idClassList, boolean historyView, KidsPageNumberPlusRequest request);

    List<EvaluateWeek> findByEvaluateWeekWidthClass(Long idClass, LocalDate date);

    List<EvaluateWeek> findEvaluateWeekClassWidthDate(Long idClass, LocalDate date);

    List<EvaluateWeek> findEvaluateWeekOfMonth(Long idClass, LocalDate startDate, LocalDate endDate);

    /**
     * tìm kiếm nhận xét tuần trong khoảng tuần
     * @param idSchool
     * @return
     */
    List<EvaluateWeek> searchEvaluateWeekChart(Long idSchool, Long idGrade, Long idClass, LocalDate date);
}
