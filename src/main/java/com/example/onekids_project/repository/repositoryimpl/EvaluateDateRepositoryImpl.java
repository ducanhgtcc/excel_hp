package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.kids.EvaluateDate;
import com.example.onekids_project.mobile.response.StartEndDateObject;
import com.example.onekids_project.repository.repositorycustom.EvaluateDateRepositoryCustom;
import com.example.onekids_project.request.evaluatekids.EvaluateDateSearchRequest;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EvaluateDateRepositoryImpl extends BaseRepositoryimpl<EvaluateDate> implements EvaluateDateRepositoryCustom {
    @Override
    public List<EvaluateDate> searchEvaluateKidsDate(Long idSchool, EvaluateDateSearchRequest evaluateDateSearchRequest) {
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
        queryStr.append("order by (Select first_name from ma_kids model1 where model1.id=model.id_kids) collate utf8_vietnamese_ci ");
        List<EvaluateDate> evaluateDateList = findAllNoPaging(queryStr.toString(), mapParams);
        return evaluateDateList;
    }

    @Override
    public List<EvaluateDate> searchEvaluateKidsDateOfMonth(Long idSchool, Long idClass, LocalDate dateStart, LocalDate dateEnd) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        if (idClass != null) {
            queryStr.append("and id_class=:idClass ");
            mapParams.put("idClass", idClass);
        }
        if (idClass != null) {
            queryStr.append("and date>=:dateStart and date<:dateEnd ");
            mapParams.put("dateStart", dateStart);
            mapParams.put("dateEnd", dateEnd);
        }
        queryStr.append("order by date asc");
        List<EvaluateDate> evaluateDateList = findAllNoPaging(queryStr.toString(), mapParams);
        return evaluateDateList;
    }

    @Override
    public List<EvaluateDate> findEvaluateDateKidMobile(Long idSchool, Long idKid) {
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
        List<EvaluateDate> evaluateDateList = findAllNoPaging(queryStr.toString(), mapParams);
        return evaluateDateList;
    }

    @Override
    public Optional<EvaluateDate> findEvaluateDateKidDateMobile(Long idSchool, Long idKid, LocalDate localDate) {
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
        if (localDate != null) {
            queryStr.append("and date=:date ");
            mapParams.put("date", localDate);
        }

        queryStr.append("and approved=true ");
//        queryStr.append("order by date desc");
        List<EvaluateDate> evaluateDateList = findAllNoPaging(queryStr.toString(), mapParams);
        if (CollectionUtils.isEmpty(evaluateDateList)) {
            return Optional.empty();
        }
        return Optional.ofNullable(evaluateDateList.get(0));
    }

    @Override
    public Optional<EvaluateDate> findEvaluateDateKidHas(Long idSchool, Long idKid) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("and id_kids=:idKid ");
        mapParams.put("idKid", idKid);
        queryStr.append("and (learn_content!='' or eat_content!='' or sleep_content!='' or healt_content!='' or sanitary_content!='' or common_content!='' or EXISTS (SELECT * FROM evaluate_attach_file WHERE model.id = evaluate_attach_file.id_evaluate_date)) ");
        queryStr.append("and approved=true ");
        queryStr.append("order by date desc");
        List<EvaluateDate> evaluateDateList = findAllNoPaging(queryStr.toString(), mapParams);
        if (CollectionUtils.isEmpty(evaluateDateList)) {
            return Optional.empty();
        }
        return Optional.ofNullable(evaluateDateList.get(0));
    }

    @Override
    public List<EvaluateDate> totalEvaluateDateMonthMobile(Long idSchool, Long idKid, Integer month, Integer year) {
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
        if (month != null && year != null) {
            queryStr.append("and month(date)=:month and year(date)=:year ");
            mapParams.put("month", month);
            mapParams.put("year", year);
        }

        queryStr.append("and approved=true ");
        queryStr.append("order by date desc");
        List<EvaluateDate> evaluateDateList = findAllNoPaging(queryStr.toString(), mapParams);
        return evaluateDateList;
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
        List<EvaluateDate> evaluateDateList = findAllNoPaging(queryStr.toString(), mapParams);
        return evaluateDateList.size();
    }

    @Override
    public List<EvaluateDate> findEvaluateDateOfMonthTeacher(Long idClass, LocalDate startDate, LocalDate endDate) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (idClass != null) {
            queryStr.append("and id_class=:idClass ");
            mapParams.put("idClass", idClass);
        }
        if (startDate != null && endDate != null) {
            queryStr.append("and date>=:startDate and date<:endDate ");
            mapParams.put("startDate", startDate);
            mapParams.put("endDate", endDate);
        }
        List<EvaluateDate> evaluateDateList = findAllNoPaging(queryStr.toString(), mapParams);
        return evaluateDateList;
    }

    @Override
    public List<EvaluateDate> findEvaluateDateOfMonthKid(Long idKid, Long idClass, List<Long> idClassList, LocalDate startDate, LocalDate endDate) {
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
        queryStr.append("and (learn_content!='' or eat_content!='' or sleep_content!='' or healt_content!='' or sanitary_content!='' or common_content!='' or EXISTS (SELECT * FROM evaluate_attach_file WHERE model.id = evaluate_attach_file.id_evaluate_date)) ");
        List<EvaluateDate> evaluateDateList = findAllNoPaging(queryStr.toString(), mapParams);
        return evaluateDateList;
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
        List<EvaluateDate> evaluateDateList = findAllNoPaging(queryStr.toString(), mapParams);
        return evaluateDateList.size();
    }

    @Override
    public int countSchoolUnreadOfMonthTeacher(Long idKid, Long idClass, LocalDate startDate, LocalDate endDate, LocalDate localDate) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and date<:date ");
        mapParams.put("date", localDate);
        if (idKid != null) {
            queryStr.append("and id_kids=:idKid ");
            mapParams.put("idKid", idKid);
        }
        if (idClass != null) {
            queryStr.append("and id_class=:idClass ");
            mapParams.put("idClass", idClass);
        }
        if (startDate != null && endDate != null) {
            queryStr.append("and date>=:startDate and date<:endDate ");
            mapParams.put("startDate", startDate);
            mapParams.put("endDate", endDate);
        }
        queryStr.append("and parent_reply_content!='' ");
        queryStr.append("and school_read_reply=false ");
        List<EvaluateDate> evaluateDateList = findAllNoPaging(queryStr.toString(), mapParams);
        return evaluateDateList.size();
    }

    @Override
    public List<EvaluateDate> findEvaluateClassDate(Long idClass, LocalDate date) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        if (idClass != null) {
            queryStr.append("and id_class=:idClass ");
            mapParams.put("idClass", idClass);
        }
        if (date != null) {
            queryStr.append("and date=:date ");
            mapParams.put("date", date);
        }
        queryStr.append("and (learn_content!='' or eat_content!='' or sleep_content!='' or healt_content!='' or sanitary_content!='' or common_content!='' or EXISTS (SELECT * FROM evaluate_attach_file WHERE model.id = evaluate_attach_file.id_evaluate_date)) ");
        List<EvaluateDate> evaluateDateList = findAllNoPaging(queryStr.toString(), mapParams);
        return evaluateDateList;
    }

    @Override
    public int countSchoolUnreadKidOfMonth(Long idClass, Long idKid, StartEndDateObject startEndDateObject) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        if (idKid != null) {
            queryStr.append("and id_kids=:idKid ");
            mapParams.put("idKid", idKid);
        }
        if (idClass != null) {
            queryStr.append("and id_class=:idClass ");
            mapParams.put("idClass", idClass);
        }
        if (startEndDateObject != null) {
            queryStr.append("and date>=:startDate and date<:endDate ");
            mapParams.put("startDate", startEndDateObject.getStartDate());
            mapParams.put("endDate", startEndDateObject.getEndDate());
        }
        queryStr.append("and parent_reply_content!='' ");
        queryStr.append("and school_read_reply=false ");
        List<EvaluateDate> evaluateDateList = findAllNoPaging(queryStr.toString(), mapParams);
        return evaluateDateList.size();
    }

    @Override
    public List<EvaluateDate> findKidDateHaveOfMonth(Long idClass, Long idKid, StartEndDateObject startEndDateObject) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        if (idKid != null) {
            queryStr.append("and id_kids=:idKid ");
            mapParams.put("idKid", idKid);
        }
        if (idClass != null) {
            queryStr.append("and id_class=:idClass ");
            mapParams.put("idClass", idClass);
        }
        if (startEndDateObject != null) {
            queryStr.append("and date>=:startDate and date<:endDate ");
            mapParams.put("startDate", startEndDateObject.getStartDate());
            mapParams.put("endDate", startEndDateObject.getEndDate());
        }
        queryStr.append("and (learn_content!='' or eat_content!='' or sleep_content!='' or healt_content!='' or sanitary_content!='' or common_content!='' or EXISTS (SELECT * FROM evaluate_attach_file WHERE model.id = evaluate_attach_file.id_evaluate_date)) ");
        List<EvaluateDate> evaluateDateList = findAllNoPaging(queryStr.toString(), mapParams);
        return evaluateDateList;
    }

    @Override
    public List<EvaluateDate> findKidDateHaveOfReplyMonth(Long idClass, Long idKid, StartEndDateObject startEndDateObject) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        if (idKid != null) {
            queryStr.append("and id_kids=:idKid ");
            mapParams.put("idKid", idKid);
        }
        if (idClass != null) {
            queryStr.append("and id_class=:idClass ");
            mapParams.put("idClass", idClass);
        }
        if (startEndDateObject != null) {
            queryStr.append("and date>=:startDate and date<:endDate ");
            mapParams.put("startDate", startEndDateObject.getStartDate());
            mapParams.put("endDate", startEndDateObject.getEndDate());
        }
        queryStr.append("and parent_reply_content!='' ");
        List<EvaluateDate> evaluateDateList = findAllNoPaging(queryStr.toString(), mapParams);
        return evaluateDateList;
    }

    @Override
    public Optional<EvaluateDate> findEvaluateDateOfDate(Long idSchool, Long idClass, boolean historyView, List<Long> idClassList, Long idKid, LocalDate localDate) {
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
        mapParams.put("idKid", idKid);
        queryStr.append("and date=:date ");
        mapParams.put("date", localDate);

        List<EvaluateDate> evaluateDateList = findAllNoPaging(queryStr.toString(), mapParams);
        if (CollectionUtils.isEmpty(evaluateDateList)) {
            return Optional.empty();
        }
        return Optional.ofNullable(evaluateDateList.get(0));
    }

    @Override
    public Optional<EvaluateDate> findEvaluateDateHas(Long idClass, List<Long> idClassList, Long idKid) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_class in (:idClassList) ");
        mapParams.put("idClassList", idClassList);
        queryStr.append("and case when id_class!=:idClass and approved=false then false else true end ");
        mapParams.put("idClass", idClass);

        queryStr.append("and id_kids=:idKid ");
        mapParams.put("idKid", idKid);

        queryStr.append("and (learn_content!='' or eat_content!='' or sleep_content!='' or healt_content!='' or sanitary_content!='' or common_content!='' or EXISTS (SELECT * FROM evaluate_attach_file WHERE model.id = evaluate_attach_file.id_evaluate_date)) ");
        queryStr.append("order by date desc");
        List<EvaluateDate> evaluateDateList = findAllNoPaging(queryStr.toString(), mapParams);
        if (CollectionUtils.isEmpty(evaluateDateList)) {
            return Optional.empty();
        }
        return Optional.ofNullable(evaluateDateList.get(0));
    }

    @Override
    public List<EvaluateDate> findEvaluateDateTeacher(Long idClass, LocalDate date) {
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
    public List<EvaluateDate> findClassDateHas(Long idClass, LocalDate date) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_class=:idClass ");
        mapParams.put("idClass", idClass);
        queryStr.append("and date=:date ");
        mapParams.put("date", date);
        queryStr.append("and approved =false ");
        queryStr.append("and (learn_content!='' or eat_content!='' or sleep_content!='' or healt_content!='' or sanitary_content!='' or common_content!='' or EXISTS (SELECT * FROM evaluate_attach_file WHERE model.id = evaluate_attach_file.id_evaluate_date)) ");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<EvaluateDate> searchEvaluateKidsDateChart(Long idSchool, Long idGrade, Long idClass, LocalDate date) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.getEvaluateKidsDateChart(queryStr, mapParams, idSchool, idGrade, idClass, date);
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    private void getEvaluateKidsDateChart(StringBuilder queryStr, Map<String, Object> mapParams, Long idSchool, Long idGrade, Long idClass, LocalDate date){
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
}
