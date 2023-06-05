package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.parent.Parent;
import com.example.onekids_project.request.base.BaseRequest;
import com.example.onekids_project.request.base.PageNumberWebRequest;
import com.example.onekids_project.request.birthdaymanagement.SearchParentBirthDayRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ParentRepositoryCustom {

    Optional<Parent> findByIdParent(Long id, boolean appTrue);

    List<Parent> searchParentBirthday(Long idSchoolLogin, SearchParentBirthDayRequest searchParentBirthDayRequest);

    List<Parent> findAllParentBirthday(Long idSchoolLogin, PageNumberWebRequest request);

    List<Parent> findAllParentAppSend(List<Long> idSchoolList);

    List<Parent> findAllParentStudent(List<Long> idKidsList);

    List<Parent> findAllParentGrade(List<Long> idGradeList);

    List<Parent> findAllParentClass(List<Long> idClassList);

    List<Parent> findAllParentGroup(List<Long> idClassList);

    List<Parent> findParentAllBirthdayNoSchool(LocalDate localDate);
}
