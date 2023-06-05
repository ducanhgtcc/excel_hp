package com.example.onekids_project.repository;

import com.example.onekids_project.entity.employee.AbsentTeacher;
import com.example.onekids_project.repository.repositorycustom.AbsentTeacherRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * date 2021-05-21 2:40 PM
 *
 * @author nguyễn văn thụ
 */
public interface AbsentTeacherRepository extends JpaRepository<AbsentTeacher, Long>, AbsentTeacherRepositoryCustom {

    Optional<AbsentTeacher> findByIdAndDelActiveTrue(Long id);

    Optional<AbsentTeacher> findByIdAndSchoolReadFalseAndDelActiveTrue(Long id);

    Optional<AbsentTeacher> findByIdAndConfirmStatusFalseAndDelActiveTrue(Long id);

    int countByInfoEmployeeSchoolIdAndTeacherReadFalseAndDelActiveTrue(Long idInfoEmployee);

    int countByIdSchoolAndConfirmStatusFalseAndInfoEmployeeSchoolDelActiveTrueAndDelActiveTrue(Long idSchool);
}
