package com.example.onekids_project.repository;

import com.example.onekids_project.entity.user.SmsSend;
import com.example.onekids_project.repository.repositorycustom.SmsSendHistoryRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmsSendHistoryRepository extends JpaRepository<SmsSend, Long>, SmsSendHistoryRepositoryCustom {


}
