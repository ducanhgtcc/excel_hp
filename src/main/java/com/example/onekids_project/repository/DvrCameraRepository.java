package com.example.onekids_project.repository;

import com.example.onekids_project.entity.school.DvrCamera;
import com.example.onekids_project.repository.repositorycustom.DvrCameraRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DvrCameraRepository extends JpaRepository<DvrCamera, Long>, DvrCameraRepositoryCustom {
    /**
     * tìm kiếm tất cả dvrcamera
     * @param idSchool
     * @return
     */
    List<DvrCamera> findBySchoolIdAndDelActiveTrueOrderByDvrNameAsc(Long idSchool);

    /**
     * tìm kiếm dvrcamera theo id
     * @param id
     * @return
     */
    Optional<DvrCamera> findByIdAndDelActiveTrue(Long id);
}
