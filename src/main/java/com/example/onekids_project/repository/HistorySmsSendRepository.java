package com.example.onekids_project.repository;

import com.example.onekids_project.entity.user.SmsReceivers;
import com.example.onekids_project.entity.user.SmsSend;
import com.example.onekids_project.repository.repositorycustom.HistorySmsSendRepositoryCustom;
import com.example.onekids_project.repository.repositorycustom.SmsSendRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface HistorySmsSendRepository extends JpaRepository<SmsReceivers, Long>, HistorySmsSendRepositoryCustom {

    @Query(value = "select * from sms_receivers where id_sms_send=:idSmsSend", nativeQuery = true)
    List<SmsReceivers> findByIdSmsSend(Long idSmsSend);
}
