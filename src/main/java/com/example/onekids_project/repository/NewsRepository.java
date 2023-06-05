package com.example.onekids_project.repository;

import com.example.onekids_project.entity.system.News;
import com.example.onekids_project.repository.repositorycustom.NewsRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long>, NewsRepositoryCustom {
    /**
     * find news for parent
     * @return
     */
    List<News> findByAppParentTrueAndDelActiveTrueOrderByCreatedDateDesc();

    /**
     * find news for teacher
     * @return
     */
    List<News> findByAppTeacherTrueAndDelActiveTrueOrderByCreatedDateDesc();
    /**
     * find news for teacher
     * @return
     */
    List<News> findByAppPlusTrueAndDelActiveTrueOrderByCreatedDateDesc();

    List<News> findByDelActiveTrueOrderByIdDesc();
}
