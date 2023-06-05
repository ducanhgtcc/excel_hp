package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.classes.ClassSchedule;

import java.time.LocalDate;
import java.util.List;

public interface ClassScheduleRepositoryCustom {
    /**
     * tìm thời khóa biểu ngày
     * @param idSchool
     * @param idClass
     * @param localDate
     * @return
     */
    ClassSchedule findScheduleDate(Long idSchool, Long idClass, LocalDate localDate);

    List<ClassSchedule> findClassScheduleMonthList(Long idSchool, Long idClass, Integer month, Integer year);

}
