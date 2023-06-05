package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.classes.ManuFile;
import com.example.onekids_project.entity.classes.ScheduleFile;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleFileRepositoryCustom {
    ScheduleFile searchScheduleImageWeek(Long idSchool, Long idClass, LocalDate localDate);

//    List<ScheduleFile> searchScheduleFile(Long idSchool, Long idClass, Long id);
//
//    long countScheduleFile(Long idSchool, Long idClass, Long id);

    List<ScheduleFile> findScheduleFile(Long idSchool, Long idClass, LocalDate monday);
    List<ScheduleFile> searchScheduleFilePageNumber(Long idSchool, Long idClass, Integer pageNumber);

    List<ScheduleFile> searchScheduleFile(Long idSchool, Long idClass, LocalDate localDate);
    long countScheduleFile(Long idSchool, Long idClass, LocalDate localDate);

}
