package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.kids.EvaluateWeek;
import com.example.onekids_project.mobile.plus.request.KidsPageNumberPlusRequest;
import com.example.onekids_project.mobile.teacher.request.evaluate.KidsPageNumberRequest;
import com.example.onekids_project.repository.repositorycustom.EvaluateWeekRepositoryCustom;
import com.example.onekids_project.request.evaluatekids.EvaluateDateSearchRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EvaluateWeekRepositoryImpl extends BaseRepositoryimpl<EvaluateWeek> implements EvaluateWeekRepositoryCustom {
    @Override
    public List<EvaluateWeek> searchEvaluateWeek(Long idSchool, EvaluateDateSearchRequest evaluateDateSearchRequest) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("and date=:date ");
        mapParams.put("date", evaluateDateSearchRequest.getDate());
        queryStr.append("and id_class=:idClass ");
        mapParams.put("idClass", evaluateDateSearchRequest.getIdClass());
        if (evaluateDateSearchRequest.getApproved() != null) {
            queryStr.append("and approved=:approved ");
            mapParams.put("approved", evaluateDateSearchRequest.getApproved());
        }
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<EvaluateWeek> findEvaluateWeekMobile(Long idSchool, Long idKid) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();

        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        if (idKid != null) {
            queryStr.append("and id_kids=:idKid ");
            mapParams.put("idKid", idKid);
        }
        queryStr.append("order by date desc");
        List<EvaluateWeek> evaluateWeekList = findAllNoPaging(queryStr.toString(), mapParams);
        return evaluateWeekList;
    }

    @Override
    public List<EvaluateWeek> findParentUnreadOfYear(Long idSchool, Long idKid) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        if (idKid != null) {
            queryStr.append("and id_kids=:idKid ");
            mapParams.put("idKid", idKid);
        }
        queryStr.append("and approved=true ");
        queryStr.append("and parent_read=false ");
        queryStr.append("and (content!='' or EXISTS (SELECT * FROM evaluate_week_file WHERE model.id = evaluate_week_file.id_evaluate_week)) ");
        List<EvaluateWeek> evaluateWeekList = findAllNoPaging(queryStr.toString(), mapParams);
        return evaluateWeekList;
    }

    @Override
    public List<EvaluateWeek> findParentdforKid(Long idSchool, Long idKid, Long id, Pageable pageable) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        if (idKid != null) {
            queryStr.append("and id_kids=:idKid ");
            mapParams.put("idKid", idKid);
        }
        if (id != null) {
            queryStr.append("and id<:id ");
            mapParams.put("id", id);
        }
        queryStr.append("and approved=true ");
        queryStr.append("and (content!='' or EXISTS (SELECT * FROM evaluate_week_file WHERE model.id = evaluate_week_file.id_evaluate_week)) ");
        queryStr.append("order by date desc, id desc ");
        List<EvaluateWeek> evaluateWeekList = findAllMobile(queryStr.toString(), mapParams, pageable);
        return evaluateWeekList;
    }

    @Override
    public long countParentUnread(Long idSchool, Long idKid, Long id) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        if (idKid != null) {
            queryStr.append("and id_kids=:idKid ");
            mapParams.put("idKid", idKid);
        }
        if (id != null) {
            queryStr.append("and id<:id ");
            mapParams.put("id", id);
        }
        queryStr.append("and approved=true ");
        queryStr.append("and (content!='' or EXISTS (SELECT * FROM evaluate_week_file WHERE model.id = evaluate_week_file.id_evaluate_week)) ");
        return countAll(queryStr.toString(), mapParams);
    }

    @Override
    public int countNewParentReply(Long idClass) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        if (idClass != null) {
            queryStr.append("and id_class=:idClass ");
            mapParams.put("idClass", idClass);
        }
        queryStr.append("and parent_reply_content!='' ");
        queryStr.append("and school_read_reply=false ");
        List<EvaluateWeek> evaluateDateList = findAllNoPaging(queryStr.toString(), mapParams);
        return evaluateDateList.size();
    }

    @Override
    public List<EvaluateWeek> findEvaluateWeekOfMonthKid(Long idKid, Long idClass, List<Long> idClassList, LocalDate startDate, LocalDate endDate) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_kids=:idKid ");
        mapParams.put("idKid", idKid);

        queryStr.append("and id_class in (:idClassList) ");
        mapParams.put("idClassList", idClassList);
        queryStr.append("and case when id_class!=:idClass and approved=false then false else true end ");
        mapParams.put("idClass", idClass);

        if (startDate != null && endDate != null) {
            queryStr.append("and date>=:startDate and date<:endDate ");
            mapParams.put("startDate", startDate.minusDays(6));
            mapParams.put("endDate", endDate);
        }
        queryStr.append("and (content!='' or EXISTS (SELECT * FROM evaluate_week_file WHERE model.id = evaluate_week_file.id_evaluate_week)) ");
        queryStr.append("order by date desc ");
        List<EvaluateWeek> evaluateWeekList = findAllNoPaging(queryStr.toString(), mapParams);
        return evaluateWeekList;
    }

    @Override
    public List<EvaluateWeek> findEvaluateWeekOfMonth(Long idClass, LocalDate startDate, LocalDate endDate) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();

        queryStr.append("and id_class =:idClass ");
        mapParams.put("idClass", idClass);

        if (startDate != null && endDate != null) {
            queryStr.append("and date>=:startDate and date<:endDate ");
            mapParams.put("startDate", startDate);
            mapParams.put("endDate", endDate);
        }
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<EvaluateWeek> searchEvaluateWeekChart(Long idSchool, Long idGrade, Long idClass, LocalDate date) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.getEvaluateKidsWeekChart(queryStr, mapParams, idSchool, idGrade, idClass, date);
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    private void getEvaluateKidsWeekChart(StringBuilder queryStr, Map<String, Object> mapParams, Long idSchool, Long idGrade, Long idClass, LocalDate date){
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        if (idGrade != null){
            queryStr.append("and id_grade=:idGrade ");
            mapParams.put("idGrade", idGrade);
        }
        if (idClass != null){
            queryStr.append("and id_class=:idClass ");
            mapParams.put("idClass", idClass);
        }
        if (date != null){
            queryStr.append("and date =:date ");
            mapParams.put("date", date);
        }
    }

    @Override
    public int countSchoolUnreadOfMonth(Long idKid, LocalDate startDate, LocalDate endDate, Long idClass) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (idKid != null) {
            queryStr.append("and id_kids=:idKid ");
            mapParams.put("idKid", idKid);
        }
        if (idClass != null) {
            queryStr.append("and id_class =:idClass ");
            mapParams.put("idClass", idClass);
        }
        if (startDate != null && endDate != null) {
            queryStr.append("and date>=:startDate and date<:endDate ");
            mapParams.put("startDate", startDate.minusDays(6));
            mapParams.put("endDate", endDate);
        }
        queryStr.append("and parent_reply_content != '' ");
        queryStr.append("and school_read_reply=false ");
        List<EvaluateWeek> evaluateDateList = findAllNoPaging(queryStr.toString(), mapParams);
        return evaluateDateList.size();
    }

    @Override
    public int countSchoolUnreadTeacher(Long idSchool, Long idKid, Long idClass, List<Long> idClassList, LocalDate localDate) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and date<:date ");
        mapParams.put("date", localDate);
        queryStr.append("and id_kids=:idKid ");
        mapParams.put("idKid", idKid);

        queryStr.append("and id_class in (:idClassList) ");
        mapParams.put("idClassList", idClassList);
        queryStr.append("and case when id_class!=:idClass and approved=false then false else true end ");
        mapParams.put("idClass", idClass);

        queryStr.append("and parent_reply_content!='' ");
        queryStr.append("and school_read_reply=false ");
        List<EvaluateWeek> evaluateDateList = findAllNoPaging(queryStr.toString(), mapParams);
        return evaluateDateList.size();
    }

    @Override
    public List<EvaluateWeek> findEvaluateWeekKidAndPaging(Long idSchool, Long idClass, List<Long> idClassList, boolean historyView, KidsPageNumberRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();

        queryStr.append("and id_class in (:idClassList) ");
        mapParams.put("idClassList", idClassList);
        queryStr.append("and case when id_class!=:idClass and approved=false then false else true end ");
        mapParams.put("idClass", idClass);
        queryStr.append("and case when id_school!=:idSchool and :historyView then false else true end ");
        mapParams.put("idSchool", idSchool);
        mapParams.put("historyView", !historyView);

        queryStr.append("and id_kids=:idKid ");
        mapParams.put("idKid", request.getIdKid());
        queryStr.append("and content!='' ");

        queryStr.append("order by date desc, id desc");

        List<EvaluateWeek> evaluateDateList = findAllMobilePaging(queryStr.toString(), mapParams, request.getPageNumber());
        return evaluateDateList;
    }

    @Override
    public List<EvaluateWeek> findEvaluateWeekKidAndPaging(Long idSchool, Long idClass, List<Long> idClassList, boolean historyView, KidsPageNumberPlusRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();

        queryStr.append("and id_class in (:idClassList) ");
        mapParams.put("idClassList", idClassList);
        queryStr.append("and case when id_class!=:idClass and approved=false then false else true end ");
        mapParams.put("idClass", idClass);
        queryStr.append("and case when id_school!=:idSchool and :historyView then false else true end ");
        mapParams.put("idSchool", idSchool);
        mapParams.put("historyView", !historyView);

        queryStr.append("and id_kids=:idKid ");
        mapParams.put("idKid", request.getIdKid());
        queryStr.append("and content!='' ");

        queryStr.append("order by date desc, id desc");

        List<EvaluateWeek> evaluateDateList = findAllMobilePaging(queryStr.toString(), mapParams, request.getPageNumber());
        return evaluateDateList;
    }

    @Override
    public List<EvaluateWeek> findByEvaluateWeekWidthClass(Long idClass, LocalDate date) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_class=:idClass ");
        mapParams.put("idClass", idClass);
        queryStr.append("and date=:date ");
        mapParams.put("date", date);
        queryStr.append("order by (Select first_name from ma_kids model1 where model1.id=model.id_kids) collate utf8_vietnamese_ci ");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<EvaluateWeek> findEvaluateWeekClassWidthDate(Long idClass, LocalDate date) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();

        queryStr.append("and id_class =:idClass ");
        mapParams.put("idClass", idClass);
        queryStr.append("and date =:date ");
        mapParams.put("date", date);
        queryStr.append("and approved =false ");

        queryStr.append("and (content!='' or EXISTS (SELECT * FROM evaluate_week_file WHERE model.id = evaluate_week_file.id_evaluate_week)) ");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }
}
