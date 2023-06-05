package com.example.onekids_project.repository;

import com.example.onekids_project.entity.parent.Parent;
import com.example.onekids_project.repository.repositorycustom.ParentRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ParentRepository extends JpaRepository<Parent, Long>, ParentRepositoryCustom {
    List<Parent> findByKidsList_IdSchoolAndDelActiveTrue(Long idSchool);

    List<Parent> findByKidsList_MaClass_IdAndDelActiveTrue(Long idClass);


    Optional<Parent> findByIdAndDelActiveTrue(Long id);

    Optional<Parent> findByIdKidLoginAndDelActiveTrue(Long idKid);

    List<Parent> findAllByDelActiveTrueAndBirthday(LocalDate localDate);
}
