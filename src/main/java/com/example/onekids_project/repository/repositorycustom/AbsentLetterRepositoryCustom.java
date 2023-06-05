package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.kids.AbsentLetter;
import com.example.onekids_project.mobile.plus.request.SearchMessagePlusRequest;
import com.example.onekids_project.mobile.plus.request.absent.SearchAbsentPlusRequest;
import com.example.onekids_project.mobile.teacher.request.absent.SearchAbsentTeacherRequest;
import com.example.onekids_project.request.parentdiary.SearchAbsentLetterRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AbsentLetterRepositoryCustom {

    List<AbsentLetter> findAllAbsent(Long idSchoolLogin, Pageable pageable);

    List<AbsentLetter> searchAbsent(Long idSchool, SearchAbsentLetterRequest searchAbsentLetterRequest);

    Optional<AbsentLetter> findByIdAbsent(Long idSchoolLogin, Long id);

    List<AbsentLetter> findAbsentMobile(Long idSchool, Long idKid, Pageable pageable, LocalDateTime localDateTime);

    Long getCountMessage(Long idSchool, Long idKid, LocalDateTime localDateTime);

    List<AbsentLetter> findFromDate(Long idkid, LocalDate fromdate);

    List<AbsentLetter> findMonthForAttendance(Long idSchool, Long idKid, Integer month, Integer year);

    List<AbsentLetter> findAbsentForTeacher(Long idSchool, Long idClass, SearchAbsentTeacherRequest searchAbsentTeacherRequest);

    long countTotalAccount(Long idSchool,SearchAbsentLetterRequest request);

    List<AbsentLetter> findAbsentInClassDate(Long idSchool, Long idClass, LocalDate date);

    List<AbsentLetter> searchAbsentForPlus(Long idSchool, SearchAbsentPlusRequest request);
}


