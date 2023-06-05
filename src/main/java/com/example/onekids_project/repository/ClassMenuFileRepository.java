package com.example.onekids_project.repository;

import com.example.onekids_project.entity.classes.ManuFile;
import com.example.onekids_project.repository.repositorycustom.ClassMenuFileRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ClassMenuFileRepository extends JpaRepository<ManuFile, Long>, ClassMenuFileRepositoryCustom {
    List<ManuFile> findByMaClassIdAndFromFileTimeAndToFileTime(Long idClass, LocalDate fromFileTsime, LocalDate toFileTsime);

    @Modifying
    @Query(value = "delete from ex_menu_file_url where id_url_meunu_file=:id", nativeQuery = true)
    void deleteExUrlFile(Long id);
}
