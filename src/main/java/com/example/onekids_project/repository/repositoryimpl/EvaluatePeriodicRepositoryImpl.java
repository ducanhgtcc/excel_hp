package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.kids.EvaluatePeriodic;
import com.example.onekids_project.mobile.plus.request.KidsPageNumberPlusRequest;
import com.example.onekids_project.mobile.teacher.request.evaluate.KidsPageNumberRequest;
import com.example.onekids_project.repository.repositorycustom.EvaluatePeriodicRepositoryCustom;
import com.example.onekids_project.request.evaluatekids.EvaluatePeriodicSearchRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EvaluatePeriodicRepositoryImpl extends BaseRepositoryimpl<EvaluatePeriodic> implements EvaluatePeriodicRepositoryCustom {
    @Override
    public List<EvaluatePeriodic> findEvaluatePeriodicMobile(Long idSchool, Long idKid) {
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
        queryStr.append("order by date desc");
        List<EvaluatePeriodic> evaluatePeriodicList = findAllNoPaging(queryStr.toString(), mapParams);
        return evaluatePeriodicList;
    }

    @Override
    public List<EvaluatePeriodic> searchEvaluatePeriodicLast(Long idSchool, EvaluatePeriodicSearchRequest evaluatePeriodicSearchRequest) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        if (evaluatePeriodicSearchRequest.getIdGrade() != null) {
            queryStr.append("and id_grade=:idGrade ");
            mapParams.put("idGrade", evaluatePeriodicSearchRequest.getIdGrade());
        }
        if (evaluatePeriodicSearchRequest.getIdClass() != null) {
            queryStr.append("and id_class=:idClass ");
            mapParams.put("idClass", evaluatePeriodicSearchRequest.getIdClass());
        }
        if (evaluatePeriodicSearchRequest.getApproved() != null) {
            queryStr.append("and approved=:approved ");
            mapParams.put("approved", evaluatePeriodicSearchRequest.getApproved());
        }

        queryStr.append("and Id = (SELECT max(Id) FROM ma_evaluate_periodic WHERE model.id_kids = id_kids) ");
        queryStr.append("order by id desc");
        List<EvaluatePeriodic> evaluatePeriodicList = findAllNoPaging(queryStr.toString(), mapParams);
        return evaluatePeriodicList;
    }

    @Override
    public List<EvaluatePeriodic> findParentUnreadOfYear(Long idSchool, Long idKid) {
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
        queryStr.append("and (content!='' or EXISTS (SELECT * FROM evaluate_periodic_file WHERE model.id = evaluate_periodic_file.id_evaluate_periodic)) ");
        List<EvaluatePeriodic> evaluatePeriodicList = findAllNoPaging(queryStr.toString(), mapParams);
        return evaluatePeriodicList;
    }

    @Override
    public List<EvaluatePeriodic> findParentdforKid(Long idSchool, Long idKid, Long id, Pageable pageable) {
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
        queryStr.append("and (content!='' or EXISTS (SELECT * FROM evaluate_periodic_file WHERE model.id = evaluate_periodic_file.id_evaluate_periodic)) ");
        queryStr.append("order by created_date desc ");
        List<EvaluatePeriodic> evaluatePeriodicList = findAllMobile(queryStr.toString(), mapParams, pageable);
        return evaluatePeriodicList;
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
        queryStr.append("and (content!='' or EXISTS (SELECT * FROM evaluate_periodic_file WHERE model.id = evaluate_periodic_file.id_evaluate_periodic)) ");
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
        List<EvaluatePeriodic> dataList = findAllNoPaging(queryStr.toString(), mapParams);
        return dataList.size();
    }

    @Override
    public List<EvaluatePeriodic> findEvaluatePeriodicOfMontKid(Long idKid, Long idClass, List<Long> idClassList, LocalDate startDate, LocalDate endDate) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_kids=:idKid ");
        mapParams.put("idKid", idKid);

        queryStr.append("and id_class in (:idClassList) ");
        mapParams.put("idClassList", idClassList);
        queryStr.append("and case when id_class!=:idClass and approved=false then false else true end ");
        mapParams.put("idClass", idClass);

        if (startDate != null && endDate != null) {
            queryStr.append("and date>=:startDate and date<:endDate ");
            mapParams.put("startDate", startDate);
            mapParams.put("endDate", endDate);
        }
        queryStr.append("and (content!='' or EXISTS (SELECT * FROM evaluate_periodic_file WHERE model.id = evaluate_periodic_file.id_evaluate_periodic)) ");
        List<EvaluatePeriodic> evaluatePeriodicList = findAllNoPaging(queryStr.toString(), mapParams);
        return evaluatePeriodicList;
    }

    @Override
    public List<EvaluatePeriodic> findEvaluatePeriodicOfMonth(LocalDate startDate, LocalDate endDate, Long idClass) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (idClass != null) {
            queryStr.append("and id_class =:idClass ");
            mapParams.put("idClass", idClass);
        }
        if (startDate != null && endDate != null) {
            queryStr.append("and date>=:startDate and date<:endDate ");
            mapParams.put("startDate", startDate);
            mapParams.put("endDate", endDate);
        }
        queryStr.append("and (content!='' or EXISTS (SELECT * FROM evaluate_periodic_file WHERE model.id = evaluate_periodic_file.id_evaluate_periodic)) ");
        return findAllNoPaging(queryStr.toString(), mapParams);
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
            mapParams.put("startDate", startDate);
            mapParams.put("endDate", endDate);
        }
        queryStr.append("and parent_reply_content != '' ");
        queryStr.append("and school_read_reply=false ");
        List<EvaluatePeriodic> evaluateDateList = findAllNoPaging(queryStr.toString(), mapParams);
        return evaluateDateList.size();
    }


    @Override
    public int countSchoolUnreadTeacher(Long idKid, Long idClass, LocalDate localDate) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and date<:date ");
        mapParams.put("date", localDate);
        queryStr.append("and id_kids=:idKid ");
        mapParams.put("idKid", idKid);
        queryStr.append("and id_class=:idClass ");
        mapParams.put("idClass", idClass);
        queryStr.append("and parent_reply_content!='' ");
        queryStr.append("and school_read_reply=false ");
        List<EvaluatePeriodic> evaluateDateList = findAllNoPaging(queryStr.toString(), mapParams);
        return evaluateDateList.size();
    }

    @Override
    public List<EvaluatePeriodic> findEvaluatePeriodicKidAndPaging(Long idSchool, Long idClass, List<Long> idClassList, boolean historyView, KidsPageNumberRequest request) {
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

        queryStr.append("order by date desc");
        List<EvaluatePeriodic> evaluatePeriodicList = findAllMobilePaging(queryStr.toString(), mapParams, request.getPageNumber());
        return evaluatePeriodicList;
    }

    @Override
    public List<EvaluatePeriodic> findEvaluatePeriodicKidAndPaging(Long idSchool, Long idClass, List<Long> idClassList, boolean historyView, KidsPageNumberPlusRequest request) {
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

        queryStr.append("order by date desc");
        List<EvaluatePeriodic> evaluatePeriodicList = findAllMobilePaging(queryStr.toString(), mapParams, request.getPageNumber());
        return evaluatePeriodicList;
    }

    @Override
    public List<EvaluatePeriodic> findByEvaluatePeriodicWithClass(Long idClass, LocalDate date) {
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
    public List<EvaluatePeriodic> findClassDateHas(Long idClass, LocalDate date) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        queryStr.append("and id_class =:idClass ");
        mapParams.put("idClass", idClass);
        queryStr.append("and date=:date ");
        mapParams.put("date", date);
        queryStr.append("and approved =false ");
        queryStr.append("and (content!='' or EXISTS (SELECT * FROM evaluate_periodic_file WHERE model.id = evaluate_periodic_file.id_evaluate_periodic)) ");
        List<EvaluatePeriodic> evaluatePeriodicList = findAllNoPaging(queryStr.toString(), mapParams);
        return evaluatePeriodicList;
    }
}
