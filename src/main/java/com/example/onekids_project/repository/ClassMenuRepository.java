package com.example.onekids_project.repository;

import com.example.onekids_project.entity.classes.ClassMenu;
import com.example.onekids_project.repository.repositorycustom.ClassMenuRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ClassMenuRepository extends JpaRepository<ClassMenu, Long>, ClassMenuRepositoryCustom {
    List<ClassMenu> findByMaClassIdAndMaClassDelActiveTrueAndMenuDateBetween(Long idClass, LocalDate startMenu, LocalDate endMenu);

    List<ClassMenu> findByDelActiveTrueAndMaClass_IdAndMenuDateBetween(Long idClass, LocalDate startMenu, LocalDate endMenu);

    List<ClassMenu> findByDelActiveTrueAndMaClass_IdAndMenuDateAndIsApprovedTrue(Long idClass, LocalDate date);

    Optional<ClassMenu> findByIdAndDelActiveTrue(Long idClassSchedule);

    boolean existsByMaClassIdAndMaClassDelActiveTrueAndMenuDateIn(Long idClass, List<LocalDate> listDateDayInWeek);

    List<ClassMenu> findByMaClassIdAndMaClassDelActiveTrueAndMenuDateIn(Long idClass, List<LocalDate> listDateDayInWeek);

    void deleteByMaClassIdAndMaClassDelActiveTrueAndMenuDateIn(Long idClass, List<LocalDate> listDateDayInWeek);

    List<ClassMenu> findDistinctByMaClassIdAndMaClassDelActiveTrueAndMenuDate(Long idClass, LocalDate timeMenuDate);
}
