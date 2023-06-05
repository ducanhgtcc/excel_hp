package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.kids.EvaluateMonth;
import com.example.onekids_project.mobile.plus.request.KidsPageNumberPlusRequest;
import com.example.onekids_project.mobile.teacher.request.evaluate.KidsPageNumberRequest;
import com.example.onekids_project.repository.repositorycustom.EvaluateMonthRepositoryCustom;
import com.example.onekids_project.request.evaluatekids.EvaluateDateSearchRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EvaluateMonthRepositoryImpl extends BaseRepositoryimpl<EvaluateMonth> implements EvaluateMonthRepositoryCustom {
    @Override
    public List<EvaluateMonth> searchEvaluateMonth(Long idSchool, EvaluateDateSearchRequest evaluateDateSearchRequest, int month, int year) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("and month=:month and year=:year ");
        mapParams.put("month", month);
        mapParams.put("year", year);
        queryStr.append("and id_class=:idClass ");
        mapParams.put("idClass", evaluateDateSearchRequest.getIdClass());
        if (evaluateDateSearchRequest.getApproved() != null) {
            queryStr.append("and approved=:approved ");
            mapParams.put("approved", evaluateDateSearchRequest.getApproved());
        }
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<EvaluateMonth> findEvaluateMonthMobile(Long idSchool, Long idKid) {
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
        queryStr.append("order by year desc, month desc, id desc ");
        List<EvaluateMonth> evaluateMonthList = findAllNoPaging(queryStr.toString(), mapParams);
        return evaluateMonthList;
    }

    @Override
    public List<EvaluateMonth> findParentUnreadOfYear(Long idSchool, Long idKid) {
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
        queryStr.append("and (content!='' or EXISTS (SELECT * FROM evaluate_month_file WHERE model.id = evaluate_month_file.id_evaluate_month)) ");
        List<EvaluateMonth> evaluateMonthList = findAllNoPaging(queryStr.toString(), mapParams);
        return evaluateMonthList;
    }

    @Override
    public List<EvaluateMonth> findParentdforKid(Long idSchool, Long idKid, Long id, Pageable pageable) {
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
        queryStr.append("and (content!='' or EXISTS (SELECT * FROM evaluate_month_file WHERE model.id = evaluate_month_file.id_evaluate_month)) ");
        queryStr.append("order by year desc, month desc, id desc ");
        List<EvaluateMonth> evaluateMonthList = findAllMobile(queryStr.toString(), mapParams, pageable);
        return evaluateMonthList;
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
        queryStr.append("and (content!='' or EXISTS (SELECT * FROM evaluate_month_file WHERE model.id = evaluate_month_file.id_evaluate_month)) ");
        return countAll(queryStr.toString(), mapParams);
    }

    @Override
    public int countNewParentReply(Long idClass) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (idClass != null) {
            queryStr.append("and id_class=:idClass ");
            mapParams.put("idClass", idClass);
        }
        queryStr.append("and parent_reply_content!='' ");
        queryStr.append("and school_read_reply=false ");
        List<EvaluateMonth> dataList = findAllNoPaging(queryStr.toString(), mapParams);
        return dataList.size();
    }

    @Override
    public List<EvaluateMonth> findEvaluateMonthOfMontKid(Long idKid, Long idClass, List<Long> idClassList, LocalDate date) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_kids=:idKid ");
        mapParams.put("idKid", idKid);

        queryStr.append("and id_class in (:idClassList) ");
        mapParams.put("idClassList", idClassList);
        queryStr.append("and case when id_class!=:idClass and approved=false then false else true end ");
        mapParams.put("idClass", idClass);

        if (date != null) {
            queryStr.append("and month=:month and year=:year ");
            mapParams.put("month", date.getMonthValue());
            mapParams.put("year", date.getYear());
        }
        queryStr.append("and (content!='' or EXISTS (SELECT * FROM evaluate_month_file WHERE model.id = evaluate_month_file.id_evaluate_month)) ");
        List<EvaluateMonth> evaluateMonthList = findAllNoPaging(queryStr.toString(), mapParams);
        return evaluateMonthList;
    }

    @Override
    public int countSchoolUnreadOfMonth(Long idKid, LocalDate date, Long idClass) {
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
        if (date != null) {
            queryStr.append("and month=:month and year=:year ");
            mapParams.put("month", date.getMonthValue());
            mapParams.put("year", date.getYear());
        }
        queryStr.append("and parent_reply_content != '' ");
        queryStr.append("and school_read_reply=false ");
        List<EvaluateMonth> evaluateDateList = findAllNoPaging(queryStr.toString(), mapParams);
        return evaluateDateList.size();
    }

    @Override
    public int countSchoolUnreadTeacher(Long idKid, Long idClass, List<Long> idClassList, LocalDate localDate) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (localDate != null) {
            queryStr.append("and date(auto_create_date)<:date ");
            mapParams.put("date", localDate);
        }
        queryStr.append("and id_kids=:idKid ");
        mapParams.put("idKid", idKid);

        queryStr.append("and id_class in (:idClassList) ");
        mapParams.put("idClassList", idClassList);
        queryStr.append("and case when id_class!=:idClass and approved=false then false else true end ");
        mapParams.put("idClass", idClass);

        queryStr.append("and parent_reply_content!='' ");
        queryStr.append("and school_read_reply=false ");
        List<EvaluateMonth> evaluateDateList = findAllNoPaging(queryStr.toString(), mapParams);
        return evaluateDateList.size();
    }

    @Override
    public List<EvaluateMonth> findEvaluateMonthKidAndPaging(Long idSchool, Long idClass, List<Long> idClassList, boolean historyView, KidsPageNumberRequest request) {
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

        queryStr.append("order by year desc, month desc, id desc ");
        List<EvaluateMonth> evaluateDateList = findAllMobilePaging(queryStr.toString(), mapParams, request.getPageNumber());
        return evaluateDateList;
    }

    @Override
    public List<EvaluateMonth> findEvaluateMonthKidAndPaging(Long idSchool, Long idClass, List<Long> idClassList, boolean historyView, KidsPageNumberPlusRequest request) {
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

        queryStr.append("order by year desc, month desc, id desc ");
        List<EvaluateMonth> evaluateDateList = findAllMobilePaging(queryStr.toString(), mapParams, request.getPageNumber());
        return evaluateDateList;
    }

    @Override
    public List<EvaluateMonth> findByEvaluateMonthWithClass(Long idClass, int month, int year) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_class=:idClass ");
        mapParams.put("idClass", idClass);
        queryStr.append("and month=:month ");
        mapParams.put("month", month);
        queryStr.append("and year=:year ");
        mapParams.put("year", year);
        queryStr.append("order by (Select first_name from ma_kids model1 where model1.id=model.id_kids) collate utf8_vietnamese_ci ");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<EvaluateMonth> findEvaluateMonthClassWidthDate(Long idClass, LocalDate date) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();

        queryStr.append("and id_class =:idClass ");
        mapParams.put("idClass", idClass);
        queryStr.append("and month=:month and year=:year ");
        mapParams.put("month", date.getMonthValue());
        mapParams.put("year", date.getYear());
        queryStr.append("and approved =false ");
        queryStr.append("and (content!='' or EXISTS (SELECT * FROM evaluate_month_file WHERE model.id = evaluate_month_file.id_evaluate_month)) ");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<EvaluateMonth> searchEvaluateKidsMonthChart(Long idSchool, Long idGrade, Long idClass, LocalDate date) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.getEvaluateKidsMonthChart(queryStr, mapParams, idSchool, idGrade, idClass, date);
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    private void getEvaluateKidsMonthChart(StringBuilder queryStr, Map<String, Object> mapParams, Long idSchool, Long idGrade, Long idClass, LocalDate date){
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
            long month = date.getMonthValue();
            long year = date.getYear();
            queryStr.append("and month =:month ");
            mapParams.put("month", month);
            queryStr.append("and year =:year ");
            mapParams.put("year", year);
        }
    }



}
