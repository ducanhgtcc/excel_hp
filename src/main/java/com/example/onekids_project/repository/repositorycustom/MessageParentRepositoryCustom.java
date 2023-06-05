package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.parent.MessageParent;
import com.example.onekids_project.mobile.plus.request.SearchMessagePlusRequest;
import com.example.onekids_project.mobile.request.SearchMessageTeacherRequest;
import com.example.onekids_project.request.parentdiary.SearchMessageParentRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MessageParentRepositoryCustom {

    List<MessageParent> findAllMessageParent(Long idSchoolLogin, Pageable pageable);

    List<MessageParent> searchMessageParent(Long idSchool, SearchMessageParentRequest request);

    long countSearchMessageParent(Long idSchool, SearchMessageParentRequest request);

    Optional<MessageParent> findByIdMessage(Long idSchool, Long id);

    /**
     * tìm kiếm danh sách lời nhắn phụ huynh cho mobile
     *
     * @param idSchool
     * @param idKid
     * @return
     */
    List<MessageParent> findMessageParentMobile(Long idSchool, Long idKid, Pageable pageable, LocalDateTime localDateTime);

    /**
     * đếm số lượng lời nhắn
     *
     * @param idSchool
     * @param idKid
     * @return
     */
    long getCountMessage(Long idSchool, Long idKid, LocalDateTime localDateTime);

    List<MessageParent> findMessageforTeacherMobile(Long idSchool, Long idClass,  SearchMessageTeacherRequest searchMessageTeacherRequest);

    List<MessageParent> findMessageforPlus(Long idSchool, SearchMessagePlusRequest request);

    List<MessageParent> findMessageParentWithDate(Long idSchool, List<Long> idClassList, LocalDate date);
}


