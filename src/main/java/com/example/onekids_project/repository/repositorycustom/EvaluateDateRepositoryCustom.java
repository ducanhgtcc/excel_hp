package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.kids.EvaluateDate;
import com.example.onekids_project.mobile.response.StartEndDateObject;
import com.example.onekids_project.request.evaluatekids.EvaluateDateSearchRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EvaluateDateRepositoryCustom {

    /**
     * tìm kiếm đánh giá cho các học sinh trong một ngày
     *
     * @param idSchool
     * @param evaluateDateSearchRequest
     * @return
     */
    List<EvaluateDate> searchEvaluateKidsDate(Long idSchool, EvaluateDateSearchRequest evaluateDateSearchRequest);

    /**
     * tìm kiếm  nhận xét tất cả học sinh 1 lớp trong một tháng
     *
     * @param idSchool
     * @param idClass
     * @return
     */

    List<EvaluateDate> searchEvaluateKidsDateOfMonth(Long idSchool, Long idClass, LocalDate dateStart, LocalDate dateEnd);

    /**
     * tìm kiếm đánh giá tất cả các ngày cho một học sinh
     *
     * @param idSchool
     * @param idKid
     * @return
     */
    List<EvaluateDate> findEvaluateDateKidMobile(Long idSchool, Long idKid);

    /**
     * @param idSchool
     * @param idKid
     * @param localDate
     * @return
     */
    Optional<EvaluateDate> findEvaluateDateKidDateMobile(Long idSchool, Long idKid, LocalDate localDate);

    Optional<EvaluateDate> findEvaluateDateKidHas(Long idSchool, Long idKid);

    /**
     * total of month
     *
     * @param idSchool
     * @param idKid
     * @return
     */
    List<EvaluateDate> totalEvaluateDateMonthMobile(Long idSchool, Long idKid, Integer month, Integer year);

    int countNewParentReply(Long idClass);

    /**
     * lấy danh sách nhận xét học sinh trong một tháng
     *
     * @param idClass
     * @param startDate
     * @param endDate
     * @return
     */
    List<EvaluateDate> findEvaluateDateOfMonthTeacher(Long idClass, LocalDate startDate, LocalDate endDate);

    List<EvaluateDate> findEvaluateDateOfMonthKid(Long idKid, Long idClass,  List<Long> idClassList, LocalDate startDate, LocalDate endDate);

    int countSchoolUnreadOfMonth(Long idKid, LocalDate startDate, LocalDate endDate, Long idClass);

    int countSchoolUnreadOfMonthTeacher(Long idKid, Long idClass, LocalDate startDate, LocalDate endDate, LocalDate localDate);

    /**
     * find kids and date
     *
     * @param idClass
     * @param date
     * @return
     */
    List<EvaluateDate> findEvaluateClassDate(Long idClass, LocalDate date);

    /**
     * Lấy tổng số ngày chưa đọc trong 1 tháng
     *
     * @param idClass
     * @param idKid
     * @param startEndDateObject
     * @return
     */
    int countSchoolUnreadKidOfMonth(Long idClass, Long idKid, StartEndDateObject startEndDateObject);

    /**
     * Lấy danh sách ngày có nhận xét trong tháng
     *
     * @param idClass
     * @param idKid
     * @param startEndDateObject
     * @return
     */
    List<EvaluateDate> findKidDateHaveOfMonth(Long idClass, Long idKid, StartEndDateObject startEndDateObject);

    List<EvaluateDate> findKidDateHaveOfReplyMonth(Long idClass, Long idKid, StartEndDateObject startEndDateObject);

    /**
     * lấy dữ liệu ngày
     * @param idSchool
     * @param idClass
     * @param historyView
     * @param idClassList
     * @param idKid
     * @param localDate
     * @return
     */
    Optional<EvaluateDate> findEvaluateDateOfDate(Long idSchool, Long idClass, boolean historyView,  List<Long> idClassList, Long idKid, LocalDate localDate);

    Optional<EvaluateDate> findEvaluateDateHas(Long idClass, List<Long> idClassList, Long idKid);

    List<EvaluateDate> findEvaluateDateTeacher(Long idClass, LocalDate date);

    List<EvaluateDate> findClassDateHas(Long idClass, LocalDate date);

    /**
     * Tìm kiếm nhận xét cho học sinh trong khoảng ngày
     * @param idSchool
     * @return
     */
    List<EvaluateDate> searchEvaluateKidsDateChart(Long idSchool, Long idGrade, Long idClass, LocalDate date);
}
