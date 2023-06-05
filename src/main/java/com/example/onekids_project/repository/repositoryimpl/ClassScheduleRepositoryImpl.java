package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.classes.ClassSchedule;
import com.example.onekids_project.repository.repositorycustom.ClassScheduleRepositoryCustom;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassScheduleRepositoryImpl extends BaseRepositoryimpl<ClassSchedule> implements ClassScheduleRepositoryCustom {

    @Override
    public ClassSchedule findScheduleDate(Long idSchool, Long idClass, LocalDate localDate) {
        ClassSchedule classSchedule = new ClassSchedule();
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (idSchool != null) {     // đặt tên theo idSchool truyền vào
            queryStr.append("and id_school=:idSchool ");    // đặt tên theo idschool giống tên trường trong database,
            mapParams.put("idSchool", idSchool); // đặt tên theo idSchool truyền vào
        }
        if (idClass != null) {
            queryStr.append("and id_class =:idClass ");
            mapParams.put("idClass", idClass);
        }
        if (localDate != null) {
            queryStr.append("and schedule_date =:localDate ");
            mapParams.put("localDate", localDate);
        }
        queryStr.append("and is_approved =true ");
        List<ClassSchedule> classScheduleList = findAllNoPaging(queryStr.toString(), mapParams);
        if (CollectionUtils.isEmpty(classScheduleList)) {
            return null;
        }
        return classScheduleList.get(0);
    }

    @Override
    public List<ClassSchedule> findClassScheduleMonthList(Long idSchool, Long idClass, Integer month, Integer year) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        if (idClass != null) {
            queryStr.append("and id_class =:idClass ");
            mapParams.put("idClass", idClass);
        }
        if (month != null) {
            queryStr.append("and month(schedule_date) =:month ");
            mapParams.put("month", month);
        }
        if (month != null) {
            queryStr.append("and year(schedule_date) =:year ");
            mapParams.put("year", year);
        }
        queryStr.append("and is_approved =true ");
        List<ClassSchedule> classScheduleList = findAllNoPaging(queryStr.toString(), mapParams);
        return classScheduleList;
    }
}
