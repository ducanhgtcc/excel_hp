package com.example.onekids_project.repository;

import com.example.onekids_project.entity.user.FeedBack;
import com.example.onekids_project.repository.repositorycustom.FeedBackRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FeedBackRepository extends JpaRepository<FeedBack, Long>, FeedBackRepositoryCustom {
    Optional<FeedBack> findByIdAndDelActive(Long id, boolean delActive);

    Optional<FeedBack> findByIdAndDelActiveTrue(Long id);
    Optional<FeedBack> findByIdAndSchoolConfirmStatusFalseAndDelActiveTrue(Long id);

    List<FeedBack> findByIdKidAndIdSchoolAndDelActive(Long idKid, Long idSchool, boolean delActive);

    List<FeedBack> findByIdKidAndIdSchoolAndSchoolConfirmStatusFalseAndDelActiveTrue(Long idKid, Long idSchool);

    Optional<FeedBack> findByIdAndIdSchoolAndDelActiveTrue(Long id, Long idSchool);

    int countByIdCreatedAndParentUnreadFalse(Long idCreate);

    int countByIdSchoolAndSchoolConfirmStatusFalse(Long idSchool);
}
