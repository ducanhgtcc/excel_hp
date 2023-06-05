package com.example.onekids_project.repository;

import com.example.onekids_project.entity.system.SysInfor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SysInforRepository extends JpaRepository<SysInfor, Long> {
    /**
     * lấy ra row đầu tiên trong bảng sysinfo
     *
     * @return
     */
//    SysInfor findFirstByDelActiveTrue();

    Optional<SysInfor> findFirstByDelActiveTrue();

    /**
     * find by id
     *
     * @param id
     * @return
     */
    Optional<SysInfor> findByIdAndDelActiveTrue(Long id);

    /**
     * find all
     *
     * @return
     */
    List<SysInfor> findAllByDelActiveTrue();

}
