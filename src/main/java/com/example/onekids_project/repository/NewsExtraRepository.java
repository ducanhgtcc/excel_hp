package com.example.onekids_project.repository;

import com.example.onekids_project.entity.system.NewsExtra;
import com.example.onekids_project.repository.repositorycustom.NewsExtraRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NewsExtraRepository extends JpaRepository<NewsExtra, Long>, NewsExtraRepositoryCustom {
    /**
     * find for parent
     * @return
     */
    Optional<NewsExtra> findFirstByAppParentTrueAndDelActiveTrue();

    /**
     * find for teacher
     * @return
     */
    Optional<NewsExtra> findFirstByAppTeacherTrueAndDelActiveTrue();

    Optional<NewsExtra> findFirstByAppPlusTrueAndDelActiveTrue();
}
