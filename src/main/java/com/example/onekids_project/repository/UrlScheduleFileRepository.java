package com.example.onekids_project.repository;

import com.example.onekids_project.entity.classes.ScheduleFile;
import com.example.onekids_project.entity.classes.UrlScheuldeFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface UrlScheduleFileRepository extends JpaRepository<UrlScheuldeFile, Long> {
//    List<ScheduleFile> findByMaClassIdAndFromFileTsimeAndToFileTsime(Long idClass, LocalDate fromFileTsime,LocalDate toFileTsime);
    @Modifying
    @Query(value = "delete from ma_url_schedule_file where id=:id", nativeQuery = true)
    void deleteUrlFile(Long id);


}
