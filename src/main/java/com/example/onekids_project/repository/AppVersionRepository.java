package com.example.onekids_project.repository;

import com.example.onekids_project.entity.appversion.AppVersion;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AppVersionRepository extends CrudRepository<AppVersion, Long> {
    /**
     * tìm kiếm tất cả
     * @return
     */
    List<AppVersion> findByDelActiveTrue();

    /**
     * find by id app version
     * @param id
     * @return
     */
    Optional<AppVersion> findByIdAndDelActiveTrue(Long id);
}
