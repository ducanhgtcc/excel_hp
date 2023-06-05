package com.example.onekids_project.repository;

import com.example.onekids_project.entity.classes.UrlMenuFile;
import com.example.onekids_project.entity.classes.UrlScheuldeFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UrlMenuFileRepository extends JpaRepository<UrlMenuFile, Long> {
//    List<ScheduleFile> findByMaClassIdAndFromFileTsimeAndToFileTsime(Long idClass, LocalDate fromFileTsime,LocalDate toFileTsime);

    @Modifying
    @Query(value = "delete from ma_url_menu_file where id=:id", nativeQuery = true)
    void deleteUrlFile(Long id);
}
