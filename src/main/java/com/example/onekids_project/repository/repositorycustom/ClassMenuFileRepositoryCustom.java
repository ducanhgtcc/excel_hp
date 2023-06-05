package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.classes.ManuFile;
import com.example.onekids_project.entity.classes.UrlMenuFile;

import java.time.LocalDate;
import java.util.List;

public interface ClassMenuFileRepositoryCustom {
    ManuFile searchMenuImageWeek(Long idSchool, Long idClass, LocalDate localDate);

    List<ManuFile> searchMenuFile(Long idSchool, Long idClass, LocalDate localDate);
    List<ManuFile> searchMenuFilePageNumber(Long idSchool, Long idClass, Integer pageNumber);

    long countMenuFile(Long idSchool, Long idClass, LocalDate localDate);

    ManuFile findMenuFile(Long idSchool, Long idClass, LocalDate monday);


    List<ManuFile> findManuFile(Long idSchool, Long idClass, LocalDate monday);
}
