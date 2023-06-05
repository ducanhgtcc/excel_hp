package com.example.onekids_project.repository;

import com.example.onekids_project.entity.classes.ClassSchedule;
import com.example.onekids_project.repository.repositorycustom.ClassScheduleRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ClassScheduleRepository extends JpaRepository<ClassSchedule, Long>, ClassScheduleRepositoryCustom {
    //ist<ClassSchedule> findByScheduleDateGreaterThanEqualAndScheduleDateLessThanEqualAndDelActiveTrue(LocalDate startTimeSchedule,LocalDate endTimeSchedule );
    Optional<ClassSchedule> findByIdAndDelActiveTrue(Long idClassSchedule);

    boolean existsByMaClassIdAndMaClassDelActiveTrueAndScheduleDateIn(Long idClass, List<LocalDate> localDateScheuduleList);

    void deleteByMaClassIdAndMaClassDelActiveTrueAndScheduleDateIn(Long idClass, List<LocalDate> localDateScheuduleList);

    List<ClassSchedule> findByMaClassIdAndMaClassDelActiveTrueAndScheduleDateIn(Long idClass, List<LocalDate> localDateScheuduleList);

    List<ClassSchedule> findByMaClassIdAndMaClassDelActiveTrueAndScheduleDateBetween(Long idClass, LocalDate start, LocalDate end);

    List<ClassSchedule> findByIsApprovedTrueAndMaClass_IdAndScheduleDateAndDelActiveTrue(Long idClass, LocalDate date);

    List<ClassSchedule> findByDelActiveTrueAndMaClass_IdAndScheduleDateBetween(Long idClass, LocalDate start, LocalDate end);

    ClassSchedule findByMaClassIdAndDelActiveTrueAndScheduleDate(Long idClass, LocalDate monday);

    List<ClassSchedule> findDistinctByMaClassIdAndMaClassDelActiveTrueAndScheduleDate(Long idClass, LocalDate scheduleDate);

}
