package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.parent.Parent;
import com.example.onekids_project.repository.repositorycustom.ParentRepositoryCustom;
import com.example.onekids_project.request.base.BaseRequest;
import com.example.onekids_project.request.base.PageNumberWebRequest;
import com.example.onekids_project.request.birthdaymanagement.SearchParentBirthDayRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ParentRepositoryImpl extends BaseRepositoryimpl<Parent> implements ParentRepositoryCustom {
    @Override
    public Optional<Parent> findByIdParent(Long id, boolean appTrue) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (id != null) {
            queryStr.append("and id=:id");
            mapParams.put("id", id);
        }
        List<Parent> parentList = findAllNoPaging(queryStr.toString(), mapParams);
        if (parentList.size() > 0) {
            return Optional.ofNullable(parentList.get(0));
        }
        return Optional.empty();
    }

    @Override
    public List<Parent> searchParentBirthday(Long idSchoolLogin, SearchParentBirthDayRequest searchParentBirthDayRequest) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (searchParentBirthDayRequest != null) {

            queryStr.append(" and exists(select*from ma_employee as a, info_employee_school as b,where model.app_type=:appType and model.id = a.id_ma_user and a.id = b.id_employee;)");
            mapParams.put("appType", AppTypeConstant.TEACHER);

            if (searchParentBirthDayRequest.getDate() != null) {
                queryStr.append("and where representation LIKE 'Bố' and father_birthday=:fatherBirthday");
                mapParams.put("fatherBirthday", searchParentBirthDayRequest.getDate());
            }
            if (searchParentBirthDayRequest.getWeek() != null) {
                queryStr.append("and father_birthday>=:monday or  mother_birthday>=:monday ");
                mapParams.put("monday", searchParentBirthDayRequest.getWeek().toString());
                queryStr.append(" and father_birthday<=:sunday or mother_birthday<=:sunday ");
                mapParams.put("sunday", searchParentBirthDayRequest.getWeek().plusDays(6).toString());
            }
            if (searchParentBirthDayRequest.getMonth() != null) {
                queryStr.append("and month(father_birthday) =:month ");
                mapParams.put("month", searchParentBirthDayRequest.getMonth().getMonthValue());
                queryStr.append("or month(mother_birthday) =:month ");
                mapParams.put("month", searchParentBirthDayRequest.getMonth().getMonthValue());
            }
            if (StringUtils.isNotBlank(searchParentBirthDayRequest.getName())) {
                queryStr.append("and mother_name like :motherName ");
                mapParams.put("motherName", "%" + searchParentBirthDayRequest.getName() + "%");
                queryStr.append("or father_name like :fatherName ");
                mapParams.put("fatherName", "%" + searchParentBirthDayRequest.getName() + "%");
            }
        }
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<Parent> findAllParentBirthday(Long idSchoolLogin, PageNumberWebRequest request) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        return findAllWebPaging(queryStr.toString(), mapParams, request.getPageNumber(), request.getMaxPageItem());
    }

    @Override
    public List<Parent> findAllParentAppSend(List<Long> idSchoolList) {
        StringBuilder quertStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (!CollectionUtils.isEmpty(idSchoolList)) {
            quertStr.append(" and exists(select*from ma_kids where model.id=ma_kids.id_parent and ma_kids.id_school in :idSchoolList)");
            mapParams.put("idSchoolList", idSchoolList);
        }
        return findAllNoPaging(quertStr.toString(), mapParams);
    }

    @Override
    public List<Parent> findAllParentStudent(List<Long> idKidsList) {

        StringBuilder quertStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (!CollectionUtils.isEmpty(idKidsList)) {
            quertStr.append(" and exists(select*from ma_kids where model.id=ma_kids.id_parent and ma_kids.id in :idKidsList)");
            mapParams.put("idKidsList", idKidsList);
        }
        return findAllNoPaging(quertStr.toString(), mapParams);
    }


    @Override
    public List<Parent> findAllParentGrade(List<Long> idGradeList) {

        StringBuilder quertStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (!CollectionUtils.isEmpty(idGradeList)) {
            quertStr.append(" and exists(select*from ma_kids where model.id=ma_kids.id_parent and ma_kids.id_grade in :idGradeList)");
            mapParams.put("idGradeList", idGradeList);
        }
        return findAllNoPaging(quertStr.toString(), mapParams);
    }

    @Override
    public List<Parent> findAllParentClass(List<Long> idClassList) {

        StringBuilder quertStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (!CollectionUtils.isEmpty(idClassList)) {
            quertStr.append(" and exists(select*from ma_kids where model.id=ma_kids.id_parent and ma_kids.id_class in :idClassList)");
            mapParams.put("idClassList", idClassList);
        }
        return findAllNoPaging(quertStr.toString(), mapParams);
    }

    @Override
    public List<Parent> findAllParentGroup(List<Long> idGroupList) {
        StringBuilder quertStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (!CollectionUtils.isEmpty(idGroupList)) {
            quertStr.append(" and exists(select*from ma_kids as ma, ex_kids_group as ekg where model.id=ma.id_parent and ma.id = ekg.id_kids and ekg.id_kids_group in :idGroupList)");
            mapParams.put("idGroupList", idGroupList);
        }
        return findAllNoPaging(quertStr.toString(), mapParams);
    }

    @Override
    public List<Parent> findParentAllBirthdayNoSchool(LocalDate localDate) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (localDate != null) {
            queryStr.append("and day(birthday) =:day ");
            mapParams.put("day", localDate.getDayOfMonth());
            queryStr.append("and month(birthday) =:month ");
            mapParams.put("month", localDate.getMonthValue());
        }
        List<Parent> dataList = findAllNoPaging(queryStr.toString(), mapParams);
        return dataList;
    }
}
