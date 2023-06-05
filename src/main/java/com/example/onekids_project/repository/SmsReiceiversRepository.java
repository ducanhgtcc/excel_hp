package com.example.onekids_project.repository;

import com.example.onekids_project.entity.user.SmsReceivers;
import com.example.onekids_project.entity.user.SmsReceiversCustom;
import com.example.onekids_project.repository.repositorycustom.SmsReiceriversRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SmsReiceiversRepository extends JpaRepository<SmsReceivers, Long>, SmsReiceriversRepositoryCustom {

    Optional<SmsReceivers> findByIdAndDelActive(Long id, boolean b);

    @Query(value = "select * from ma_sms_receivers where id_sms_send=:idSmsSend ", nativeQuery = true)
    List<SmsReceivers> findByIdSmsSend(Long idSmsSend);
}
