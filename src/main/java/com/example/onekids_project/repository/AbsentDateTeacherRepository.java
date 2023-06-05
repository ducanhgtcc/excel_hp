package com.example.onekids_project.repository;

import com.example.onekids_project.entity.employee.AbsentDateTeacher;
import com.example.onekids_project.repository.repositorycustom.AbsentDateTeacherRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * date 2021-05-21 6:25 PM
 *
 * @author nguyễn văn thụ
 */
public interface AbsentDateTeacherRepository extends JpaRepository<AbsentDateTeacher, Long>, AbsentDateTeacherRepositoryCustom {
    List<AbsentDateTeacher> findAllByAbsentTeacherId(Long id);
}
