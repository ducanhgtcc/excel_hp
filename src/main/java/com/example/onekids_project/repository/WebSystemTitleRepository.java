package com.example.onekids_project.repository;

import com.example.onekids_project.entity.system.WebSystemTitle;
import com.example.onekids_project.repository.repositorycustom.WebSystemTitleRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WebSystemTitleRepository extends JpaRepository<WebSystemTitle, Long>, WebSystemTitleRepositoryCustom {
    /**
     * tìm kiếm tất cả
     * @return
     */
    List<WebSystemTitle> findByDelActiveTrue();

    /**
     * tìm kiếm theo id
     * @param id
     * @return
     */
    Optional<WebSystemTitle> findByIdAndDelActiveTrue(Long id);
}
