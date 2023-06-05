package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.common.EmployeeConstant;
import com.example.onekids_project.common.SystemConstant;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.master.request.employee.EmployeeSearchAdminRequest;
import com.example.onekids_project.repository.repositorycustom.InfoEmployeeSchoolRepositoryCustom;
import com.example.onekids_project.request.birthdaymanagement.SearchTeacherBirthDayRequest;
import com.example.onekids_project.request.employee.*;
import com.example.onekids_project.request.kids.ExcelGroupOutRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InfoEmployeeSchoolRepositoryImpl extends BaseRepositoryimpl<InfoEmployeeSchool> implements InfoEmployeeSchoolRepositoryCustom {


    @Override
    public List<InfoEmployeeSchool> search(Long idSchool, EmployeeSearchNew search) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchEmployee(queryStr, mapParams, idSchool, search);
        return findAllWebPaging(queryStr.toString(), mapParams, search.getPageNumber(), search.getMaxPageItem());
    }


    @Override
    public long countEmployee(Long idSchool, EmployeeSearchNew search) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchEmployee(queryStr, mapParams, idSchool, search);
        return countAll(queryStr.toString(), mapParams);
    }

    @Override
    public List<InfoEmployeeSchool> searchPlus(Long idSchool, EmployeePlusSearchNew search) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchEmployeePlus(queryStr, mapParams, idSchool, search);
        return findAllWebPagingDeleteOrNot(queryStr.toString(), mapParams, search.getPageNumber(), search.getMaxPageItem(), search.isDeleteStatus());
    }

    @Override
    public long countEmployeePlus(Long idSchool, EmployeePlusSearchNew search) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchEmployeePlus(queryStr, mapParams, idSchool, search);
        return countAllDeleteOrNot(queryStr.toString(), mapParams, search.isDeleteStatus());
    }

    @Override
    public List<InfoEmployeeSchool> searchEmployeeAdmin(EmployeeSearchAdminRequest search, List<Long> idSchoolList) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchEmployeeAdmin(queryStr, mapParams, search, idSchoolList);
        return findAllWebPagingDeleteOrNot(queryStr.toString(), mapParams, search.getPageNumber(), search.getMaxPageItem(), search.isDeleteStatus());
    }

    @Override
    public long countEmployeeAdmin(EmployeeSearchAdminRequest search, List<Long> idSchoolList) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchEmployeeAdmin(queryStr, mapParams, search, idSchoolList);
        return countAllDeleteOrNot(queryStr.toString(), mapParams, search.isDeleteStatus());
    }


    @Override
    public List<InfoEmployeeSchool> findAllInfoEmployeeSchool(SearchInfoEmployeeRequest searchInfoEmployeeRequest, Pageable pageable, Long idSchool) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (searchInfoEmployeeRequest != null) {
            queryStr.append(" and model.app_type =:appType ");
            mapParams.put("appType", AppTypeConstant.TEACHER);
            if (StringUtils.isNotBlank(searchInfoEmployeeRequest.getEmployeeNameOrPhone())) {
                queryStr.append("and (full_name like :fullName");
                mapParams.put("fullName", "%" + searchInfoEmployeeRequest.getEmployeeNameOrPhone().trim() + "%");
                queryStr.append(" or phone like:phone)");
                mapParams.put("phone", "%" + searchInfoEmployeeRequest.getEmployeeNameOrPhone().trim() + "%");
            }
            if (StringUtils.isNotBlank(searchInfoEmployeeRequest.getEmployeeStatus())) {
                queryStr.append(" and employee_status =:employeeStatus");
                mapParams.put("employeeStatus", searchInfoEmployeeRequest.getEmployeeStatus());
            }
            if (idSchool != null) {
                queryStr.append(" and id_school =:idSchool");
                mapParams.put("idSchool", idSchool);
            }
            if (searchInfoEmployeeRequest.getIdDepartment() != null) {
                queryStr.append(" and EXISTS (Select*from ex_department_employee b where model.id=b.id_info_employee and b.id_department=:idDepartment )");
                mapParams.put("idDepartment", searchInfoEmployeeRequest.getIdDepartment());
            }
        }
        queryStr.append(" order by lower(first_name) ASC");

        return findAll(queryStr.toString(), mapParams, pageable);
    }

    /**
     * Tìm kiếm  tất cả các InfoEmployee (có thể lọc theo từng field)
     *
     * @param searchExportEmployeeRequest
     * @return
     */

    @Override
    public List<InfoEmployeeSchool> findAllInfoEmployeeSchoolByDepartment(Long idSchool, SearchExportEmployeeRequest searchExportEmployeeRequest) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (searchExportEmployeeRequest != null) {

//            if (StringUtils.isNotBlank(searchExportEmployeeRequest.getEmployeeStatus())) {
//                queryStr.append(" and employee_status like :employeeStatus");
//                mapParams.put("employeeStatus", "%" + searchExportEmployeeRequest.getEmployeeStatus() + "%");
//            }
            if (idSchool != null) {
                queryStr.append(" and id_school =:idSchool");
                mapParams.put("idSchool", idSchool);
            }
            if (searchExportEmployeeRequest.getIdDepartment() != null) {
                queryStr.append(" and EXISTS (Select*from ex_department_employee b where model.id=b.id_info_employee and b.id_department=:idDepartment )");
                mapParams.put("idDepartment", searchExportEmployeeRequest.getIdDepartment());
            }
        }
        queryStr.append(" order by full_name ASC");

        return findAllNoPaging(queryStr.toString(), mapParams);

    }

    @Override
    public List<InfoEmployeeSchool> findAllInfoEmployeeSchoolAppSend(List<Long> idSchoolList, List<Long> idClassList) {
        StringBuilder quertStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (!CollectionUtils.isEmpty(idSchoolList)) {
            quertStr.append(" and id_school in (:idSchoolStr)");
            mapParams.put("idSchoolStr", idSchoolList);
        }
        if (!CollectionUtils.isEmpty(idClassList)) {
            quertStr.append(" and exists(select*from ex_employee_class ec where info_employee_school.id=ec.id_info_employee and ec.id_class in :idClassList)");
            mapParams.put("idClassList", idClassList);
        }
        return findAllNoPaging(quertStr.toString(), mapParams);
    }

    @Override
    public List<InfoEmployeeSchool> findContactTeacher(Long idSchool, Integer pageNumber) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        queryStr.append("and app_type=:appType ");
        mapParams.put("appType", AppTypeConstant.TEACHER);
        queryStr.append("and id_employee is not null ");
        queryStr.append("and employee_status=:employeeStatus ");
        mapParams.put("employeeStatus", EmployeeConstant.STATUS_WORKING);
        queryStr.append("order by first_name collate utf8_vietnamese_ci ");
        return findAllMobilePaging(queryStr.toString(), mapParams, pageNumber);
    }

    @Override
    public List<InfoEmployeeSchool> findInfoEmployeeDeparmentList(Long idSchool, List<Long> dataDepartmentNotifyList) {

        StringBuilder quertStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (idSchool != null) {
            quertStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        if (!CollectionUtils.isEmpty(dataDepartmentNotifyList)) {
            quertStr.append(" and exists(select*from ex_department_employee as model1 where model.id = model1.id_info_employee and model1.id_department in :dataDepartmentNotifyList) ");
            mapParams.put("dataDepartmentNotifyList", dataDepartmentNotifyList);
        }
        return findAllNoPaging(quertStr.toString(), mapParams);
    }

    @Override
    public List<InfoEmployeeSchool> findInfoEmployeeSchool(Long idSchool, List<Long> dataEmployeeNotifyList) {
        StringBuilder quertStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (idSchool != null) {
            quertStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        if (!CollectionUtils.isEmpty(dataEmployeeNotifyList)) {
            quertStr.append(" and model.id in :dataEmployeeNotifyList ");
            mapParams.put("dataEmployeeNotifyList", dataEmployeeNotifyList);
        }
        return findAllNoPaging(quertStr.toString(), mapParams);
    }

    @Override
    public List<InfoEmployeeSchool> searchEmplyeenew(Long idSchool, SearchTeacherBirthDayRequest searchTeacherBirthDayRequest) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (searchTeacherBirthDayRequest != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
            queryStr.append("and app_type='teacher' ");
            if (searchTeacherBirthDayRequest.getDate() != null) {
                queryStr.append("and birthday=:birthDay ");
                mapParams.put("birthDay", searchTeacherBirthDayRequest.getDate());
            }
            if (searchTeacherBirthDayRequest.getWeek() != null) {
                queryStr.append("and birthday >=:monday ");
                mapParams.put("monday", searchTeacherBirthDayRequest.getWeek().toString());
                queryStr.append(" and birthday <=:sunday ");
                mapParams.put("sunday", searchTeacherBirthDayRequest.getWeek().plusDays(6).toString());
            }
            if (searchTeacherBirthDayRequest.getMonth() != null) {
                queryStr.append("and month(birthday) =:month ");
                mapParams.put("month", searchTeacherBirthDayRequest.getMonth().getMonthValue());
            }
            if (StringUtils.isNotBlank(searchTeacherBirthDayRequest.getName())) {
                queryStr.append("and full_name like :fullName ");
                mapParams.put("fullName", "%" + searchTeacherBirthDayRequest.getName() + "%");
            }
            queryStr.append("order by first_name asc ");
        }
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<InfoEmployeeSchool> searchEmplyeenewa(Long idSchool, SearchTeacherBirthDayRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchTeacherBirthday(idSchool, request, queryStr, mapParams);
        return findAllWebPaging(queryStr.toString(), mapParams, request.getPageNumber(), request.getMaxPageItem());
    }

    @Override
    public List<InfoEmployeeSchool> findEmployeeAllBirthdayNoSchool(LocalDate localDate) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and employee_status =:status ");
        mapParams.put("status", AppConstant.EMPLOYEE_STATUS_WORKING);
        queryStr.append("and activated=true ");
        queryStr.append("and day(birthday) =:day ");
        mapParams.put("day", localDate.getDayOfMonth());
        queryStr.append("and month(birthday) =:month ");
        mapParams.put("month", localDate.getMonthValue());
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public long countSearchTeacherBirthday(Long idSchool, SearchTeacherBirthDayRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchTeacherBirthday(idSchool, request, queryStr, mapParams);
        return countAll(queryStr.toString(), mapParams);
    }

    @Override
    public List<InfoEmployeeSchool> searchEmployeeSaraly(Long idSchool, String employeeStatus, Long idDepartment, String nameOrPhone, LocalDate date) {

        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school =:idSchool ");
        mapParams.put("idSchool", idSchool);
        if (idDepartment != null) {
            queryStr.append("and EXISTS (Select*from ex_department_employee b where model.id=b.id_info_employee and b.id_department=:idDepartment) ");
            mapParams.put("idDepartment", idDepartment);
        }
        queryStr.append("and app_type =:appType ");
        mapParams.put("appType", AppTypeConstant.TEACHER);
        if (StringUtils.isNotBlank(nameOrPhone)) {
            queryStr.append("and (full_name like :fullName ");
            mapParams.put("fullName", "%" + nameOrPhone.trim() + "%");
            queryStr.append("or phone like:phone) ");
            mapParams.put("phone", "%" + nameOrPhone.trim() + "%");
        }
//        queryStr.append("and id in (select distinct id_info_employee from employee_status_timeline as model2 where model2.start_date <= :date and model2.status = :type and id in(SELECT max(id) FROM employee_status_timeline  GROUP BY id_info_employee)) ");
        queryStr.append("and exists(select * from employee_status_timeline as model1 where model.id =  model1.id_info_employee and model1.status =:status  and model1.start_date <=:date and case when end_date is not null then end_date>=:date else true end) ");
        mapParams.put("date", date);
        mapParams.put("status", EmployeeConstant.STATUS_WORKING);
        queryStr.append("order by lower(first_name) ASC");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<InfoEmployeeSchool> searchEmployeeSalaryNew(Long idSchool, String employeeStatus, Long idDepartment, String nameOrPhone, List<Long>... idList) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school =:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("and app_type =:appType ");
        mapParams.put("appType", AppTypeConstant.TEACHER);
        queryStr.append("and employee_status =:employeeStatus ");
        mapParams.put("employeeStatus", employeeStatus);
        if (idList.length > 0) {
            List<Long> list = new ArrayList<>();
            for (List<Long> longList : idList) {
                list = longList;
            }
            if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(list)) {
                queryStr.append("and id in :idList ");
                mapParams.put("idList", list);
            }
        }
        if (idDepartment != null) {
            queryStr.append("and EXISTS (Select*from ex_department_employee b where model.id=b.id_info_employee and b.id_department=:idDepartment) ");
            mapParams.put("idDepartment", idDepartment);
        }
        if (StringUtils.isNotBlank(nameOrPhone)) {
            queryStr.append("and (full_name like :fullName ");
            mapParams.put("fullName", "%" + nameOrPhone.trim() + "%");
            queryStr.append("or phone like:phone) ");
            mapParams.put("phone", "%" + nameOrPhone.trim() + "%");
        }
        queryStr.append("order by first_name collate utf8_vietnamese_ci ");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<InfoEmployeeSchool> searchEmployeeWidthStatus(Long idSchool, String status) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school =:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("and app_type =:appType ");
        mapParams.put("appType", AppTypeConstant.TEACHER);
        if (StringUtils.isNotBlank(status)) {
            queryStr.append("and employee_status =:employeeStatus ");
            mapParams.put("employeeStatus", status);
        }
        queryStr.append("order by first_name collate utf8_vietnamese_ci ");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<InfoEmployeeSchool> findEmployeeRetain(Long idSchool, String employeeStatusRetain) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school =:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("and employee_status =:status ");
        mapParams.put("status", AppConstant.EMPLOYEE_STATUS_RETAIN);
        queryStr.append(" and del_active =:delActive ");
        mapParams.put("delActive", AppConstant.APP_TRUE);
        queryStr.append("order by id_employee is null desc, lower(first_name) ASC");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<InfoEmployeeSchool> findEmployeeLeave(Long idSchool, String employeeStatusLeave) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school =:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("and employee_status =:status ");
        mapParams.put("status", AppConstant.EMPLOYEE_STATUS_LEAVE);
        queryStr.append(" and del_active =:delActive ");
        mapParams.put("delActive", AppConstant.APP_TRUE);
        queryStr.append("order by id_employee is null desc, lower(first_name) ASC");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<InfoEmployeeSchool> findAllInSchool(Long idSchool) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school =:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append(" and del_active =:delActive ");
        mapParams.put("delActive", AppConstant.APP_TRUE);
        queryStr.append("order by id_employee is null desc, lower(first_name) ASC");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<InfoEmployeeSchool> findByEmployeeTimeLineWithDateAndNameOrPhone(Long idSchool, LocalDate date, String nameOrPhone) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school =:idSchool ");
        mapParams.put("idSchool", idSchool);

        queryStr.append("and app_type =:appType ");
        mapParams.put("appType", AppTypeConstant.TEACHER);
        if (StringUtils.isNotBlank(nameOrPhone)) {
            queryStr.append("and (full_name like :fullName ");
            mapParams.put("fullName", "%" + nameOrPhone.trim() + "%");
            queryStr.append("or phone like:phone) ");
            mapParams.put("phone", "%" + nameOrPhone.trim() + "%");
        }

//        queryStr.append("and id in (select distinct id_info_employee from employee_status_timeline as model2 where model2.start_date <= :date and model2.status = :type and id in(SELECT max(id) FROM employee_status_timeline  GROUP BY id_info_employee)) ");
        queryStr.append("and exists(select * from employee_status_timeline as model1 where model.id =  model1.id_info_employee and model1.status =:status  and model1.start_date <=:date and case when end_date is not null then end_date>=:date else true end) ");
        mapParams.put("date", date);
        mapParams.put("status", EmployeeConstant.STATUS_WORKING);
        queryStr.append("order by lower(first_name) ASC");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<InfoEmployeeSchool> findByEmployeeTimeLineWithDate(Long idSchool, LocalDate date) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school =:idSchool ");
        mapParams.put("idSchool", idSchool);

        queryStr.append("and app_type =:appType ");
        mapParams.put("appType", AppTypeConstant.TEACHER);
//        queryStr.append("and id in (select distinct id_info_employee from employee_status_timeline as model2 where model2.start_date <= :date and model2.status = :type and id in(SELECT max(id) FROM employee_status_timeline  GROUP BY id_info_employee)) ");
        queryStr.append("and exists(select * from employee_status_timeline as model1 where model.id =  model1.id_info_employee and model1.status =:status  and model1.start_date <=:date and case when end_date is not null then end_date>=:date else true end) ");
        mapParams.put("date", date);
        mapParams.put("status", EmployeeConstant.STATUS_WORKING);
        queryStr.append("order by lower(first_name) ASC");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<InfoEmployeeSchool> searchGroupOut(Long idSchool, SearchEmployeeGroupOutRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.getEmployeeGroupOut(queryStr, mapParams, request, idSchool);
        return findAllWebPagingDeleteOrNot(queryStr.toString(), mapParams, request.getPageNumber(), request.getMaxPageItem(), AppConstant.APP_FALSE);
    }

    @Override
    public List<InfoEmployeeSchool> searchGroupOutExcel(Long idSchool, ExcelGroupOutRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.getEmployeeGroupOutExcel(queryStr, mapParams, request, idSchool);
        return findAllNoPagingDeleteOrNot(queryStr.toString(), mapParams, AppConstant.APP_FALSE);
    }


    @Override
    public long countGroupOut(Long idSchool, SearchEmployeeGroupOutRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.getEmployeeGroupOut(queryStr, mapParams, request, idSchool);
        return countAllDeleteOrNot(queryStr.toString(), mapParams, AppConstant.APP_FALSE);
    }

    @Override
    public List<InfoEmployeeSchool> getInfoListForCelebrateAuto(Long idSchool, String type, String gender) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and app_type=:appType ");
        mapParams.put("appType", type);
        if (!idSchool.equals(SystemConstant.ID_SYSTEM)) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        if (!gender.equals(AppConstant.ALL)) {
            queryStr.append("and gender=:gender ");
            mapParams.put("gender", gender);
        }
        queryStr.append("and id_employee is not null ");
        queryStr.append("and activated=true ");
        queryStr.append("and employee_status=:status ");
        mapParams.put("status", EmployeeConstant.STATUS_WORKING);
        return findAllNoPaging(queryStr.toString(), mapParams);
    }


    private void setSearchEmployeeAdmin(StringBuilder queryStr, Map<String, Object> mapParams, EmployeeSearchAdminRequest search, List<Long> idSchoolList) {
        queryStr.append("and id_school in (:idSchoolList) ");
        mapParams.put("idSchoolList", idSchoolList);
        if (search.getIdSchool() != null) {
            queryStr.append("and id_school =:idSchool ");
            mapParams.put("idSchool", search.getIdSchool());
        }
        if (StringUtils.isNotBlank(search.getAppType())) {
            queryStr.append("and model.app_type =:appType ");
            mapParams.put("appType", search.getAppType());
        }
        if (StringUtils.isNotBlank(search.getStatus())) {
            queryStr.append("and employee_status =:employeeStatus ");
            mapParams.put("employeeStatus", search.getStatus());
        }
        if (StringUtils.isNotBlank(search.getNameOrPhone())) {
            queryStr.append("and (full_name like :fullName ");
            mapParams.put("fullName", "%" + search.getNameOrPhone().trim() + "%");
            queryStr.append("or phone like:phone) ");
            mapParams.put("phone", "%" + search.getNameOrPhone().trim() + "%");
        }
        queryStr.append(" order by id_employee is null desc, id_school");
    }

    private void setSearchEmployee(StringBuilder queryStr, Map<String, Object> mapParams, Long idSchool, EmployeeSearchNew search) {
        if (idSchool != null) {
            queryStr.append("and id_school =:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        if (search != null) {
            queryStr.append("and model.app_type =:appType ");
            mapParams.put("appType", AppTypeConstant.TEACHER);
            if (StringUtils.isNotBlank(search.getEmployeeNameOrPhone())) {
                queryStr.append("and (full_name like :fullName ");
                mapParams.put("fullName", "%" + search.getEmployeeNameOrPhone().trim() + "%");
                queryStr.append("or phone like:phone) ");
                mapParams.put("phone", "%" + search.getEmployeeNameOrPhone().trim() + "%");
            }
            if (StringUtils.isNotBlank(search.getEmployeeStatus())) {
                queryStr.append("and employee_status =:employeeStatus ");
                mapParams.put("employeeStatus", search.getEmployeeStatus());
            }

            if (search.getIdDepartment() != null) {
                queryStr.append("and EXISTS (Select*from ex_department_employee b where model.id=b.id_info_employee and b.id_department=:idDepartment) ");
                mapParams.put("idDepartment", search.getIdDepartment());
            }
        }
        queryStr.append("order by id_employee is null desc, lower(first_name) ASC");
    }

    private void setSearchEmployeePlus(StringBuilder queryStr, Map<String, Object> mapParams, Long idSchool, EmployeePlusSearchNew search) {
        if (idSchool != null) {
            queryStr.append(" and id_school =:idSchool");
            mapParams.put("idSchool", idSchool);
        }
        if (search != null) {
            queryStr.append(" and model.app_type =:appType ");
            mapParams.put("appType", AppTypeConstant.SCHOOL);
            if (StringUtils.isNotBlank(search.getEmployeeNameOrPhone())) {
                queryStr.append("and (full_name like :fullName");
                mapParams.put("fullName", "%" + search.getEmployeeNameOrPhone().trim() + "%");
                queryStr.append(" or phone like:phone)");
                mapParams.put("phone", "%" + search.getEmployeeNameOrPhone().trim() + "%");
            }
            if (StringUtils.isNotBlank(search.getEmployeeStatus())) {
                queryStr.append(" and employee_status =:employeeStatus");
                mapParams.put("employeeStatus", search.getEmployeeStatus());
            }

            if (search.getIdDepartment() != null) {
                queryStr.append(" and EXISTS (Select*from ex_department_employee b where model.id=b.id_info_employee and b.id_department=:idDepartment )");
                mapParams.put("idDepartment", search.getIdDepartment());
            }
        }
        queryStr.append(" order by id_employee is null desc, lower(first_name) ASC");
    }

    private void setSearchTeacherBirthday(Long idSchool, SearchTeacherBirthDayRequest request, StringBuilder queryStr, Map<String, Object> mapParams) {
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("and app_type='teacher' ");
        if (request.getDate() != null) {
            queryStr.append("and day(birthday) =:day ");
            mapParams.put("day", request.getDate().getDayOfMonth());

            queryStr.append("and month(birthday) =:month ");
            mapParams.put("month", request.getDate().getMonthValue());
        }
        if (request.getWeek() != null) {
            LocalDate day1 = request.getWeek();
            LocalDate day2 = day1.plusDays(1);
            LocalDate day3 = day1.plusDays(2);
            LocalDate day4 = day1.plusDays(3);
            LocalDate day5 = day1.plusDays(4);
            LocalDate day6 = day1.plusDays(5);
            LocalDate day7 = day1.plusDays(6);

            queryStr.append("and ((day(birthday) =:day1 ");
            mapParams.put("day1", day1.getDayOfMonth());
            queryStr.append("and month(birthday) =:month) ");
            mapParams.put("month", day1.getMonthValue());

            queryStr.append("or (day(birthday) =:day2 ");
            mapParams.put("day2", day2.getDayOfMonth());
            queryStr.append("and month(birthday) =:month) ");
            mapParams.put("month", day2.getMonthValue());

            queryStr.append("or (day(birthday) =:day3 ");
            mapParams.put("day3", day3.getDayOfMonth());
            queryStr.append("and month(birthday) =:month) ");
            mapParams.put("month", day3.getMonthValue());

            queryStr.append("or (day(birthday) =:day4 ");
            mapParams.put("day4", day4.getDayOfMonth());
            queryStr.append("and month(birthday) =:month) ");
            mapParams.put("month", day4.getMonthValue());

            queryStr.append("or (day(birthday) =:day5 ");
            mapParams.put("day5", day5.getDayOfMonth());
            queryStr.append("and month(birthday) =:month) ");
            mapParams.put("month", day5.getMonthValue());

            queryStr.append("or (day(birthday) =:day6 ");
            mapParams.put("day6", day6.getDayOfMonth());
            queryStr.append("and month(birthday) =:month) ");
            mapParams.put("month", day6.getMonthValue());

            queryStr.append("or (day(birthday) =:day7 ");
            mapParams.put("day7", day7.getDayOfMonth());
            queryStr.append("and month(birthday) =:month)) ");
            mapParams.put("month", day7.getMonthValue());
        }
        if (request.getMonth() != null) {
            queryStr.append("and month(birthday) =:month ");
            mapParams.put("month", request.getMonth().getMonthValue());
        }
        if (StringUtils.isNotBlank(request.getName())) {
            queryStr.append("and full_name like :fullName ");
            mapParams.put("fullName", "%" + request.getName() + "%");
        }
        queryStr.append("order by first_name asc ");
    }

    private void getEmployeeGroupOut(StringBuilder queryStr, Map<String, Object> mapParams, SearchEmployeeGroupOutRequest request, Long idSchool) {
        LocalDate startYear = LocalDate.of(request.getYearOut().getYear(), 1, 1);
        LocalDate endYear = LocalDate.of(request.getYearOut().getYear(), 12, 31);
        queryStr.append("and id_school =:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("and id_group_out_employee=:idGroupOut ");
        mapParams.put("idGroupOut", request.getIdGroupOut());
        queryStr.append("and out_date >= :startYear and out_date <= :endYear ");
        mapParams.put("startYear", startYear);
        mapParams.put("endYear", endYear);
        if (!CollectionUtils.isEmpty(request.getDateInList())) {
            LocalDate startDate = request.getDateInList().get(0);
            LocalDate endDate = request.getDateInList().get(1);
            queryStr.append("and start_date>=:startDate and start_date<=:endDate ");
            mapParams.put("startDate", startDate);
            mapParams.put("endDate", endDate);
        }
        queryStr.append("order by out_date DESC ");
    }

    private void getEmployeeGroupOutExcel(StringBuilder queryStr, Map<String, Object> mapParams, ExcelGroupOutRequest request, Long idSchool) {
        LocalDate startYear = LocalDate.of(request.getYearOut().getYear(), 1, 1);
        LocalDate endYear = LocalDate.of(request.getYearOut().getYear(), 12, 31);
        queryStr.append("and id_school =:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("and out_date >= :startYear and out_date <= :endYear ");
        mapParams.put("startYear", startYear);
        mapParams.put("endYear", endYear);
        if (!CollectionUtils.isEmpty(request.getDateInList())) {
            LocalDate startDate = request.getDateInList().get(0);
            LocalDate endDate = request.getDateInList().get(1);
            queryStr.append("and start_date>=:startDate and start_date<=:endDate ");
            mapParams.put("startDate", startDate);
            mapParams.put("endDate", endDate);
        }
        if (!CollectionUtils.isEmpty(request.getBirthdayList())) {
            LocalDate startBirthday = request.getBirthdayList().get(0);
            LocalDate endBirthday = request.getBirthdayList().get(1);
            queryStr.append("and birthday>=:startBirthday and birthday<=:endBirthday ");
            mapParams.put("startBirthday", startBirthday);
            mapParams.put("endBirthday", endBirthday);
        }
        queryStr.append("order by out_date DESC ");
    }

}
