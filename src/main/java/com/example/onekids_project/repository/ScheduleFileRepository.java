package com.example.onekids_project.repository;

import com.example.onekids_project.entity.classes.ScheduleFile;
import com.example.onekids_project.repository.repositorycustom.ScheduleFileRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleFileRepository extends JpaRepository<ScheduleFile, Long>, ScheduleFileRepositoryCustom {
    List<ScheduleFile> findByMaClassIdAndFromFileTsimeAndToFileTsime(Long idClass, LocalDate fromFileTsime, LocalDate toFileTsime);

    @Modifying
    @Query(value = "delete from ex_schedule_file_url where id_url_schedule_file=:id", nativeQuery = true)
    void deleteExUrlFile(Long id);
}
