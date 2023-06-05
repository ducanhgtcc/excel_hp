package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.employee.AttendanceEmployee;
import com.example.onekids_project.repository.repositorycustom.AttendanceEmployeeRepositoryCustom;
import org.apache.commons.collections4.CollectionUtils;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * date 2021-03-09 3:42 CH
 *
 * @author ADMIN
 */
public class AttendanceEmployeeRepositoryImpl extends BaseRepositoryimpl<AttendanceEmployee> implements AttendanceEmployeeRepositoryCustom {

    @Override
    public List<AttendanceEmployee> searchAttendanceEmployeeMonth(Long idSchool, LocalDate date) {
        StringBuilder quertStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        quertStr.append(" and month(date)=:month ");
        mapParams.put("month", date.getMonthValue());
        quertStr.append("and year(date)=:year ");
        mapParams.put("year", date.getYear());
        quertStr.append("and exists(select*from info_employee_school as model1 where model1.id=model.id_info_employee and model1.id_school=:idSchool) ");
        mapParams.put("idSchool", idSchool);
        return findAllNoPaging(quertStr.toString(), mapParams);
    }

    @Override
    public List<AttendanceEmployee> searchAttendanceEmployeeChart(Long idSchool, Long idDepartment, List<LocalDate> dateList) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (CollectionUtils.isNotEmpty(dateList)){
            queryStr.append(" and date in :dateList ");
            mapParams.put("dateList", dateList);
        }
        if (idDepartment != null){
            queryStr.append("and exists(select*from ex_department_employee as model2 where model2.id_info_employee=model.id_info_employee and model2.id_department=:id_department) ");
            mapParams.put("id_department", idDepartment);
        }
        queryStr.append("and exists(select*from info_employee_school as model1 where model1.id=model.id_info_employee and model1.id_school=:idSchool) ");
        mapParams.put("idSchool", idSchool);
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

}
