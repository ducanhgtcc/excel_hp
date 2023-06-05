package com.example.onekids_project.repository;

import com.example.onekids_project.entity.sample.EvaluateSample;
import com.example.onekids_project.repository.repositorycustom.EvaluateSampleRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EvaluateSampleRepository extends JpaRepository<EvaluateSample, Long>, EvaluateSampleRepositoryCustom {
    /**
     * tìm kiếm mẫu đánh giá mặc định
     *
     * @param idSchool
     * @return
     */
    List<EvaluateSample> findByIdSchoolAndDelActiveTrueOrderByIdDesc(Long idSchool);

    /**
     * tìm kiếm mẫu đánh giá theo id
     *
     * @param id
     * @param idSchool
     * @return
     */
    Optional<EvaluateSample> findByIdAndIdSchoolAndDelActiveTrue(Long id, Long idSchool);
}
