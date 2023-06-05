package com.example.onekids_project.repository;

import com.example.onekids_project.entity.user.Receivers;
import com.example.onekids_project.repository.repositorycustom.ReceiversRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReceiversRepository extends JpaRepository<Receivers, Long>, ReceiversRepositoryCustom {
    /**
     * tìm kiếm theo id
     *
     * @param id
     * @return
     */
    Optional<Receivers> findByIdAndDelActiveTrue(Long id);

    List<Receivers> findByAppSendIdAndDelActiveTrue(Long idUser);

    List<Receivers> findByIdClass(Long idClass);

    List<Receivers> findAllByAppSendId(Long idAppSend);

    Optional<Receivers> findByAppSendIdAndIdUserReceiverAndDelActiveTrue(Long idSend, Long idUserReceiver);
    List<Receivers> findByAppSendIdAndIdUserReceiverInAndDelActiveTrue(Long idSend, List<Long> idUserReceiverList);

//    int countByIdUserReceiverAndIsApprovedTrueAndUserUnreadFalseAndSendDelFalseAndDelActiveTrue(Long idUser);
    int countByIdUserReceiverAndIdSchoolAndIsApprovedTrueAndUserUnreadFalseAndSendDelFalseAndDelActiveTrue(Long idSchool, Long idUser);

    int countByIdUserReceiverAndIdSchoolAndUserUnreadFalseAndSendDelFalseAndDelActiveTrue(Long idSchool, Long idUser);

    List<Receivers> findByIdIn(List<Long> idList);

}

