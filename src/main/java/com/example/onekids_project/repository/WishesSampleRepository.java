package com.example.onekids_project.repository;

import com.example.onekids_project.entity.sample.WishesSample;
import com.example.onekids_project.repository.repositorycustom.WishesSampleRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishesSampleRepository extends JpaRepository<WishesSample, Long>, WishesSampleRepositoryCustom {
    /**
     * find by id school
     * @param idSchool
     * @return
     */
    List<WishesSample> findByIdSchoolAndDelActiveTrueOrderByIdDesc(Long idSchool);

    /**
     * find by id
     * @param id
     * @return
     */
    Optional<WishesSample> findByIdAndDelActiveTrue(Long id);
}
