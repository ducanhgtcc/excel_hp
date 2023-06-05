package com.example.onekids_project.repository;

import com.example.onekids_project.entity.user.SmsReceivers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SmsReiceiverRepository extends JpaRepository<SmsReceivers, Long> {

    @Query(value = "select * from ma_sms_receivers where id_sms_send=:idSmsSend and id_sms_code =1", nativeQuery = true)
    List<SmsReceivers> findByIdSmsSend(Long idSmsSend);

    @Query(value = "select * from ma_sms_receivers where id_sms_send=:idSmsSend", nativeQuery = true)
    List<SmsReceivers> findAllByIdSmsSend(Long idSmsSend);

    @Query(value = "select * from ma_sms_receivers where id_sms_send=:idSmsSend and id_sms_code !=1 or id_sms_code is null ", nativeQuery = true)
    List<SmsReceivers> findByIdSmsSendFail(Long idSmsSend);

    @Query(value = "select * from ma_sms_receivers where id_sms_send=:idSmsSend", nativeQuery = true)
    List<SmsReceivers> findByIdSmsSendAll(Long idSmsSend);

}
