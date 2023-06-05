package com.example.onekids_project.repository;

import com.example.onekids_project.entity.employee.AbsentTeacherAttackFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * date 2021-05-22 9:15 AM
 *
 * @author nguyễn văn thụ
 */
public interface AbsentTeacherAttachFileRepository extends JpaRepository<AbsentTeacherAttackFile, Long> {
    Optional<AbsentTeacherAttackFile> findByIdAndDelActiveTrue(Long id);
}
