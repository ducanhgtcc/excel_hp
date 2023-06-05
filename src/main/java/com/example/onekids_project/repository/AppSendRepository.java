package com.example.onekids_project.repository;

import com.example.onekids_project.entity.user.AppSend;
import com.example.onekids_project.repository.repositorycustom.AppSendRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AppSendRepository extends JpaRepository<AppSend, Long>, AppSendRepositoryCustom {
    Optional<AppSend> findByIdAndDelActive(Long id, boolean delActive);

    int countByIdSchoolAndIsApprovedFalseAndSendTypeAndDelActiveTrueAndSendDelFalse(Long idSchool, String sendType);

    @Query(value = "SELECT * FROM ma_app_send as model where del_active = 1 and exists(select * from ma_receivers as model1 where model1.id_send =  model.id and model1.id_class = id_class ) ", nativeQuery = true)
    List<AppSend> findAllById(Long idCLass);
}
