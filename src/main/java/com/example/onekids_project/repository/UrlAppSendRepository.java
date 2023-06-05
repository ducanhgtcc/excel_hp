package com.example.onekids_project.repository;

import com.example.onekids_project.entity.user.UrlFileAppSend;
import com.example.onekids_project.repository.repositorycustom.UrlAppSendRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UrlAppSendRepository extends JpaRepository<UrlFileAppSend, Long>, UrlAppSendRepositoryCustom {
    /**
     * tìm kiếm theo id
     *
     * @param id
     * @return
     */
    Optional<UrlFileAppSend> findByIdAndDelActiveTrue(Long id);


}

