package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.classes.ClassMenu;
import com.example.onekids_project.entity.classes.ManuFile;
import com.example.onekids_project.entity.classes.ScheduleFile;

import java.time.LocalDate;
import java.util.List;

public interface ClassMenuRepositoryCustom {
    ClassMenu searchDateMenu(Long idSchool, Long idClass, LocalDate date);
    List<ClassMenu> searchClassMenuMonthList(Long idSchool, Long idClass, Integer month, Integer year);
    List<ClassMenu> searchClassMenuWeekList(Long idSchool, Long idClass, LocalDate date);
}
