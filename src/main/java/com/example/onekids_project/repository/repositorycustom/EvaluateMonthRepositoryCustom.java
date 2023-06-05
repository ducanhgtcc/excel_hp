package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.kids.EvaluateMonth;
import com.example.onekids_project.mobile.plus.request.KidsPageNumberPlusRequest;
import com.example.onekids_project.mobile.teacher.request.evaluate.KidsPageNumberRequest;
import com.example.onekids_project.request.evaluatekids.EvaluateDateSearchRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface EvaluateMonthRepositoryCustom {

    /**
     * tìm kiếm đánh giá tháng cho các học sinh
     *
     * @param idSchool
     * @param evaluateDateSearchRequest
     * @param month
     * @param year
     * @return
     */
    List<EvaluateMonth> searchEvaluateMonth(Long idSchool, EvaluateDateSearchRequest evaluateDateSearchRequest, int month, int year);

    /**
     * tìm kiếm đánh giá tất cả các ngày cho một học sinh
     *
     * @param idSchool
     * @param idKid
     * @return
     */
    List<EvaluateMonth> findEvaluateMonthMobile(Long idSchool, Long idKid);

    /**
     * get parent unread
     *
     * @param idSchool
     * @param idKid
     * @return
     */
    List<EvaluateMonth> findParentUnreadOfYear(Long idSchool, Long idKid);

    /**
     * get parent for week
     *
     * @param idSchool
     * @param idKid
     * @param id
     * @return
     */
    List<EvaluateMonth> findParentdforKid(Long idSchool, Long idKid, Long id, Pageable pageable);

    /**
     * parent unread
     *
     * @param idSchool
     * @param idKid
     * @param id
     * @return
     */
    long countParentUnread(Long idSchool, Long idKid, Long id);

    int countNewParentReply(Long idClass);

    List<EvaluateMonth> findEvaluateMonthOfMontKid(Long idKid, Long idClass, List<Long> idClassList, LocalDate date);

    int countSchoolUnreadOfMonth(Long idKid, LocalDate data, Long idClass);

    int countSchoolUnreadTeacher(Long idKid, Long idClass, List<Long> idClassList, LocalDate localDate);

    List<EvaluateMonth> findEvaluateMonthKidAndPaging(Long idSchool, Long idClass, List<Long> idClassList, boolean historyView, KidsPageNumberRequest request);

    List<EvaluateMonth> findEvaluateMonthKidAndPaging(Long idSchool, Long idClass, List<Long> idClassList, boolean historyView, KidsPageNumberPlusRequest request);

    List<EvaluateMonth> findByEvaluateMonthWithClass(Long idClass, int month, int year);

    List<EvaluateMonth> findEvaluateMonthClassWidthDate(Long idClass, LocalDate date);

    /**
     * Tìm kiếm list tháng
     * @param idSchool
     * @return
     */
    List<EvaluateMonth> searchEvaluateKidsMonthChart(Long idSchool, Long idGrade, Long idClass, LocalDate date);
}
