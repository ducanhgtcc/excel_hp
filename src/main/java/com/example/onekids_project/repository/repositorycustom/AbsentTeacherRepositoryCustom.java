package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.employee.AbsentTeacher;
import com.example.onekids_project.mobile.plus.request.absentteacher.SearchAbsentTeacherPlusRequest;
import com.example.onekids_project.mobile.request.PageNumberRequest;
import com.example.onekids_project.request.absentteacher.SearchAbsentTeacherRequest;

import java.time.LocalDate;
import java.util.List;

/**
 * date 2021-05-21 2:35 PM
 *
 * @author nguyễn văn thụ
 */
public interface AbsentTeacherRepositoryCustom {

    List<AbsentTeacher> searchAbsentTeacher(SearchAbsentTeacherRequest searchAbsentTeacherRequest, Long idSchool);

    List<AbsentTeacher> findFromDate(Long idInfoEmployee, LocalDate fromDate, LocalDate toDate);

    long countTotalAccount(SearchAbsentTeacherRequest request, Long idSchool);

    List<AbsentTeacher> searchAbsentTeacherMobile(PageNumberRequest request, Long idSchool, Long idInfoEmployee);

    List<AbsentTeacher> searchAbsentTeacherPlus(SearchAbsentTeacherPlusRequest request, Long idSchool);
}
