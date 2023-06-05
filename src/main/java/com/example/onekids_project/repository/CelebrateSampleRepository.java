package com.example.onekids_project.repository;

import com.example.onekids_project.entity.school.CelebrateSample;
import com.example.onekids_project.repository.repositorycustom.CelebrateRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CelebrateSampleRepository extends JpaRepository<CelebrateSample, Long>, CelebrateRepositoryCustom {
    /**
     * find by idschool
     * @param idSchool
     * @return
     */
    List<CelebrateSample> findByIdSchoolAndDelActiveTrueOrderByIdDesc(Long idSchool);

    List<CelebrateSample> findAllByDelActiveTrue();

    List<CelebrateSample> findAllByAndDateAndMonthAndActiveTrueAndDelActiveTrue(String date, String month);

    /**
     * find by id
     * @param id
     * @return
     */
    Optional<CelebrateSample> findByIdAndDelActiveTrue(Long id);
}
