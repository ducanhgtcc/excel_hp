package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.kids.KidsExtraInfo;

import java.util.Optional;

public interface KidsExtraInfoRepositoryCustom {
    /**
     * tìm kiếm thông tin mở rộng của học sinh
     *
     * @param idSchool
     * @param id
     * @return
     */
    Optional<KidsExtraInfo> findByIdKidsExtraInfo(Long idSchool, Long id);
}
