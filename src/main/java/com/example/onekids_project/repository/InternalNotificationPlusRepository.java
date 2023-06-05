package com.example.onekids_project.repository;

import com.example.onekids_project.entity.school.InternalNotificationPlus;
import com.example.onekids_project.repository.repositorycustom.InternalNotificationPlusCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * date 2021-07-28 3:46 PM
 *
 * @author nguyễn văn thụ
 */
public interface InternalNotificationPlusRepository extends JpaRepository<InternalNotificationPlus, Long>, InternalNotificationPlusCustomRepository {
    Optional<InternalNotificationPlus> findByIdAndDelActiveTrue(Long id);
}
