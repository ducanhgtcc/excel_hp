package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.kids.EvaluatePeriodic;
import com.example.onekids_project.mobile.plus.request.KidsPageNumberPlusRequest;
import com.example.onekids_project.mobile.teacher.request.evaluate.KidsPageNumberRequest;
import com.example.onekids_project.request.evaluatekids.EvaluatePeriodicSearchRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface EvaluatePeriodicRepositoryCustom {
    /**
     * tìm kiếm đánh giá tất cả các định kỳ cho một học sinh
     *
     * @param idSchool
     * @param idKid
     * @return
     */
    List<EvaluatePeriodic> findEvaluatePeriodicMobile(Long idSchool, Long idKid);

    /**
     * search last
     *
     * @param idSchool
     * @param evaluatePeriodicSearchRequest
     * @return
     */
    List<EvaluatePeriodic> searchEvaluatePeriodicLast(Long idSchool, EvaluatePeriodicSearchRequest evaluatePeriodicSearchRequest);

    /**
     * @param idSchool
     * @param idKid
     * @return
     */
    List<EvaluatePeriodic> findParentUnreadOfYear(Long idSchool, Long idKid);

    /**
     * @param idSchool
     * @param idKid
     * @param id
     * @param pageable
     * @return
     */
    List<EvaluatePeriodic> findParentdforKid(Long idSchool, Long idKid, Long id, Pageable pageable);

    /**
     * @param idSchool
     * @param idKid
     * @param id
     * @return
     */
    long countParentUnread(Long idSchool, Long idKid, Long id);

    int countNewParentReply(Long idClass);

    List<EvaluatePeriodic> findEvaluatePeriodicOfMontKid(Long idKid, Long idClass, List<Long> idClassList, LocalDate startDate, LocalDate endDate);

    int countSchoolUnreadOfMonth(Long idKid, LocalDate startDate, LocalDate endDate, Long idClass);

    int countSchoolUnreadTeacher(Long idKid, Long idClass, LocalDate localDate);

    List<EvaluatePeriodic> findEvaluatePeriodicKidAndPaging(Long idSchool, Long idClass, List<Long> idClassList, boolean historyView, KidsPageNumberRequest request);

    List<EvaluatePeriodic> findEvaluatePeriodicKidAndPaging(Long idSchool, Long idClass, List<Long> idClassList, boolean historyView, KidsPageNumberPlusRequest request);

    List<EvaluatePeriodic> findByEvaluatePeriodicWithClass(Long idClass, LocalDate date);

    List<EvaluatePeriodic> findClassDateHas(Long idClass, LocalDate date);

    List<EvaluatePeriodic> findEvaluatePeriodicOfMonth(LocalDate startDate, LocalDate endDate, Long idClass);
}
