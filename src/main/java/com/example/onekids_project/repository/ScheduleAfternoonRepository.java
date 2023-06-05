package com.example.onekids_project.repository;

import com.example.onekids_project.entity.classes.ClassSchedule;
import com.example.onekids_project.entity.classes.ScheduleAfternoon;
import com.example.onekids_project.entity.classes.ScheduleEvening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ScheduleAfternoonRepository extends JpaRepository<ScheduleAfternoon, Long>{
    void deleteByClassScheduleId(Long idClassSchedule);
    void deleteByClassSchedule (ClassSchedule classSchedule);
    @Modifying
    @Query("delete from ScheduleAfternoon sm where sm.classSchedule.id=:idClass")
    void deleteByIdClass(@Param("idClass") Long idClass);
}
