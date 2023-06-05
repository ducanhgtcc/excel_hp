package com.example.onekids_project.repository;

import com.example.onekids_project.entity.system.MenuSupport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * date 2021-10-15 14:44
 *
 * @author lavanviet
 */
public interface MenuSupportRepository extends JpaRepository<MenuSupport, Long> {
    List<MenuSupport> findByPlusStatusTrue();
    List<MenuSupport> findByTeacherStatusTrue();
    List<MenuSupport> findByParentStatusTrue();
}
