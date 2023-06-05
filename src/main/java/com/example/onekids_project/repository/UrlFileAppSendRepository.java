package com.example.onekids_project.repository;

import com.example.onekids_project.entity.user.UrlFileAppSend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UrlFileAppSendRepository extends JpaRepository<UrlFileAppSend, Long> {

    @Modifying
    @Query(value = "select * from url_file_app_send where id_appsend=:id ", nativeQuery = true)
    List<UrlFileAppSend> findUrlFileAppSendByThanh(Long id);
}
