package com.example.onekids_project.repository;

import com.example.onekids_project.entity.kids.AttendanceLeaveKids;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface AttendanceLeaveKidsRepository extends JpaRepository<AttendanceLeaveKids, Long> {
    @Modifying
    @Query(value = "delete from attendance_leave_kids where id=:id", nativeQuery = true)
    void deleteByIdCustom(Long id);

    AttendanceLeaveKids findByIdAndDelActiveTrue(Long idLeave);
}
