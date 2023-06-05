package com.example.onekids_project.repository;

import com.example.onekids_project.entity.kids.AttendanceEatKids;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface AttendanceEatKidsRepository extends JpaRepository<AttendanceEatKids, Long> {
    @Modifying
    @Query(value = "delete from attendance_eat_kids where id=:id", nativeQuery = true)
    void deleteByIdCustom(Long id);

    AttendanceEatKids findByIdAndDelActiveTrue(Long idEat);
}
