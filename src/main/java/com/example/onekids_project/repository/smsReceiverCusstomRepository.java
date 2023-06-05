package com.example.onekids_project.repository;

import com.example.onekids_project.entity.user.SmsReceiversCustom;
import com.example.onekids_project.repository.repositorycustom.SmsReceiverCusstomRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface smsReceiverCusstomRepository extends JpaRepository<SmsReceiversCustom, Long>, SmsReceiverCusstomRepositoryCustom {

}
