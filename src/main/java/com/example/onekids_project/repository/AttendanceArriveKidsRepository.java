package com.example.onekids_project.repository;

import com.example.onekids_project.entity.kids.AttendanceArriveKids;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceArriveKidsRepository extends JpaRepository<AttendanceArriveKids, Long> {

    @Modifying
    @Query(value = "delete from attendance_arrive_kids where id=:id", nativeQuery = true)
    void deleteByIdCustom(Long id);

    AttendanceArriveKids findByIdAndDelActiveTrue(Long idArrive);
}
