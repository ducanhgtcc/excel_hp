package com.example.onekids_project.repository;

import com.example.onekids_project.entity.classes.ClassSchedule;
import com.example.onekids_project.entity.classes.ScheduleEvening;
import com.example.onekids_project.entity.classes.ScheduleMorning;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ScheduleMorningRepository extends JpaRepository<ScheduleMorning, Long>{
    void deleteByClassSchedule(ClassSchedule classSchedule);

    @Modifying
    @Query("delete from ScheduleMorning sm where sm.classSchedule.id=:idClass")
    void deleteByIdClass(@Param("idClass") Long idClass);
}
