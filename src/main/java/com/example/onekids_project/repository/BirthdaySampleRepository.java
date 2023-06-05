package com.example.onekids_project.repository;

import com.example.onekids_project.entity.school.BirthdaySample;
import com.example.onekids_project.repository.repositorycustom.BirthdaySampleRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BirthdaySampleRepository extends JpaRepository<BirthdaySample, Long>, BirthdaySampleRepositoryCustom {

    /**
     * tìm kiếm mẫu sinh nhật theo id
     * @param id
     * @return
     */
    Optional<BirthdaySample> findByIdAndDelActiveTrue(Long id);
    /**
     * tìm kiếm mẫu lời chúc sinh nhật
     * @param idSchool
     * @return
     */
    List<BirthdaySample> findByIdSchoolAndDelActiveTrue(Long idSchool);

    Optional<BirthdaySample> findByIdSchoolAndBirthdayTypeAndActiveTrue(Long idSchool, String birdayType);

    Optional<BirthdaySample> findByIdSchoolAndBirthdayTypeAndAppSendTrue(Long idSchool, String birdayType);
    Optional<BirthdaySample> findByIdSchoolAndBirthdayType(Long idSchool, String birdayType);
    Optional<BirthdaySample> findByIdSchoolAndBirthdayTypeAndSmsSendTrue(Long idSchool, String birdayType);

    Optional<BirthdaySample> findByIdSchoolAndBirthdayTypeAndActiveTrueAndSmsSendTrue(Long idSchool, String birdayType);
}
