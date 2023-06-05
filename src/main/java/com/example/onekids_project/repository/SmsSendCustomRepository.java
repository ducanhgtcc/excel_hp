package com.example.onekids_project.repository;

import com.example.onekids_project.entity.user.SmsSendCustom;
import com.example.onekids_project.repository.repositorycustom.SmsSendCustomRepositoryCustom;
import com.example.onekids_project.request.notifihistory.SearchSmsSendCustomRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SmsSendCustomRepository extends JpaRepository<SmsSendCustom, Long>, SmsSendCustomRepositoryCustom {

    List<SmsSendCustom> findAllByIdSchoolAndDelActiveTrue(Long idSchool);
}
