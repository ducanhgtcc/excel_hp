package com.example.onekids_project.repository;

import com.example.onekids_project.entity.parent.MessageParent;
import com.example.onekids_project.repository.repositorycustom.MessageParentRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MessageParentRepository extends JpaRepository<MessageParent, Long>, MessageParentRepositoryCustom {

    Optional<MessageParent> findByIdMessage(Long idSchoolLogin, Long id);

    List<MessageParent> findByKidsIdAndIdSchoolAndDelActive(Long idKid, Long idSchool, boolean delActive);

    List<MessageParent> findByKidsIdAndIdSchoolAndParentMessageDelFalseAndDelActiveTrue(Long idKid, Long idSchool);

    /**
     * tìm kiếm lời nhắn chưa bị xóa
     *
     * @param id
     * @return
     */
    Optional<MessageParent> findByIdAndDelActiveTrue(Long id);

    int countByIdClassAndParentMessageDelFalseAndConfirmStatusFalseAndDelActiveTrue(Long idClass);
    int countByIdClassAndParentMessageDelFalseAndConfirmStatusFalseAndDelActiveTrueAndDelActiveTrue(Long idClass);

    int countByIdSchoolAndParentMessageDelFalseAndConfirmStatusFalseAndDelActiveTrueAndKidsDelActiveTrue(Long idSchool);

    Optional<MessageParent> findByIdAndConfirmStatusFalseAndDelActiveTrue(Long id);

    List<MessageParent> findAllByIdSchoolAndConfirmStatusFalseAndDelActiveTrue(Long idSchool);
}
