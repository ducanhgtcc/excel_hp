package com.example.onekids_project.repository;

import com.example.onekids_project.entity.school.NotificationManageDate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * date 2021-07-28 3:55 PM
 *
 * @author nguyễn văn thụ
 */
public interface NotificationManageDateRepository extends JpaRepository<NotificationManageDate, Long> {
    Optional<NotificationManageDate> findByIdAndDelActiveTrue(Long id);

    List<NotificationManageDate> findAllByNotificationManageStatusTrueAndNotificationManageDelActiveTrueAndStatusTrueAndDelActiveTrue();

    List<NotificationManageDate> findAllByStatusTrueAndNotificationManageDelActiveTrueAndDelActiveTrue();

    List<NotificationManageDate> findAllByStatusTrueAndDelActiveTrue();
}
