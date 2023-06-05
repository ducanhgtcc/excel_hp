package com.example.onekids_project.repository;

import com.example.onekids_project.entity.user.SmsSend;
import com.example.onekids_project.repository.repositorycustom.SmsSendRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SmsSendRepository extends JpaRepository<SmsSend, Long>, SmsSendRepositoryCustom {

    Optional<SmsSend> findByIdAndDelActive(Long id, boolean b);

    Optional<SmsSend> findByIdAndDelActiveTrue(Long id);

    @Query(value = "select * from ma_sms_send where id_school=:idSchool ", nativeQuery = true)
    List<SmsSend> findAllByIsSchool(Long idSchool);

    List<SmsSend> findByIdInAndDelActiveTrueOrderByIdDesc(List<Long> idList);
}
