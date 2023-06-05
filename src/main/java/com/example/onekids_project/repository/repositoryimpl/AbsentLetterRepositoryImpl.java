package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.kids.AbsentLetter;
import com.example.onekids_project.mobile.plus.request.absent.SearchAbsentPlusRequest;
import com.example.onekids_project.mobile.teacher.request.absent.SearchAbsentTeacherRequest;
import com.example.onekids_project.repository.repositorycustom.AbsentLetterRepositoryCustom;
import com.example.onekids_project.request.parentdiary.SearchAbsentLetterRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AbsentLetterRepositoryImpl extends BaseRepositoryimpl<AbsentLetter> implements AbsentLetterRepositoryCustom {


    @Override
    public List<AbsentLetter> findAllAbsent(Long idSchoolLogin, Pageable pageable) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<AbsentLetter> searchAbsent(Long idSchool, SearchAbsentLetterRequest request) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchAbsentLetter(queryStr, mapParams, idSchool, request);
        return findAllWebPaging(queryStr.toString(), mapParams, request.getPageNumber(), request.getMaxPageItem());
    }

    @Override
    public Optional<AbsentLetter> findByIdAbsent(Long idSchool, Long id) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        if (id != null) {
            queryStr.append("and id=:id");
            mapParams.put("id", id);
        }
        List<AbsentLetter> absentLetterList = findAllNoPaging(queryStr.toString(), mapParams);
        if (absentLetterList.size() > 0) {
            return Optional.ofNullable(absentLetterList.get(0));
        }
        return Optional.empty();
    }

    @Override
    public List<AbsentLetter> findAbsentMobile(Long idSchool, Long idKid, Pageable pageable, LocalDateTime localDateTime) {
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
        if (localDateTime != null) {
            queryStr.append("and created_date<:createDate ");
            mapParams.put("createDate", localDateTime);
        }
        queryStr.append("and parent_absent_del=:parentAbsentDel ");
        mapParams.put("parentAbsentDel", AppConstant.APP_FALSE);
        queryStr.append("order by created_date desc");
        return findAllMobile(queryStr.toString(), mapParams, pageable);
    }

    @Override
    public Long getCountMessage(Long idSchool, Long idKid, LocalDateTime localDateTime) {
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
        if (localDateTime != null) {
            queryStr.append("and created_date<:createDate ");
            mapParams.put("createDate", localDateTime);
        }
        queryStr.append("and parent_absent_del=:parentAbsentDel ");
        mapParams.put("parentAbsentDel", AppConstant.APP_FALSE);
        return countAll(queryStr.toString(), mapParams);
    }

    @Override
    public List<AbsentLetter> findFromDate(Long idkid, LocalDate fromdate) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (idkid != null) {
            queryStr.append("and id_kids=:idkids ");
            mapParams.put("idkids", idkid);
        }
        queryStr.append("and from_date >=:nowDate ");
        mapParams.put("nowDate", LocalDate.now());
        List<AbsentLetter> absentLetterList = findAllNoPaging(queryStr.toString(), mapParams);
        return absentLetterList;
    }

    @Override
    public List<AbsentLetter> findMonthForAttendance(Long idSchool, Long idKid, Integer month, Integer year) {
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
            queryStr.append("and (year(from_date)=:year and month(from_date)=:month) or (year(to_date)=:year and month(to_date)=:month) ");
            mapParams.put("year", year);
            mapParams.put("month", month);
        }
        queryStr.append("and from_date<=:nowDate ");
        mapParams.put("nowDate", LocalDate.now());
        queryStr.append("and parent_absent_del=:parentAbsentDel ");
        mapParams.put("parentAbsentDel", AppConstant.APP_FALSE);
        List<AbsentLetter> absentLetterList = findAllNoPaging(queryStr.toString(), mapParams);
        return absentLetterList;
    }

    @Override
    public List<AbsentLetter> findAbsentForTeacher(Long idSchool, Long idClass, SearchAbsentTeacherRequest searchAbsentTeacherRequest) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (searchAbsentTeacherRequest != null) {
            if (searchAbsentTeacherRequest.getConfirmStatus() != null) {
                queryStr.append("and confirm_status=:confirmStatus ");
                mapParams.put("confirmStatus", searchAbsentTeacherRequest.getConfirmStatus());
            }
            if (StringUtils.isNotBlank(searchAbsentTeacherRequest.getDateAbsent())) {
                queryStr.append("and to_date >=:dateAbsent and from_date <=:dateAbsent ");
                mapParams.put("dateAbsent", LocalDate.now());
            }
            if (StringUtils.isNotBlank(searchAbsentTeacherRequest.getDateDetail())) {
                queryStr.append("and to_date >=:dateDetail and from_date <=:dateDetail ");
                mapParams.put("dateDetail", searchAbsentTeacherRequest.getDateDetail());
            }
            if (StringUtils.isNotBlank(searchAbsentTeacherRequest.getKeyWord())) {
                queryStr.append("and absent_content like :absentContent ");
                mapParams.put("absentContent", "%" + searchAbsentTeacherRequest.getKeyWord() + "%");
            }
            queryStr.append("and EXISTS (SELECT * FROM ma_kids as model1 WHERE model.id_kids = model1.id and del_active=true) ");
            queryStr.append("and id_class=:idClass ");
            mapParams.put("idClass", idClass);
            queryStr.append("and parent_absent_del=:parentAbsentDel ");
            mapParams.put("parentAbsentDel", AppConstant.APP_FALSE);
            queryStr.append("order by created_date desc");
        }
        return findAllMobilePaging(queryStr.toString(), mapParams, searchAbsentTeacherRequest.getPageNumber());
    }

    @Override
    public long countTotalAccount(Long idSchool, SearchAbsentLetterRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchAbsentLetter(queryStr, mapParams, idSchool, request);
        return countAll(queryStr.toString(), mapParams);
    }


    @Override
    public List<AbsentLetter> findAbsentInClassDate(Long idSchool, Long idClass, LocalDate date) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("and id_class=:idClass ");
        mapParams.put("idClass", idClass);
//        queryStr.append("and :date>=from_date and :date<=to_date ");
//        mapParams.put("date", date);
        queryStr.append("and parent_absent_del=:parentAbsentDel ");
        mapParams.put("parentAbsentDel", AppConstant.APP_FALSE);
        queryStr.append("and confirm_status=:confirmStatus ");
        mapParams.put("confirmStatus", AppConstant.APP_FALSE);
        List<AbsentLetter> absentLetterList = findAllNoPaging(queryStr.toString(), mapParams);
        return absentLetterList;
    }

    @Override
    public List<AbsentLetter> searchAbsentForPlus(Long idSchool, SearchAbsentPlusRequest request) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("and parent_absent_del=:parentAbsentDel ");
        mapParams.put("parentAbsentDel", AppConstant.APP_FALSE);
        if (request.getConfirmStatus() != null) {
            queryStr.append("and confirm_status=:confirmStatus ");
            mapParams.put("confirmStatus", request.getConfirmStatus());
        }
        if (request.getIdClass() != null) {
            queryStr.append("and id_class=:idClass ");
            mapParams.put("idClass", request.getIdClass());
        }
        if (StringUtils.isNotBlank(request.getDateAbsent())) {
            queryStr.append("and to_date >=:dateAbsent and from_date <=:dateAbsent ");
            mapParams.put("dateAbsent", LocalDate.now());
        }
        if (request.getDateDetail() != null) {
            queryStr.append("and to_date >=:dateDetail and from_date <=:dateDetail ");
            mapParams.put("dateDetail", request.getDateDetail());
        }
        if (StringUtils.isNotBlank(request.getKidName())) {
            request.setKidName(request.getKidName().trim());
            queryStr.append("and EXISTS (SELECT * FROM ma_kids as model1 WHERE model.id_kids = model1.id and full_name like :fullName) ");
            mapParams.put("fullName", "%" + request.getKidName() + "%");
        }
        queryStr.append("and EXISTS (SELECT * FROM ma_kids as model1 WHERE model.id_kids = model1.id and del_active=true) ");
        queryStr.append("order by created_date desc");
        return findAllMobilePaging(queryStr.toString(), mapParams, request.getPageNumber());
    }

    private void setSearchAbsentLetter(StringBuilder queryStr, Map<String, Object> mapParams, Long idSchool, SearchAbsentLetterRequest request) {
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        if (request.getIdGrade() != null) {
            queryStr.append("and id_grade=:idGrade ");
            mapParams.put("idGrade", request.getIdGrade());
        }
        if (request.getDate() != null) {
            queryStr.append("and date(created_date) = :createdDate ");
            mapParams.put("createdDate", request.getDate());
        }
        if (request.getIdClass() != null) {
            queryStr.append("and id_class=:idClass ");
            mapParams.put("idClass", request.getIdClass());
        }
        if (request.getConfirmStatus() != null) {
            queryStr.append("and confirm_status=:confirmStatus ");
            mapParams.put("confirmStatus", request.getConfirmStatus());
        }
        if (StringUtils.isNotBlank(request.getDateSick())) {
            queryStr.append("and to_date >=:dateSick and from_date <=:dateSick ");
            mapParams.put("dateSick", LocalDate.now());
        }
        if (StringUtils.isNotBlank(request.getName())) {
            queryStr.append("and absent_content like :absentContent ");
            mapParams.put("absentContent", "%" + request.getName() + "%");
        }
        queryStr.append("and EXISTS (SELECT * FROM ma_kids as model1 WHERE model.id_kids = model1.id and del_active=true) ");
        queryStr.append("order by created_date desc");
    }
}
