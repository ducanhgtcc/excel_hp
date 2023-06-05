package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.employee.AbsentTeacher;
import com.example.onekids_project.mobile.plus.request.absentteacher.SearchAbsentTeacherPlusRequest;
import com.example.onekids_project.mobile.request.PageNumberRequest;
import com.example.onekids_project.repository.repositorycustom.AbsentTeacherRepositoryCustom;
import com.example.onekids_project.request.absentteacher.SearchAbsentTeacherRequest;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * date 2021-05-21 2:36 PM
 *
 * @author nguyễn văn thụ
 */
public class AbsentTeacherRepositoryImpl extends BaseRepositoryimpl<AbsentTeacher> implements AbsentTeacherRepositoryCustom {

    @Override
    public List<AbsentTeacher> searchAbsentTeacher(SearchAbsentTeacherRequest request, Long idSchool) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchAbsentTeacher(queryStr, mapParams, request, idSchool);
        return findAllWebPaging(queryStr.toString(), mapParams, request.getPageNumber(), request.getMaxPageItem());
    }

    @Override
    public List<AbsentTeacher> findFromDate(Long idInfoEmployee, LocalDate fromDate, LocalDate toDate) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_info_employee=:idInfoEmployee ");
        mapParams.put("idInfoEmployee", idInfoEmployee);
        queryStr.append("and from_date >=:fromDate ");
        mapParams.put("fromDate", fromDate);
        queryStr.append("and from_date <=:toDate ");
        mapParams.put("toDate", toDate);
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public long countTotalAccount(SearchAbsentTeacherRequest request, Long idSchool) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchAbsentTeacher(queryStr, mapParams, request, idSchool);
        return countAll(queryStr.toString(), mapParams);
    }

    @Override
    public List<AbsentTeacher> searchAbsentTeacherMobile(PageNumberRequest request, Long idSchool, Long idInfoEmployee) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school =:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("and id_info_employee =:idInfoEmployee ");
        mapParams.put("idInfoEmployee", idInfoEmployee);
        queryStr.append("order by created_date desc");
        return findAllMobilePaging(queryStr.toString(), mapParams, request.getPageNumber());
    }

    @Override
    public List<AbsentTeacher> searchAbsentTeacherPlus(SearchAbsentTeacherPlusRequest request, Long idSchool) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();

        queryStr.append("and id_school =:idSchool ");
        mapParams.put("idSchool", idSchool);
        if(request.getAbsentDate() != null){
            queryStr.append("and to_date >= :absentDate and from_date <= :absentDate ");
            mapParams.put("absentDate", request.getAbsentDate());
        }
        if (StringUtils.isNotBlank(request.getDateDetail())) {
            queryStr.append("and to_date >=:dateDetail and from_date <=:dateDetail ");
            mapParams.put("dateDetail", LocalDate.now());
        }
        if (request.getConfirmStatus() != null) {
            queryStr.append("and confirm_status =:confirmStatus ");
            mapParams.put("confirmStatus", request.getConfirmStatus());
        }
        if (StringUtils.isNotBlank(request.getTeacherName())) {
            request.setTeacherName(request.getTeacherName().trim());
            queryStr.append("and EXISTS (SELECT * FROM info_employee_school as model1 WHERE model.id_info_employee = model1.id and full_name like :teacherName) ");
            mapParams.put("teacherName", "%" + request.getTeacherName() + "%");
        }
        queryStr.append("and EXISTS (SELECT * FROM info_employee_school as model1 WHERE model.id_info_employee = model1.id and del_active=true) ");
        queryStr.append("order by created_date desc");
        return findAllMobilePaging(queryStr.toString(), mapParams, request.getPageNumber());
    }

    private void setSearchAbsentTeacher(StringBuilder queryStr, Map<String, Object> mapParams, SearchAbsentTeacherRequest request, Long idSchool) {
        queryStr.append("and id_school =:idSchool ");
        mapParams.put("idSchool", idSchool);
        if (request.getCreatedDate() != null) {
            queryStr.append("and date(created_date)=:createdDate ");
            mapParams.put("createdDate", request.getCreatedDate());
        }
        if (StringUtils.isNotBlank(request.getContent())) {
            queryStr.append("and content like :content ");
            mapParams.put("content", "%" + request.getContent() + "%");
        }
        if (request.getDate() != null) {
            queryStr.append("and to_date >= :dateSick and from_date <= :dateSick ");
            mapParams.put("dateSick", request.getDate());
        }
        if (request.getConfirmStatus() != null) {
            queryStr.append("and confirm_status =:confirmStatus ");
            mapParams.put("confirmStatus", request.getConfirmStatus());
        }
        queryStr.append("and EXISTS (SELECT * FROM info_employee_school as model1 WHERE model.id_info_employee = model1.id and del_active=true) ");
        queryStr.append("order by created_date desc");
    }
}
