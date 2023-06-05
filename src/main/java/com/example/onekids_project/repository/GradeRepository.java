package com.example.onekids_project.repository;

import com.example.onekids_project.entity.school.Grade;
import com.example.onekids_project.repository.repositorycustom.GradeRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GradeRepository extends JpaRepository<Grade, Long>, GradeRepositoryCustom {

    Optional<Grade> findByGradeNameAndDelActiveTrueAndSchool_Id(String nameGrade, Long id);

    Optional<Grade> findByDelActiveTrueAndId(Long id);

    List<Grade> findByDelActiveTrueAndSchool_Id(Long idSchool);

    List<Grade> findByIdIn(List<Long> idGradeList);
}
