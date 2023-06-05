package com.example.onekids_project.repository;

import com.example.onekids_project.entity.school.NotificationManage;
import com.example.onekids_project.repository.repositorycustom.NotificationManageRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * date 2021-07-28 3:55 PM
 *
 * @author nguyễn văn thụ
 */
public interface NotificationManageRepository extends JpaRepository<NotificationManage, Long> , NotificationManageRepositoryCustom {

    List<NotificationManage> findByIdSchool(Long idSchool);

    Optional<NotificationManage> findByIdAndIdSchoolAndDelActiveTrue(Long id, Long idSchool);

    Optional<NotificationManage> findByIdAndDelActiveTrue(Long id);
    //cron job
    Optional<NotificationManage> findByIdSchoolAndTypeAndTypeReceiveAndDelActiveTrue(Long idSchool, String type, String typeReceive);
}
