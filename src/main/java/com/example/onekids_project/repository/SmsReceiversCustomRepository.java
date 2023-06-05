package com.example.onekids_project.repository;

import com.example.onekids_project.entity.user.SmsReceivers;
import com.example.onekids_project.entity.user.SmsReceiversCustom;
import com.example.onekids_project.mobile.plus.request.historyNotifiRequest.DetailSmsRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.swing.text.html.Option;
import java.util.List;

public interface SmsReceiversCustomRepository extends JpaRepository<SmsReceiversCustom, Long> {

    @Query(value = "select * from sms_receivers_custom where id_send_custom=:id", nativeQuery = true)
    List<SmsReceiversCustom> findSmsReceiverCustom(Long id);

    @Query(value = "select * from sms_receivers_custom where id_send_custom=:idSmsSend and id_sms_code =1", nativeQuery = true)
    List<SmsReceiversCustom> findByIdSmsSend(Long idSmsSend);

    @Query(value = "select * from sms_receivers_custom where id_send_custom=:idSmsSend and id_sms_code !=1 or id_sms_code is null ", nativeQuery = true)
    List<SmsReceiversCustom> findByIdSmsSendFail(Long idSmsSend);

    @Query(value = "select * from sms_receivers_custom where id_send_custom=:idSmsSend", nativeQuery = true)
    List<SmsReceiversCustom> findByIdSmsSendAll(Long idSmsSend);

}
